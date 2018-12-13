package org.secfirst.advancedsearch.util

import android.app.Application
import org.secfirst.advancedsearch.mvp.ThreadSpec
import java.util.concurrent.Executors

class Global: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
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