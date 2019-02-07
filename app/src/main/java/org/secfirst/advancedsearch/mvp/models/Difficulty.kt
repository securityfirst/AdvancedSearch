package org.secfirst.advancedsearch.mvp.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull


@Entity(tableName = "difficulty")
data class Difficulty (
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "did") var id: String,
    @ColumnInfo(name = "dname") var name: String
    ) {
    companion object {
        val None: Difficulty = Difficulty("", "")
    }
}