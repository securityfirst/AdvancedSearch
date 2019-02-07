package org.secfirst.advancedsearch.mvp.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull


@Entity(tableName = "category")
data class Category(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "cid") var id: String,
    @ColumnInfo(name = "cname") var title: String,
    @ColumnInfo(name = "cparent") var parent: String
) {
    companion object {
        val None: Category = Category("", "", "")
    }
}