package org.secfirst.advancedsearch.util.mvp

import org.secfirst.advancedsearch.util.mvp.ThreadSpec

class SameThreadSpec : ThreadSpec {
    override fun bg(function: () -> Unit) = function()
    override fun ui(function: () -> Unit) = function()
}