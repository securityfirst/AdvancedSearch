package org.secfirst.advancedsearch.mvp

class SameThreadSpec : ThreadSpec {
    override fun bg(function: () -> Unit) = function()
    override fun ui(function: () -> Unit) = function()
}