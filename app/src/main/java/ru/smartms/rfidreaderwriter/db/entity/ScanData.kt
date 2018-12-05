package ru.smartms.rfidreaderwriter.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ScanData(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val barcode: String,
        val dateTime: String
)