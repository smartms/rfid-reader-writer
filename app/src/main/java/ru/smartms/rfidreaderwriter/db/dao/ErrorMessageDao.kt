package ru.smartms.rfidreaderwriter.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.smartms.rfidreaderwriter.db.entity.ErrorMessage

@Dao
interface ErrorMessageDao {

    @Query("SELECT * FROM errormessage")
    fun getAll(): LiveData<List<ErrorMessage>>

    @Query("SELECT * FROM errormessage ORDER BY ID DESC LIMIT 1")
    fun getLast(): LiveData<List<ErrorMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(errorMessage: ErrorMessage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(errorMessages: List<ErrorMessage>)

    @Query("DELETE FROM errormessage")
    fun deleteAll()
}