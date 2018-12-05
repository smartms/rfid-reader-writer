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
    var jobStartScan: Job = Job()

    init {
        App.component.inject(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun connect() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun disconnect() {
        runFlag = false
        reader?.close()
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

    private fun startScan() {
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
                var isScanned = false
                var epcStr: String
                if (reader != null) {
                    while (runFlag) {
                        epcList = reader?.inventoryRealTime() as ArrayList<ByteArray>
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
                                    val scanData = ScanData(0, epcStr, getCurrentDateTime())
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

    fun onOffRFID(isStartScan: Boolean) {
        setupUHFReader()
        if (reader != null) {
            sp?.play(1, 1F, 1F, 0, 0, 1F)
            if (isStartScan) {
                try {
                    setUHF("1")
                    runFlag = true
                    GlobalScope.launch {
                        startScan()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                try {
                    runFlag = false
                    setUHF("0")
                    jobStartScan.cancel()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setUHF(p: String) {
        val localFileWriterOff = FileWriter(File(
                "/proc/gpiocontrol/set_uhf"))
        localFileWriterOff.write(p)
        localFileWriterOff.close()
    }
}