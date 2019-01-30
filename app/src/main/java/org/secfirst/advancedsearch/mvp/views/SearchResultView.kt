package org.secfirst.advancedsearch.mvp.views

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxrelay.BehaviorRelay
import kotlinx.android.synthetic.main.search_view.view.*
import org.secfirst.advancedsearch.FieldTypes
import org.secfirst.advancedsearch.R
import org.secfirst.advancedsearch.mvp.adapters.SearchResultAdapter
import org.secfirst.advancedsearch.mvp.models.PillItem
import org.secfirst.advancedsearch.SearchCriteria
import org.secfirst.advancedsearch.SearchResult
import org.secfirst.advancedsearch.mvp.models.Category
import org.secfirst.advancedsearch.mvp.models.Difficulty
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

    private var presenter: SearchResultPresenter? = null

    private val intentReceivedRelay = BehaviorRelay.create<Intent>()
    private val searchAppliedRelay = BehaviorRelay.create<List<Pair<String, String>>>()

    override fun onIntentReceived(): Observable<Intent> = intentReceivedRelay
    override fun onSearchClicked(): Observable<List<Pair<String, String>>> = searchAppliedRelay
    override fun onAdvancedToggleClick(): Observable<Unit> = advancedCriteriaToggle.clicks()

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
        val categories = listOf(Category("personal", "Personal", ""), Category("information", "Information", ""))
        val difficulties = listOf(Difficulty("beginner", "Beginner"), Difficulty("advanced", "Advanced"))
        val categoryOptions = categories.map { it.title }
        val difficultyOptions = difficulties.map { it.name }
        val criteria = mutableListOf<SearchCriteria>()
        criteria.add(SearchCriteria("category", FieldTypes.PILLBOX, categoryOptions,null))
        criteria.add(SearchCriteria("difficulty", FieldTypes.STRING, difficultyOptions,null))
        criteria.add(SearchCriteria("text", FieldTypes.FREE_TEXT, null,null))

        presenter = SearchResultPresenter(
            Global.instance.db?.segmentDao(),
            criteria,
            Global.instance.getThreadSpec()
        )
        presenter?.onViewAttached(this)
        passIntent((context as AppCompatActivity).intent)
        searchApply.setOnClickListener {
            val list: List<Pair<String, String>> = (0..criteriaLayout.childCount).filter {
                criteriaLayout.getChildAt(it) != null && (criteriaLayout.getChildAt(it) as EditText).text.toString().isNotEmpty()
            }.map {
                val child = criteriaLayout.getChildAt(it)
                Pair(child.tag as String, (child as EditText).text.toString())
            }
            if (list.isEmpty()) {
                Logger.getLogger("aaa").info("empty list")
            } else {
                Logger.getLogger("aaa").info(list.joinToString { "\'${it.first} ${it.second}\'" })
            }
            searchAppliedRelay.call(list)
        }
    }

    override fun passIntent(intent: Intent) {
        intentReceivedRelay.call(intent)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter?.onViewDetached(this)
    }

    override fun displaySearchTerm(searchTerm: String) {
        searchTermView.visibility = View.VISIBLE
        searchTermView.text = context.getString(R.string.results_while_searching, searchTerm)
        (0..criteriaLayout.childCount).forEach {
            if (criteriaLayout.getChildAt(it) != null && criteriaLayout.getChildAt(it).tag.equals("text")) {
                (criteriaLayout.getChildAt(it) as EditText).setText(searchTerm)
            }
        }
    }

    override fun displaySearchTermWithResultCount(searchTerm: String, count: Int) {
        searchTermView.visibility = View.VISIBLE
        searchTermView.text = "$count ${context.getString(R.string.results_while_searching, searchTerm)}"

    }

    override fun hideSearchTermView() {
        searchTermView.visibility = View.GONE
    }

    override fun addResultsToAdapter(vararg results: SearchResult) {
        searchResultAdapter.append(*results)
    }

    override fun resetResults() {
        searchResultAdapter.reset()
    }

    override fun hideEmptyView() {
        searchResultsEmptyView.visibility = View.GONE
    }

    override fun showEmptyView() {
        searchResultsEmptyView.visibility = View.VISIBLE
    }

    override fun showErrorView() {
        searchResultsNoSearchTerm.visibility = View.VISIBLE
    }

    override fun hideErrorView() {
        searchResultsNoSearchTerm.visibility = View.GONE
    }

    override fun showResultsView() {
        searchResultsListView.visibility = View.VISIBLE
    }

    override fun hideResultsView() {
        searchResultsListView.visibility = View.GONE
    }

    override fun toggleAdvancedCriteria() {
        when(advancedCriteriaPanel.visibility == View.VISIBLE) {
            true -> hideAdvancedCriteria()
            false -> showAdvancedCriteria()
        }
    }

    override fun showAdvancedCriteria() {
        advancedCriteriaPanel.visibility = View.VISIBLE
    }

    override fun hideAdvancedCriteria() {
        advancedCriteriaPanel.visibility = View.GONE
    }

    override fun addPillboxToLayout(criteria: SearchCriteria) {
        Logger.getLogger("aaa").info(criteria.values?.toTypedArray().toString())
        val adapter = ArrayAdapter<String>(
            context,
            android.R.layout.simple_dropdown_item_1line,
            criteria.values?.toTypedArray() ?: arrayOf()
        )
        val addView = AutoCompleteTextView(context)
        addView.setAdapter(adapter)
        addView.threshold = 1
        addView.hint = criteria.name
        addView.tag = criteria.name
        criteriaLayout.addView(addView)
    }

    override fun addEditTextToLayout(criteria: SearchCriteria) {
        val addView = EditText(context)
        addView.hint = criteria.name
        addView.tag = criteria.name
        criteriaLayout.addView(addView)
    }

    override fun addMainTextToLayout(criteria: SearchCriteria) {
        val addView = EditText(context)
        addView.setSingleLine(false)
        addView.maxLines = 3
        addView.tag = criteria.name
        addView.hint = criteria.name
        criteriaLayout.addView(addView)
    }
}