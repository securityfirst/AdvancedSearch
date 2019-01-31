package org.secfirst.advancedsearch.models

import org.secfirst.advancedsearch.models.FieldTypes

data class SearchCriteria(val name: String, val type: FieldTypes, val values: List<String>?, var searchFor: String?)