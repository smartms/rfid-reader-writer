package ru.smartms.rfidreaderwriter.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.smartms.rfidreaderwriter.App
import ru.smartms.rfidreaderwriter.data.local.ScanDataRepository
import ru.smartms.rfidreaderwriter.db.entity.ScanData
import javax.inject.Inject

class MainViewModel : ViewModel() {

    init {
        App.component.inject(this)
    }

    @Inject
    lateinit var scanDataRepository: ScanDataRepository

    fun getAllScanData(): LiveData<List<ScanData>> = scanDataRepository.getScanData()
    fun deleteScanData(barcode: String?) {
        GlobalScope.launch {
            scanDataRepository.deleteScanData(barcode)
        }
    }
}
