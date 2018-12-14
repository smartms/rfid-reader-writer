package ru.smartms.rfidreaderwriter.lifecycle

import android.content.Context
import javax.inject.Inject
import com.magicrf.uhfreaderlib.reader.UhfReader
import android.media.SoundPool
import android.media.AudioManager
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.magicrf.uhfreaderlib.reader.Tools
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.smartms.rfidreaderwriter.App
import ru.smartms.rfidreaderwriter.R
import ru.smartms.rfidreaderwriter.data.local.ScanDataRepository
import ru.smartms.rfidreaderwriter.data.local.SharedPrefRepository
import ru.smartms.rfidreaderwriter.db.entity.ScanData
import ru.smartms.rfidreaderwriter.uhfreader.UhfReaderDevice
import ru.smartms.rfidreaderwriter.utils.getCurrentDateTime
import java.io.File
import java.io.FileWriter

class RFIDScannerLifecycle : LifecycleObserver {

    companion object {
        const val POWER_VALUE = 26
    }

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var sharedPrefRepository: SharedPrefRepository

    @Inject
    lateinit var scanDataRepository: ScanDataRepository

    var sp: SoundPool? = null
    private lateinit var serialPortPath: String
    var reader: UhfReader? = null
    private val readerDevice: UhfReaderDevice? by lazy { UhfReaderDevice.getInstance() }
    var runFlag: Boolean = false
    private val epcStringList = ArrayList<String>()
    private var jobStartScan: Job = Job()
    var startFlag = false

    init {
        App.component.inject(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun connect() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun disconnect() {
        runFlag = false
//        reader?.close()
        readerDevice?.powerOff()
    }

    private fun setupUHFReader() {
        try {
            if (reader == null) {
                reader = UhfReader.getInstance()
            }
            if (sp == null) {
                @Suppress("DEPRECATION")
                sp = SoundPool(1, AudioManager.STREAM_MUSIC, 1)
            }
            sp?.load(context, R.raw.msg, 1)
            serialPortPath = sharedPrefRepository.getPortPath()
            UhfReader.setPortPath(serialPortPath)
            if (reader != null) {
                reader?.setOutputPower(POWER_VALUE)
            }
        } catch (e: Exception) {
            Log.e("setupUHFReader", e.localizedMessage)
        }
    }

    fun startInventory(isChecked: Boolean) {
        startFlag = isChecked
    }

    fun startScan() {
        if (reader == null) {
            return
        }
        jobStartScan.cancel()
        jobStartScan = Job()
        GlobalScope.launch(jobStartScan) {
            launch {
                while (true) {
                    delay(10000)
                    epcStringList.clear()
                }
            }
            try {
                var epcList: ArrayList<ByteArray>
                var epcStr: String
                while (runFlag) {
                    if (startFlag) {
                        epcList = reader?.inventoryRealTime() as ArrayList<ByteArray>
                        var isScanned = false
                        if (!epcList.isEmpty()) {
                            sp?.play(1, 1F, 1F, 0, 0, 1F)
                            epcList.forEach { epc ->
                                epcStr = Tools.Bytes2HexString(epc, epc.size)
                                val epcStringListTemp = epcStringList
                                epcStringListTemp.forEach {
                                    if (it == epcStr) {
                                        isScanned = true
                                    }
                                }
                                if (!isScanned) {
                                    epcStringList.add(epcStr)
                                    val scanData = ScanData()
                                    scanData.barcode = epcStr
                                    scanData.dateTime = getCurrentDateTime()
                                    scanDataRepository.insert(scanData)
                                }
                            }
                        }
                    }
                }
            } catch (t: Throwable) {
                Log.e("startScan", t.localizedMessage)
            }
        }
    }

    fun onOffRFID(isPowerOn: Boolean) {
        setupUHFReader()
        if (reader != null) {
            sp?.play(1, 1F, 1F, 0, 0, 1F)
            if (isPowerOn) {
                try {
                    setUHF("1")
                    runFlag = true
                    startScan()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                try {
                    setUHF("0")
                    runFlag = false
                    jobStartScan.cancel()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setUHF(p: String) {
        val localFileWriterOff = FileWriter(
            File(
                "/proc/gpiocontrol/set_uhf"
            )
        )
        localFileWriterOff.write(p)
        localFileWriterOff.close()
    }
}