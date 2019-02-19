package org.secfirst.advancedsearch.presenters

import android.content.Intent
import org.secfirst.advancedsearch.*
import org.secfirst.advancedsearch.interfaces.DataProvider
import org.secfirst.advancedsearch.models.FieldTypes
import org.secfirst.advancedsearch.models.SearchCriteria
import org.secfirst.advancedsearch.models.SearchResult
import org.secfirst.advancedsearch.models.SearchTerm
import org.secfirst.advancedsearch.util.mvp.Presenter
import org.secfirst.advancedsearch.util.mvp.SameThreadSpec
import org.secfirst.advancedsearch.util.mvp.ThreadSpec
import rx.Observable
import java.util.logging.Logger

class SearchResultPresenter(private val dataProvider: DataProvider,
                            private val criteriaList: List<SearchCriteria>,
                            threadSpec: ThreadSpec = SameThreadSpec()
) :
    Presenter<SearchResultPresenter.View>(threadSpec) {

    private val tag = javaClass.simpleName

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        setSearchFields(view)

        view.onSearchClicked().subscribeUntilDetached { list ->
            AdvancedSearch.getSearchTermFromCompositeView(list).let {searchTerm ->
                performSearch(view, searchTerm)
                view.hideApplyResultsView()
            }
        }

        view.onIntentReceived().subscribeUntilDetached { intent ->
            AdvancedSearch.getSearchTermFromIntent(intent).let {searchTerm ->
                performSearch(view, searchTerm)
                view.hideApplyResultsView()
            }
        }

        view.onAdvancedToggleClick().subscribeUntilDetached {
            view.toggleAdvancedCriteria()
        }

        view.onCancelClicked().subscribeUntilDetached {
            view.resetResults()
            view.showApplyResultsView()
            view.emptyFields()
            setSearchFields(view)
        }

    }

    private fun setSearchFields(view: View) {
        criteriaList.forEach {
            when(it.type) {
                FieldTypes.STRING -> {
                    view.addEditTextToLayout(it)
                }
                FieldTypes.PILLBOX -> {
                    view.addPillboxToLayout(it)
                }
                FieldTypes.FREE_TEXT -> {
                    view.addMainTextToLayout(it)
                }
                else -> {
                    view.addEditTextToLayout(it)
                }
            }

        }
    }

    private fun performSearch(
        view: View,
        searchTerm: SearchTerm
    ) {
        Logger.getLogger(tag).info("Searching for $searchTerm")
        view.resetResults()
        view.displaySearchTerm(searchTerm.text)
        bg {
            dataProvider.findByCriteria(searchTerm.text, *searchTerm.criteria?.toTypedArray() ?: arrayOf()).subscribe { segmentList: List<SearchResult> ->
                when (segmentList.isNotEmpty()) {
                    true -> {
                        ui {
                            view.hideEmptyView()
                            view.hideErrorView()
                            view.showResultsView()
                            segmentList.forEach { Logger.getLogger(tag).info("Result $it") }
                            view.addResultsToAdapter(*segmentList.toTypedArray())
                            view.displayResultCountView(segmentList.size)
                            val criteria: String = searchTerm
                                .criteria
                                ?.filterNot { it.second.isEmpty() }
                                ?.joinToString(
                                " "
                            ) {
                                "\n${it.first}: ${it.second.joinToString()}"
                            } ?: ""
                            view.displaySearchTermWithResultCount(searchTerm.text, segmentList.size, criteria)
                        }
                    }
                    false -> {
                        ui {
                            view.showEmptyView()
                            view.hideErrorView()
                            view.hideResultsView()
                        }
                    }
                }
            } ?: kotlin.run {
                ui {
                    view.hideEmptyView()
                    view.hideResultsView()
                    view.showErrorView()
                }
            }
        }
    }

    interface View: Presenter.View {
        fun onIntentReceived(): Observable<Intent>
        fun onSearchClicked(): Observable<HashMap<String, List<String>>>
        fun onCancelClicked(): Observable<Unit>

        fun displaySearchTerm(searchTerm: String)
        fun passIntent(intent: Intent)
        fun hideSearchTermView()
        fun addResultsToAdapter(vararg results: SearchResult)
        fun hideEmptyView()
        fun showEmptyView()
        fun resetResults()
        fun showErrorView()
        fun hideErrorView()
        fun showResultsView()
        fun hideResultsView()
        fun showAdvancedCriteria()
        fun hideAdvancedCriteria()
        fun onAdvancedToggleClick(): Observable<Unit>
        fun toggleAdvancedCriteria()
        fun addPillboxToLayout(criteria: SearchCriteria)
        fun addEditTextToLayout(criteria: SearchCriteria)
        fun addMainTextToLayout(criteria: SearchCriteria)
        fun emptyFields()
        fun showApplyResultsView()
        fun hideApplyResultsView()
        fun displayResultCountView(count: Int)
        fun displaySearchTermWithResultCount(searchTerm: String, count: Int, criteria: String)
    }
}