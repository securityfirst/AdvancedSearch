package org.secfirst.advancedsearch.mvp.presentation

import android.content.Context
import android.content.Intent
import android.widget.Toast
import org.secfirst.advancedsearch.AdvancedSearch
import org.secfirst.advancedsearch.mvp.Presenter
import org.secfirst.advancedsearch.mvp.SameThreadSpec
import org.secfirst.advancedsearch.mvp.ThreadSpec
import org.secfirst.advancedsearch.mvp.data.SegmentDao
import org.secfirst.advancedsearch.mvp.models.SearchResult
import rx.Observable
import java.util.logging.Logger

class SearchResultPresenter(private val context: Context,
                            private val segmentDao: SegmentDao?,
                            threadSpec: ThreadSpec = SameThreadSpec()) :
    Presenter<SearchResultPresenter.View>(threadSpec) {

    val TAG = javaClass.simpleName

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        view.onIntentReceived().subscribeUntilDetached {
            AdvancedSearch.getQueryFromSearchIntent(it)?.let {
                Logger.getLogger(TAG).info("Searching for ${it}")
                view.displaySearchTerm(it)

                bg {
                    segmentDao?.findByTitleOrText(it, it)?.subscribe { segmentList ->
                        when (segmentList.isNotEmpty()) {
                            true -> {
                                segmentList.forEach { Logger.getLogger(TAG).info("Result $it") }
                                ui {
                                    view.addResultsToAdapter(*segmentList.map { SearchResult(it.title, it.text, "deeplink") }.toTypedArray())
                                }
                            }
                            false -> {
                                Logger.getLogger(TAG).info("No results found")
                            }
                        }
                    } ?: kotlin.run {
                        Logger.getLogger(TAG).info("No search term present, show empty view")
                    }
                }
            } ?: kotlin.run {
                view.hideSearchTermView()
                Toast.makeText(context, "Cannot find variable", Toast.LENGTH_SHORT).show()
            }
        }

    }

    interface View: Presenter.View {
        fun onIntentReceived(): Observable<Intent>

        fun displaySearchTerm(searchTerm: String)
        fun passIntent(intent: Intent)
        fun hideSearchTermView()
        fun addResultsToAdapter(vararg results: SearchResult)
    }
}