package org.secfirst.advancedsearch.mvp.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
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
    fun insertAll(vararg category: Category) : List<Long>

    @Delete
    fun delete(category: Category) : Int
}