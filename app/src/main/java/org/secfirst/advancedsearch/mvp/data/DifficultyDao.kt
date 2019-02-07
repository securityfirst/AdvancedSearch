package org.secfirst.advancedsearch.mvp.data

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Maybe
import org.secfirst.advancedsearch.mvp.models.Difficulty

@Dao
interface DifficultyDao {
    @Query("SELECT * FROM difficulty")
    fun getAll(): Flowable<List<Difficulty>>

    @Query("SELECT * FROM difficulty WHERE did IN (:difficultyIds)")
    fun loadAllByIds(difficultyIds: IntArray): Flowable<List<Difficulty>>

    @Query("SELECT * FROM difficulty WHERE dname LIKE :name LIMIT 1")
    fun findByName(name: String): Maybe<Difficulty>

    @Insert
    fun insertAll(vararg difficulty: Difficulty) : List<Long>

    @Delete
    fun delete(difficulty: Difficulty) : Int
}