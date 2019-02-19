package org.secfirst.advancedsearch.mvp.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import io.reactivex.Flowable
import org.secfirst.advancedsearch.interfaces.DataProvider
import org.secfirst.advancedsearch.models.SearchResult
import java.util.logging.Logger

class SegmentDaoImpl(val segmentDao: SegmentDao?): DataProvider {

    override fun findByCriteria(text: String, vararg additional: Pair<String, List<String>>): Flowable<List<SearchResult>> {
        Logger.getLogger("aaa").info("pew pew1 ${additional.joinToString()}")
        val difficulty: String = additional.find { it.first == "difficulty" }?.second?.getOrNull(0) ?: ""
        val categories: String = additional.filter { it.first == "category" }.flatMap {
            it.second
        }.firstOrNull().orEmpty()
        return segmentDao?.
            findByCriteria(
                text,
                categories,
                difficulty
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