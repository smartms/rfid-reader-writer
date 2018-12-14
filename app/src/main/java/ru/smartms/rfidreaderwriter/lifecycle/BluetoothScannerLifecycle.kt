package ru.smartms.rfidreaderwriter.lifecycle

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.device.ScanDevice
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.generalscan.SendConstant
import com.generalscan.bluetooth.BluetoothConnect
import kotlinx.coroutines.*
import ru.smartms.rfidreaderwriter.App
import ru.smartms.rfidreaderwriter.data.local.ScanDataRepository
import ru.smartms.rfidreaderwriter.db.entity.ScanData
import ru.smartms.rfidreaderwriter.utils.getCurrentDateTime
import java.lang.Exception
import javax.inject.Inject


class BluetoothScannerLifecycle : LifecycleObserver {

    private var sm: ScanDevice? = null
    private val barcodeBuilder = StringBuilder()
    private val scanAction = "scan.rcv.message"
    private var scanBroadcast: ScanBroadcast? = null

    @Inject
    lateinit var scanDataRepository: ScanDataRepository

    @Inject
    lateinit var context: Context

    init {
        App.component.inject(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun connect() {
        BluetoothConnect.BindService(context)
        GlobalScope.launch {
            while (true) {
                try {
                    delay(500L)
                    if (!BluetoothConnect.isConnected()) {
                        BluetoothConnect.Connect()
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                    try {
                        if (!BluetoothConnect.isConnected()) {
                            delay(3000L)
                            BluetoothConnect.Connect()
                        }
                    } catch (e1: Throwable) {
                        e1.printStackTrace()
                    }
                }
            }
        }
        try {
            sm = ScanDevice()
            sm?.outScanMode = 0 //Сканирование в рессивер
            sm?.openScan()
        } catch (e: Throwable) {

        }
        setBroadcast()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun disconnect() {
        if (scanBroadcast != null) {
            context.unregisterReceiver(scanBroadcast)
        }
        sm?.closeScan()
        try {
            BluetoothConnect.UnBindService(context)
        } catch (e: Exception) {
            Log.e("OnLifecycleDisconnect", e.localizedMessage)
        }
    }


    private fun setBroadcast() {
        scanBroadcast = ScanBroadcast()
        val filter = IntentFilter()
        filter.addAction(SendConstant.GetDataAction)
        filter.addAction(scanAction)
        context.registerReceiver(scanBroadcast, filter)
    }

    inner class ScanBroadcast : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                SendConstant.GetDataAction -> {
                    val data = intent.getStringExtra(SendConstant.GetData)
                    barcodeBuilder.append(data)
                    if (data == "\r") {
                        var barcode = barcodeBuilder.toString().replace("\n", "")
                        barcode = barcode.replace("\r", "")
                        Toast.makeText(context, barcode, Toast.LENGTH_SHORT).show()
                        val scanData = ScanData()
                        scanData.barcode = barcode
                        scanData.dateTime = getCurrentDateTime()
                        scanData.isRFID = false
                        scanDataRepository.insert(scanData)
                        barcodeBuilder.setLength(0)
                    }
                }
                scanAction -> {
                    val barocode = intent.getByteArrayExtra("barocode")
                    val barocodelen = intent.getIntExtra("length", 0)
                    val temp = intent.getByteExtra("barcodeType", 0.toByte())
                    android.util.Log.i("debug", "----codetype--$temp")
                    val scanData = ScanData()
                    scanData.barcode = String(barocode, 0, barocodelen)
                    scanData.dateTime = getCurrentDateTime()
                    scanData.isRFID = false
                    scanDataRepository.insert(scanData)
                }
            }

        }
    }
}