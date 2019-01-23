package org.secfirst.advancedsearch.mvp.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "difficulty")
data class Difficulty (
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "did") var id: String,
    @ColumnInfo(name = "dname") var name: String?
    ) {
    companion object {
        val None: Difficulty = Difficulty("", null)
    }
}