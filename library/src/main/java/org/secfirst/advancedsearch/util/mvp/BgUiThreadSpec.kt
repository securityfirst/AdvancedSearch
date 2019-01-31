package org.secfirst.advancedsearch.util.mvp

import android.os.Handler
import android.os.Looper
import org.secfirst.advancedsearch.util.mvp.ThreadSpec
import java.util.concurrent.ExecutorService

class BgUiThreadSpec(private val executor: ExecutorService) : ThreadSpec {
    override fun bg(function: () -> Unit) = executor.execute { function() }

    override fun ui(function: () -> Unit) {
        if(Thread.currentThread() !== Looper.getMainLooper().thread) {
            Handler(Looper.getMainLooper()).post({ function() })
        } else {
            function()
        }
    }
}