package ru.smartms.rfidreaderwriter.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ScanData {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
    var barcode: String? = null
    var dateTime: String? =  null
    var isRFID: Boolean = true
    var count: Int = 1
}