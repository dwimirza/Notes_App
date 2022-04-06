package com.nanda.notesapp.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

// anotasi entity untuk menandakan bahwa sebuah data class dijadikan table database
@Entity(tableName = "notes_table")
@Parcelize
data class Notes(
    // untuk id dalam tabel supaya tidak duplikat
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var priority: Priority,
    var description: String,
    var date: String,
) : Parcelable