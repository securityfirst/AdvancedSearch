package org.secfirst.advancedsearch.views

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxrelay.BehaviorRelay
import kotlinx.android.synthetic.main.search_view.view.*
import org.secfirst.advancedsearch.adapters.SearchResultAdapter
import org.secfirst.advancedsearch.interfaces.AdvancedSearchPresenter
import org.secfirst.advancedsearch.library.R
import org.secfirst.advancedsearch.models.FieldTypes
import org.secfirst.advancedsearch.models.PillItem
import org.secfirst.advancedsearch.models.SearchCriteria
import org.secfirst.advancedsearch.models.SearchResult
import org.secfirst.advancedsearch.presenters.SearchResultPresenter
import org.secfirst.advancedsearch.util.SelectablePillBox
import org.secfirst.advancedsearch.util.asSequence
import org.secfirst.advancedsearch.util.hideKeyboard
import pe.orbis.materialpillsbox.OnPillClickListener
import pe.orbis.materialpillsbox.PillEntity
import rx.Observable
import java.util.logging.Logger

class SearchResultView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), SearchResultPresenter.View {

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.search_view, this)
    }

    private val searchResultAdapter = SearchResultAdapter(mutableListOf(), context)
    private val presenterInterface: AdvancedSearchPresenter = context as AdvancedSearchPresenter

    private val presenter: SearchResultPresenter by lazy {
        SearchResultPresenter(
            presenterInterface.getDataProvider(),
            presenterInterface.getCriteria(),
            presenterInterface.getThreadSpec()
        )
    }

    private val intentReceivedRelay = BehaviorRelay.create<Intent>()
    private val searchAppliedRelay = BehaviorRelay.create<List<Pair<String, String>>>()


    override fun onIntentReceived(): Observable<Intent> = intentReceivedRelay
    override fun onSearchClicked(): Observable<List<Pair<String, String>>> = searchAppliedRelay
    override fun onAdvancedToggleClick(): Observable<Unit> = advancedCriteriaToggle.clicks()
    override fun onCancelClicked(): Observable<Unit> = searchCancel.clicks()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
//        presenter = SearchResultPresenter(
//            presenterInterface.getDataProvider(),
//            presenterInterface.getCriteria(),
//            presenterInterface.getThreadSpec()
//        )
        presenter.onViewAttached(this)
        setSearchResultsListView()
    }

    private fun setSearchResultsListView() {
        searchResultsListView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        searchResultsListView.adapter = searchResultAdapter

        passIntent((context as AppCompatActivity).intent)
        val list = mutableListOf<Pair<String, String>>()
        searchApply.setOnClickListener {
            (criteriaLayout as ViewGroup).asSequence().iterator().forEach {criteriaChild ->
                when(criteriaChild) {
                    is EditText -> {
                        list.add(Pair(criteriaChild.tag as String, criteriaChild.text.toString()))
                    }
                    is LinearLayout -> {
                        (0..criteriaChild.childCount).filter {
                            criteriaChild.getChildAt(it) != null
                        }.forEach {item ->
                            val linearLayoutChild = criteriaChild.getChildAt(item)
                            Logger.getLogger("ccc").info("${linearLayoutChild::class.qualifiedName}")
                            linearLayoutChild?.let {
                                when(linearLayoutChild) {
                                    is SelectablePillBox -> {
                                        if (linearLayoutChild.selectedItems.isNotEmpty()) {
                                            list.add(Pair(linearLayoutChild.tag as String, linearLayoutChild.selectedItems[0]))
                                        }
                                    }
                                    else -> Logger.getLogger("bbb").info("${linearLayoutChild::class.qualifiedName}")
                                }
                            }
                        }
                    }
                }
            }
            searchAppliedRelay.call(list)
            context.hideKeyboard(this@SearchResultView)
        }
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
        searchTermView.text = context.getString(R.string.results_while_searching, searchTerm)
        (0..criteriaLayout.childCount).forEach {
            if (criteriaLayout.getChildAt(it)?.tag?.equals("text") == true) {
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
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        val objects = mutableListOf<PillEntity>()
        val pillsBox = LayoutInflater.from(context).inflate(R.layout.pill_item, null) as SelectablePillBox
        pillsBox.tag = criteria.name
        pillsBox.initFirstSetup(objects as List<Any>?)
        pillsBox.setOnPillClickListener(object  : OnPillClickListener {
            override fun onCloseIconClick(p0: View?, p1: Int) {
                objects[p1].let {
                    objects.removeAt(p1)
                    pillsBox.notifyDataSetChanged()
                }
            }

            override fun onPillClick(p0: View?, p1: Int) {
            }
        })
        val adapter = ArrayAdapter<String>(
            context,
            android.R.layout.simple_dropdown_item_1line,
            criteria.values?.toTypedArray() ?: arrayOf()
        )
        val addView = AutoCompleteTextView(context)
        addView.setAdapter(adapter)
        addView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            adapter.getItem(position)?.let {
                objects.add(PillItem(it))
                pillsBox.setObjects(objects)
                pillsBox.notifyDataSetChanged()
                addView.setText("")
            }
        }
        addView.threshold = 1
        addView.hint = criteria.name
        layout.tag = criteria.name
        layout.addView(pillsBox)
        layout.addView(addView)
        criteriaLayout.addView(layout)
    }

    fun refreshSearchCriteriaByTag(criteria: SearchCriteria) {
        (criteriaLayout as ViewGroup).asSequence().iterator().forEach {
            if (it.tag == criteria.name) {
                when(criteria.type) {
                    FieldTypes.STRING -> {
                        addEditTextToLayout(criteria)
                    }
                    FieldTypes.PILLBOX -> {
                        addPillboxToLayout(criteria)
                    }
                    FieldTypes.FREE_TEXT -> {
                        addMainTextToLayout(criteria)
                    }
                    else -> {
                        addEditTextToLayout(criteria)
                    }
                }
            }
        }
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

    override fun emptyFields() {
        criteriaLayout.removeAllViewsInLayout()
        hideSearchTermView()
    }
}