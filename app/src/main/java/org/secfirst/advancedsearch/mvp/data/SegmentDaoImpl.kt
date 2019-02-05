package org.secfirst.advancedsearch.mvp.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import io.reactivex.Flowable
import org.secfirst.advancedsearch.interfaces.DataProvider
import org.secfirst.advancedsearch.models.SearchResult

class SegmentDaoImpl(val segmentDao: SegmentDao?): DataProvider {

    override fun findByCriteria(text: String, vararg additional: String): Flowable<List<SearchResult>> {
        return segmentDao?.
            findByCriteria(
                text,
                additional.getOrElse(1) {""},
                additional.getOrElse(0) {""}
            )?.
            map { it.map { segm ->
                SearchResult(
                    "${segm.category.title} - ${segm.difficulty.name}",
                    "<h1>${segm.title}</h1>\n ${segm.text}"
                ) { c: Context ->  c.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("advancedsearch://details/${segm.id}"))) }
            }
            } ?:
        Flowable.just(listOf())
    }

}