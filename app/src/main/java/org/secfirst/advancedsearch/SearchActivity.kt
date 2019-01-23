package org.secfirst.advancedsearch

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import org.secfirst.advancedsearch.mvp.views.SearchResultView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

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