package ru.smartms.rfidreaderwriter.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.smartms.rfidreaderwriter.db.entity.ScanData

@Dao
interface ScanDataDao {

    @Query("SELECT 0 as id, COUNT(barcode) as count, barcode FROM scandata GROUP BY barcode")
    fun getAll(): LiveData<List<ScanData>>

    @Query("SELECT * FROM scandata ORDER BY ID DESC LIMIT 1")
    fun getLast(): LiveData<List<ScanData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(scandata: ScanData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<ScanData>)

    @Query("DELETE FROM scandata")
    fun deleteAll()
}