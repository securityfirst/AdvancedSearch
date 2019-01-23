package org.secfirst.advancedsearch.mvp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
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
    fun insertAll(vararg difficulty: Difficulty) : Completable

    @Delete
    fun delete(difficulty: Difficulty) : Single<Int>
}