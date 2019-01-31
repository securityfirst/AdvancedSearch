package org.secfirst.advancedsearch.util.mvp

interface ThreadSpec {
    fun bg(function: () -> Unit)
    fun ui(function: () -> Unit)
}