package org.secfirst.advancedsearch.util

import android.app.Application
import androidx.room.Room
import org.secfirst.advancedsearch.mvp.ThreadSpec
import org.secfirst.advancedsearch.mvp.data.AppDatabase
import java.util.concurrent.Executors

class Global: Application() {

    var db: AppDatabase? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "umbrella"
        ).build()
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