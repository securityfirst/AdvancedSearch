package org.secfirst.advancedsearch

import android.app.SearchManager
import android.content.Intent
import org.secfirst.advancedsearch.models.SearchTerm
import java.util.logging.Logger

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

        fun getSearchTermFromCompositeView(filters: HashMap<String, String>): SearchTerm {
            if (filters.size<1) Logger.getLogger("ddd").info("Map empty")
            filters.forEach {
                Logger.getLogger("ddd").info("${it.key} ${it.value}")
            }
            val result = SearchTerm.NONE
            result.text = filters["text"].orEmpty()
            filters.remove("text")
            result.criteria = filters.toList()
            return result
        }


    }
}