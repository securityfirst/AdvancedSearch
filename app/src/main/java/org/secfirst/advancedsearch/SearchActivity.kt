package org.secfirst.advancedsearch

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_search.*
import org.secfirst.advancedsearch.interfaces.AdvancedSearchPresenter
import org.secfirst.advancedsearch.interfaces.DataProvider
import org.secfirst.advancedsearch.models.FieldTypes
import org.secfirst.advancedsearch.models.SearchCriteria
import org.secfirst.advancedsearch.mvp.data.SegmentDaoImpl
import org.secfirst.advancedsearch.mvp.models.Category
import org.secfirst.advancedsearch.mvp.models.Difficulty
import org.secfirst.advancedsearch.util.mvp.ThreadSpec
import org.secfirst.advancedsearch.views.SearchResultView

class SearchActivity : AppCompatActivity(), AdvancedSearchPresenter {

    private val categoryOptions = listOf(Category("personal", "Personal", ""), Category("information", "Information", "")).map { it.title }
    private val difficultyOptions = listOf(Difficulty("beginner", "Beginner"), Difficulty("advanced", "Advanced")).map { it.name }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.search_results)
        }

        if (Intent.ACTION_SEARCH != intent.action) {
            intent.action = Intent.ACTION_SEARCH
            intent.putExtra(SearchManager.QUERY, "title")
        }
    }

    override fun getCriteria(): List<SearchCriteria> =
        listOf(
            SearchCriteria(
                "category",
                FieldTypes.PILLBOX,
                categoryOptions,
                null
            ),
            SearchCriteria(
                "difficulty",
                FieldTypes.STRING,
                difficultyOptions,
                null
            ),
            SearchCriteria(
                "text",
                FieldTypes.FREE_TEXT,
                null,
                null)
        )

    override fun getDataProvider(): DataProvider = SegmentDaoImpl(AdvancedSearchApp.instance.db?.segmentDao())

    override fun getThreadSpec(): ThreadSpec = AdvancedSearchApp.instance.getThreadSpec()

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed();
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNewIntent(intent: Intent?) = intent?.let {
            (searchResultView as SearchResultView).passIntent(it)
        } ?: Unit

}