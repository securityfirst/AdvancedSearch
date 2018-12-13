package org.secfirst.advancedsearch.util

import android.app.Application
import org.secfirst.advancedsearch.mvp.ThreadSpec
import java.util.concurrent.Executors

class Global: Application() {

    companion object {
        lateinit var instance: Global
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    private var threadSpec: ThreadSpec? = null

    fun getThreadSpec(): ThreadSpec {
        if (threadSpec == null) {
            threadSpec = BgUiThreadSpec(Executors.newFixedThreadPool(10))
        }
        return threadSpec as ThreadSpec
    }

}