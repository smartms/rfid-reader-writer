package ru.smartms.rfidreaderwriter.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.magicrf.uhfreaderlib.reader.Tools
import com.magicrf.uhfreaderlib.reader.UhfReader
import ru.smartms.rfidreaderwriter.uhfreader.UhfReaderDevice
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.smartms.rfidreaderwriter.App
import ru.smartms.rfidreaderwriter.R
import ru.smartms.rfidreaderwriter.data.local.ErrorMessageRepository
import ru.smartms.rfidreaderwriter.data.local.ScanDataRepository
import ru.smartms.rfidreaderwriter.db.entity.ErrorMessage
import ru.smartms.rfidreaderwriter.db.entity.ScanData
import java.nio.charset.Charset
import javax.inject.Inject

class ScanDataDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val reader: UhfReader?
    private val readerDevice: UhfReaderDevice?

    init {
        App.component.inject(this)
        reader = UhfReader.getInstance();
        readerDevice = UhfReaderDevice.getInstance();
    }

    companion object {
        //EPC
        const val MEM_BANK = 1
        const val ADDR = 2
        const val LENGTH = 6
    }

    val epcHex: MutableLiveData<String> = MutableLiveData()
    val epc: MutableLiveData<String> = MutableLiveData()

    @Inject
    lateinit var scanDataRepository: ScanDataRepository

    @Inject
    lateinit var errorMessageRepository: ErrorMessageRepository

    fun getErrorMessage(): LiveData<List<ErrorMessage>>? {
        return errorMessageRepository.getErrorMessage()
    }

    fun deleteErrorMessage() {
        GlobalScope.launch {
            errorMessageRepository.deleteAll()
        }
    }

    fun getScanData(): LiveData<List<ScanData>>? = scanDataRepository.getScanData()

    fun deleteScanData() = scanDataRepository.deleteAll()

    fun setEPC(epc: String) {
        reader?.selectEpc(Tools.HexString2Bytes(epc));
    }

    fun readEpc(password: String) {
        val accessPassword = Tools.HexString2Bytes(password)
        if (accessPassword.size != 4) {
            return
        }
        val data = reader?.readFrom6C(MEM_BANK, ADDR, LENGTH, accessPassword)
        if (data != null && data.size > 1) {
            epcHex.postValue(Tools.Bytes2HexString(data, data.size))
            epc.postValue(String(data))
        } else {
            var errorMessage = getApplication<App>().getString(R.string.read_failure)
            if (data != null) {
                errorMessage = "${getApplication<App>().getString(R.string.read_failure)}: ${data[0].toInt()}"
            }
            GlobalScope.launch {
                errorMessageRepository.insertMessage(errorMessage)
            }
        }
    }

    fun writeEpc(epcWrite: String, password: String) {
        val accessPassword = Tools.HexString2Bytes(password)
        if (accessPassword.size != 4) {
            return
        }
        val epcTemp: String
        val builder = StringBuilder()
        epcTemp = if (epcWrite.length != 12) {
            builder.append(epcWrite)
            for (i in 1..12 - epcWrite.length) {
                builder.append(" ")
            }
            builder.toString()
        } else {
            epcWrite
        }
        val dataBytes = epcTemp.toByteArray(Charset.defaultCharset())
        val writeFlag = reader?.writeTo6C(accessPassword, MEM_BANK, ADDR, LENGTH, dataBytes)
        val errorMessage: String
        if (writeFlag == true) {
            errorMessage = getApplication<App>().getString(R.string.write_success)
            reader?.selectEpc(dataBytes)
            epcHex.postValue(Tools.Bytes2HexString(dataBytes, dataBytes.size))
            epc.postValue(epcTemp)
        } else {
            errorMessage = getApplication<App>().getString(R.string.write_failure)
        }
        GlobalScope.launch {
            errorMessageRepository.insertMessage(errorMessage)
        }
    }
}
