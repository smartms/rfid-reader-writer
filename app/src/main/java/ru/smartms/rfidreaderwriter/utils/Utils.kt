package ru.smartms.rfidreaderwriter.utils

import java.text.SimpleDateFormat
import java.util.*

fun getCurrentDateTime(): String {
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR).toString()
    val month = setNull(c.get(Calendar.MONTH) + 1)
    val day = setNull(c.get(Calendar.DAY_OF_MONTH))
    val hour = setNull(c.get(Calendar.HOUR_OF_DAY))
    val minute = setNull(c.get(Calendar.MINUTE))
    val second = setNull(c.get(Calendar.SECOND))
    return "$year-$month-${day}T$hour:$minute:$second"
}

fun convertDateTime(dateTime: String): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("ru")).parse(dateTime)
        SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale("ru")).format(sdf)
    } catch (t: Throwable) {
        dateTime
    }
}

fun setNull(i: Int): String = if (i < 10) "0$i" else i.toString()

fun isValidEmail(target: CharSequence?): Boolean {
    return if (target == null) false else android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
}