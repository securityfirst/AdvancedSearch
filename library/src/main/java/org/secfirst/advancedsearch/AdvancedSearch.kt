package org.secfirst.advancedsearch

import android.app.SearchManager
import android.content.Intent
import org.secfirst.advancedsearch.models.SearchTerm

open class AdvancedSearch {

    companion object {
        fun getSearchTermFromIntent(intent:Intent): SearchTerm {
            if (Intent.ACTION_SEARCH == intent.action) {
                intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                    return SearchTerm(query, null)
                }
            }
            return SearchTerm.NONE
        }

        fun getSearchTermFromCompositeView(filters: List<Pair<String, String>>?): SearchTerm {
            val result = SearchTerm.NONE
            filters?.apply {
                result.criteria = filters?.filterNot { it.first == "text" }
                this.forEach{
                    when(it.first) {
                        "text" -> result.text = it.second
                    }
                }
            }
            return result
        }


    }
}