package org.secfirst.advancedsearch.interfaces

import io.reactivex.Flowable
import org.secfirst.advancedsearch.models.SearchResult

interface DataProvider {
    fun findByCriteria(text: String, vararg additional: Pair<String, List<String>>): Flowable<List<SearchResult>>
}