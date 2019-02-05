package org.secfirst.advancedsearch.mvp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import org.secfirst.advancedsearch.mvp.models.Segment

@Dao
interface SegmentDao {
    @Query("SELECT * FROM segment")
    fun getAll(): Flowable<List<Segment>>

    @Query("SELECT * FROM segment WHERE id IN (:segmentIds) ")
    fun loadAllByIds(segmentIds: List<String>): Flowable<List<Segment>>

    @Query("SELECT * FROM segment WHERE id = :segmentId LIMIT 1")
    fun loadById(segmentId: String): Flowable<Segment>

    @Query("SELECT * FROM segment WHERE title LIKE '%' || :title || '%' OR " +
            "text LIKE '%' || :text || '%'")
    fun findByTitleOrText(title: String, text: String): Flowable<List<Segment>>

    @Query("SELECT * FROM segment WHERE (title LIKE '%' || :title || '%' OR " +
            "text LIKE '%' || :title || '%') AND (LOWER(cname) LIKE LOWER('%' || :category || '%')) AND (dname != :difficulty)")
    fun findByCriteria(title: String, category: String, difficulty: String): Flowable<List<Segment>>

    @Insert
    fun insertAll(vararg segment: Segment) : Completable

    @Delete
    fun delete(segment: Segment) : Single<Int>
}