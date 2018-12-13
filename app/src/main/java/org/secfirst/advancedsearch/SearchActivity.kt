package org.secfirst.advancedsearch

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_search.*
import org.secfirst.advancedsearch.mvp.views.SearchResultView
import java.util.logging.Logger

class SearchActivity : AppCompatActivity() {
    val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Verify the action and get the query
        AdvancedSearch.getQueryFromSearchIntent(intent)?.let {
            Logger.getLogger(TAG).info("Searchinng for ${it}")
            (searchResultView as SearchResultView).passIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        Logger.getLogger(TAG).info("on new intent")
    }

}