package org.secfirst.advancedsearch.mvp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import org.secfirst.advancedsearch.mvp.models.Category

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    fun getAll(): Flowable<List<Category>>

    @Query("SELECT * FROM category WHERE cid IN (:categoryIds)")
    fun loadAllByIds(categoryIds: IntArray): Flowable<List<Category>>

    @Query("SELECT * FROM category WHERE cname LIKE :name LIMIT 1")
    fun findByName(name: String): Maybe<Category>

    @Insert
    fun insertAll(vararg category: Category) : Completable

    @Delete
    fun delete(category: Category) : Single<Int>
}