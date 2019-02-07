package org.secfirst.advancedsearch

import android.app.Application
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import org.secfirst.advancedsearch.util.mvp.BgUiThreadSpec
import org.secfirst.advancedsearch.util.mvp.ThreadSpec
import org.secfirst.advancedsearch.mvp.data.AppDatabase
import java.util.concurrent.Executors

class AdvancedSearchApp: Application() {

    var db: AppDatabase? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "umbrella.db"
        ).setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .build()
    }

    companion object {
        lateinit var instance: AdvancedSearchApp
            private set
    }

    val threadSpec: ThreadSpec = BgUiThreadSpec(Executors.newFixedThreadPool(10))

}