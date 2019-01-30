package org.secfirst.advancedsearch.mvp.presentation

import android.content.Intent
import org.secfirst.advancedsearch.*
import org.secfirst.advancedsearch.mvp.Presenter
import org.secfirst.advancedsearch.mvp.SameThreadSpec
import org.secfirst.advancedsearch.mvp.ThreadSpec
import org.secfirst.advancedsearch.mvp.data.SegmentDao
import rx.Observable
import java.util.logging.Logger

class SearchResultPresenter(private val segmentDao: SegmentDao?,
                            private val criteriaList: List<SearchCriteria>,
                            threadSpec: ThreadSpec = SameThreadSpec()) :
    Presenter<SearchResultPresenter.View>(threadSpec) {

    private val TAG = javaClass.simpleName

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

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

        view.onSearchClicked().subscribeUntilDetached { list ->
            val filters = list.map { it }
            AdvancedSearch.getSearchTermFromCompositeView(filters).let {searchTerm ->
                val criteriaMap = hashMapOf<String, SearchCriteria>()
                criteriaList.associateByTo(criteriaMap) { it.name }
                searchTerm.criteria?.filterNot { it.first == "text" }?.forEach {
                    criteriaMap[it.first]?.searchFor = it.second
                }
                performSearch(view, searchTerm, criteriaMap)
            }
        }

        view.onIntentReceived().subscribeUntilDetached { intent ->
            AdvancedSearch.getSearchTermFromIntent(intent).let {searchTerm ->
                val criteriaMap = hashMapOf<String, SearchCriteria>()
                criteriaList.associateByTo(criteriaMap) { it.name }
                performSearch(view, searchTerm, criteriaMap)
            }
        }

        view.onAdvancedToggleClick().subscribeUntilDetached {
            view.toggleAdvancedCriteria()
        }

    }

    private fun performSearch(
        view: View,
        searchTerm: SearchTerm,
        criteriaMap: HashMap<String, SearchCriteria>
    ) {
        Logger.getLogger(TAG).info("Searching for ${searchTerm}")
        view.resetResults()
        view.displaySearchTerm(searchTerm.text)
        bg {
            segmentDao?.findByCriteria(searchTerm.text, criteriaMap["category"]?.searchFor ?: "", criteriaMap["difficulty"]?.searchFor ?: "")?.subscribe { segmentList ->
                when (segmentList.isNotEmpty()) {
                    true -> {
                        ui {
                            view.hideEmptyView()
                            view.hideErrorView()
                            view.showResultsView()
                            segmentList.forEach { Logger.getLogger(TAG).info("Result $it") }
                            view.addResultsToAdapter(*segmentList.map {
                                SearchResult(
                                    it.title,
                                    it.text,
                                    "deeplink"
                                )
                            }.toTypedArray())
                            view.displaySearchTermWithResultCount(searchTerm.text, segmentList.size)
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
        fun onSearchClicked(): Observable<List<Pair<String, String>>>

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
        fun displaySearchTermWithResultCount(searchTerm: String, count: Int)
    }
}