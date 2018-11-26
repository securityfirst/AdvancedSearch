package org.secfirst.advancedsearch

import android.app.SearchManager
import android.content.Intent

open class AdvancedSearch {

    companion object {
        fun getQueryFromSearchIntent(intent:Intent):String? {
            if (Intent.ACTION_SEARCH == intent.action) {
                intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                    return query
                }
            }
            return null
        }
    }
}