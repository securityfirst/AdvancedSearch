package org.secfirst.advancedsearch.mvp.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "segment")
data class Segment (
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "text") var text: String,
    @Embedded var category: Category = Category.None,
    @Embedded var difficulty: Difficulty = Difficulty.None

)