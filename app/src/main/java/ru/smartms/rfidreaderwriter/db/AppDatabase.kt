package ru.smartms.rfidreaderwriter.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.smartms.rfidreaderwriter.db.dao.ErrorMessageDao
import ru.smartms.rfidreaderwriter.db.dao.ScanDataDao
import ru.smartms.rfidreaderwriter.db.entity.ErrorMessage
import ru.smartms.rfidreaderwriter.db.entity.ScanData

@Database(entities = [
    ScanData::class,
    ErrorMessage::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scanDataDao(): ScanDataDao
    abstract fun errorMessageDao(): ErrorMessageDao
}