package org.secfirst.advancedsearch

data class SearchCriteria(val name: String, val type: FieldTypes, val values: List<String>?, var searchFor: String?)