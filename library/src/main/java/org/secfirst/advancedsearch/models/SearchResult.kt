package org.secfirst.advancedsearch.models

import android.content.Context

data class SearchResult(val title: String, val summary: String, val listener: (c: Context) -> Unit = {})