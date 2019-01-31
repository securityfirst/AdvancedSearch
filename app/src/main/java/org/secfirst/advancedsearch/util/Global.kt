package org.secfirst.advancedsearch.util

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.facebook.stetho.Stetho
import org.secfirst.advancedsearch.util.mvp.BgUiThreadSpec
import org.secfirst.advancedsearch.util.mvp.ThreadSpec
import org.secfirst.advancedsearch.mvp.data.AppDatabase
import java.util.concurrent.Executors

class Global: Application() {

    var db: AppDatabase? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "umbrella.db"
        ).setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .build()
        Stetho.initializeWithDefaults(this@Global)
    }

    companion object {
        lateinit var instance: Global
            private set
    }

    private var threadSpec: ThreadSpec? = null

    fun getThreadSpec(): ThreadSpec {
        if (threadSpec == null) {
            threadSpec = BgUiThreadSpec(Executors.newFixedThreadPool(10))
        }
        return threadSpec as ThreadSpec
    }

}