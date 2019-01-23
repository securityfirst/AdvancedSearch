package org.secfirst.advancedsearch.mvp.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxrelay.BehaviorRelay
import kotlinx.android.synthetic.main.search_view.*
import kotlinx.android.synthetic.main.search_view.view.*
import org.secfirst.advancedsearch.SearchActivity
import org.secfirst.advancedsearch.mvp.adapters.SearchResultAdapter
import org.secfirst.advancedsearch.mvp.models.PillItem
import org.secfirst.advancedsearch.mvp.models.SearchResult
import org.secfirst.advancedsearch.mvp.presentation.SearchResultPresenter
import org.secfirst.advancedsearch.util.Global
import pe.orbis.materialpillsbox.MaterialPillsBox
import pe.orbis.materialpillsbox.OnPillClickListener
import rx.Observable
import java.util.logging.Logger

class SearchResultView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), SearchResultPresenter.View {

    private val searchResultAdapter = SearchResultAdapter(mutableListOf(), context)

        private val presenter: SearchResultPresenter by lazy {
        SearchResultPresenter(
            Global.instance,
            Global.instance.db?.segmentDao(),
            Global.instance.getThreadSpec()
        )
    }

    private val intentReceivedRelay = BehaviorRelay.create<Intent>()

    override fun onIntentReceived(): Observable<Intent> = intentReceivedRelay

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        searchResultsListView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        searchResultsListView.adapter = searchResultAdapter

        val objects = ArrayList<PillItem>()
        objects.add(PillItem("First"))
        objects.add(PillItem("Second"))
        val pillsBox: MaterialPillsBox = mtbArea
        pillsBox.initFirstSetup(objects as List<Any>?)
        pillsBox.setOnPillClickListener(object  : OnPillClickListener {
            override fun onCloseIconClick(p0: View?, p1: Int) {
                Logger.getLogger("blah").info("event close: view text $p0, object ${objects[p1]}")
            }

            override fun onPillClick(p0: View?, p1: Int) {
                Logger.getLogger("blah").info("event click: view text $p0, object ${objects[p1].name}")
            }

        })
        presenter.onViewAttached(this)
        passIntent((context as AppCompatActivity).intent)
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

    override fun addResultsToAdapter(vararg results: SearchResult) {
        searchResultAdapter.append(*results)
    }

}