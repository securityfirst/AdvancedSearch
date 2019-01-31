package org.secfirst.advancedsearch.interfaces

import org.secfirst.advancedsearch.models.SearchCriteria
import org.secfirst.advancedsearch.util.mvp.ThreadSpec

interface AdvancedSearchPresenter {
    fun getCriteria() : List<SearchCriteria>
    fun getDataProvider() : DataProvider
    fun getThreadSpec() : ThreadSpec
}