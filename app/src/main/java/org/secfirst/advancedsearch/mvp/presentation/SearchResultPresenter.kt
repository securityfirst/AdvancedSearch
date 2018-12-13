package org.secfirst.advancedsearch.mvp.presentation

import android.content.Context
import android.content.Intent
import android.widget.Toast
import org.secfirst.advancedsearch.AdvancedSearch
import org.secfirst.advancedsearch.mvp.Presenter
import org.secfirst.advancedsearch.mvp.SameThreadSpec
import org.secfirst.advancedsearch.mvp.ThreadSpec
import rx.Observable
import java.util.logging.Logger

class SearchResultPresenter(private val context: Context, threadSpec: ThreadSpec = SameThreadSpec()) :
    Presenter<SearchResultPresenter.View>(threadSpec) {

    val TAG = javaClass.simpleName

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        view.onIntentReceived().subscribeUntilDetached {
            AdvancedSearch.getQueryFromSearchIntent(it)?.let {
                Logger.getLogger(TAG).info("Searching for ${it}")
                view.displaySearchTerm(it)
            } ?: kotlin.run {
                view.hideSearchTermView()
                Toast.makeText(context, "Cannot find variable", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun displaySearchTerm(view: View, searchTerm: String) = ui {
        view.displaySearchTerm(searchTerm)
    }

    interface View: Presenter.View {
        fun onIntentReceived(): Observable<Intent>

        fun displaySearchTerm(searchTerm: String)
        fun passIntent(intent: Intent)
        fun hideSearchTermView()
    }
}