package ru.smartms.rfidreaderwriter.ui

import androidx.lifecycle.ViewModel;
import com.magicrf.uhfreaderlib.reader.Tools
import com.magicrf.uhfreaderlib.reader.UhfReader
import ru.smartms.rfidreaderwriter.uhfreader.UhfReaderDevice
import androidx.lifecycle.MutableLiveData


class ScanDataDetailsViewModel : ViewModel() {

    companion object {
        //EPC
        const val MEM_BANK = 2
        const val ADDR = 2
        const val LENGTH = 6
    }

    val epcHex: MutableLiveData<String> = MutableLiveData()
    val epc: MutableLiveData<String> = MutableLiveData()

    val reader: UhfReader by lazy {
        UhfReader.getInstance()
    }

    val readerDevice: UhfReaderDevice by lazy {
        UhfReaderDevice.getInstance()
    }

    fun setEPC(epc: String) {
        reader.selectEpc(Tools.HexString2Bytes(epc));
    }

    fun readEpc(password: String) {
        val accessPassword = Tools.HexString2Bytes(password)
        if (accessPassword.size != 4) {
            return
        }
        val data = reader.readFrom6C(MEM_BANK, ADDR, LENGTH, accessPassword)
        if (data != null && data.size > 1) {
            val dataInt = Tools.bytesToInt(data)
            epcHex.postValue(dataInt.toString())
        } else {
//            //				Toast.makeText(getApplicationContext(), "������ʧ��", Toast.LENGTH_SHORT).show();
//            if (data != null) {
//                editReadData.append("������ʧ�ܣ������룺" + (data[0] and 0xff) + "\n")
//                return
//            }
//            editReadData.append("������ʧ�ܣ�����Ϊ��" + "\n")
        }
    }
}
