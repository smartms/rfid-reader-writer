package ru.smartms.rfidreaderwriter.data.local

import javax.inject.Inject
import javax.inject.Singleton
import androidx.lifecycle.LiveData
import ru.smartms.rfidreaderwriter.db.dao.ErrorMessageDao
import ru.smartms.rfidreaderwriter.db.entity.ErrorMessage
import ru.smartms.rfidreaderwriter.utils.getCurrentDateTime

@Singleton
class ErrorMessageRepository @Inject constructor(private val errorMessageDao: ErrorMessageDao) {

    fun getErrorMessage(): LiveData<List<ErrorMessage>> {
        return errorMessageDao.getAll()
    }

    fun deleteAll() = errorMessageDao.deleteAll()


    fun insert(errorMessage: ErrorMessage) = errorMessageDao.insert(errorMessage)


    fun insertMessage(message: String) {
        val errorMessage = ErrorMessage(0, message, getCurrentDateTime())
        insert(errorMessage)
    }
}