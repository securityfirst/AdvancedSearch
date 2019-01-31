package org.secfirst.advancedsearch.mvp.data

import io.reactivex.Flowable
import org.secfirst.advancedsearch.interfaces.DataProvider
import org.secfirst.advancedsearch.models.SearchResult

class SegmentDaoImpl(val segmentDao: SegmentDao?): DataProvider {

    override fun findByCriteria(text: String, vararg additional: String): Flowable<List<SearchResult>> {
        val (category, difficulty) = additional.slice(IntRange(0,1))
        return segmentDao?.findByCriteria(text, category, difficulty)?.map { it.map { SearchResult(it.title, it.text, "deeplink") } } ?: Flowable.just(
            listOf())
    }

}