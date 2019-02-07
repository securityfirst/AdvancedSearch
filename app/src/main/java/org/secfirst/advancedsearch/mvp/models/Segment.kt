package org.secfirst.advancedsearch.mvp.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull


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