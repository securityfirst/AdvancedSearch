package org.secfirst.advancedsearch.mvp.views

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.jakewharton.rxrelay.BehaviorRelay
import kotlinx.android.synthetic.main.search_view.view.*
import org.secfirst.advancedsearch.mvp.presentation.SearchResultPresenter
import org.secfirst.advancedsearch.util.Global
import rx.Observable

class SearchResultView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), SearchResultPresenter.View {

    private val presenter: SearchResultPresenter by lazy {
        SearchResultPresenter(
            Global.instance,
            Global.instance.getThreadSpec()
        )
    }

    private val intentReceivedRelay = BehaviorRelay.create<Intent>()

    override fun onIntentReceived(): Observable<Intent> = intentReceivedRelay

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.onViewAttached(this)
    }

    override fun passIntent(intent: Intent) {
        intentReceivedRelay.call(intent)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.onViewDetached(this)
    }

    override fun displaySearchTerm(searchTerm: String) {
        searchTermView.visibility = View.VISIBLE
        searchTermView.text = searchTerm
    }

    override fun hideSearchTermView() {
        searchTermView.visibility = View.GONE
    }

}