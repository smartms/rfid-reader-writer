package ru.smartms.rfidreaderwriter.data.local

import javax.inject.Inject
import javax.inject.Singleton
import android.content.SharedPreferences

@Singleton
class SharedPrefRepository @Inject constructor(private val sharedPreferences: SharedPreferences) {

    companion object {
        const val PURCHASE_TOKEN = "PurchaseToken"
        const val DB_NAME = "dbName"
        const val URL = "url"
        const val SCAN_ONE_BY_ONE = "scan_one_by_one_cbx"
        const val IS_LICENSE_ACTIVE = "IsLicenseActive"
        const val EMAIL = "Email"
        const val PASSWORD = "Password"
    }

    fun getPortPath(): String = sharedPreferences.getString("portPath", "/dev/ttyS2")
            ?: "/dev/ttyS2"
}