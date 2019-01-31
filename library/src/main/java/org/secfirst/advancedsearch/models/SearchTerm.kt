package org.secfirst.advancedsearch.models

data class SearchTerm(var text: String, var criteria: List<Pair<String, String>>?) {
    companion object {
        val NONE = SearchTerm("", null)
    }
}