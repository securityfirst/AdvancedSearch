package org.secfirst.advancedsearch.mvp.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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