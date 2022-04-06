package com.nanda.notesapp.data.room

import androidx.room.TypeConverter
import com.nanda.notesapp.data.entity.Priority

class Converter {

    // untuk convert dari priority enum class ke string
    // fungsi ini dipanggil ketika get sebuah database
    @TypeConverter
    fun fromPriority(priority: Priority): String{
        return priority.name
    }


    // ini untuk convert sebuah string kedalam enum class priority
    // fungsi ini dipanggil ketika add dan update sebuah data ke database
    @TypeConverter
    fun toPriority(priority: String): Priority{
        return Priority.valueOf(priority)
    }
}