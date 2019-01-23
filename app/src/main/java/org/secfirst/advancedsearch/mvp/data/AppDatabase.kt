package org.secfirst.advancedsearch.mvp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import org.secfirst.advancedsearch.mvp.models.Category
import org.secfirst.advancedsearch.mvp.models.Difficulty
import org.secfirst.advancedsearch.mvp.models.Segment

@Database(entities = arrayOf(
    Segment::class,
    Difficulty::class,
    Category::class),
        version = 1,
    exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun segmentDao(): SegmentDao
    abstract fun categoryDao(): CategoryDao
    abstract fun difficultyDao(): DifficultyDao
}