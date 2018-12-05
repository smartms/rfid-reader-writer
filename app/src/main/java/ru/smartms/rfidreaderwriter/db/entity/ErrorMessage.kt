package ru.smartms.rfidreaderwriter.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ErrorMessage(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val message: String,
        val dateTime: String
)