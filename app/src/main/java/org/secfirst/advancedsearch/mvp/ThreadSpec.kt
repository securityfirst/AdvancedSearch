package org.secfirst.advancedsearch.mvp

interface ThreadSpec {
    fun bg(function: () -> Unit)
    fun ui(function: () -> Unit)
}