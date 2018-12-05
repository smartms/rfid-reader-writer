package ru.smartms.rfidreaderwriter.data.local

import javax.inject.Inject
import javax.inject.Singleton
import androidx.lifecycle.LiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.smartms.rfidreaderwriter.db.dao.ScanDataDao
import ru.smartms.rfidreaderwriter.db.entity.ScanData

@Singleton
class ScanDataRepository @Inject constructor(private val scanDataDao: ScanDataDao) {

    fun getScanData(): LiveData<List<ScanData>> = scanDataDao.getAll()

    fun deleteAll() = GlobalScope.launch { scanDataDao.deleteAll() }

    fun insert(scanData: ScanData) = GlobalScope.launch { scanDataDao.insert(scanData) }
}