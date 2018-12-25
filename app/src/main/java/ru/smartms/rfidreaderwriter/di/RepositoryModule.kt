package ru.smartms.rfidreaderwriter.di

import dagger.Module
import dagger.Provides
import ru.smartms.rfidreaderwriter.App
import ru.smartms.rfidreaderwriter.data.local.ErrorMessageRepository
import ru.smartms.rfidreaderwriter.data.local.ScanDataRepository
import ru.smartms.rfidreaderwriter.db.dao.ErrorMessageDao
import ru.smartms.rfidreaderwriter.db.dao.ScanDataDao
import javax.inject.Singleton

@Module
class RepositoryModule {
//Repository
    @Provides
    @Singleton
    fun provideErrorMessageRepository(errorMessageDao: ErrorMessageDao): ErrorMessageRepository {
        return ErrorMessageRepository(errorMessageDao)
    }

    @Provides
    @Singleton
    fun provideScanDataRepository(scanDataDao: ScanDataDao): ScanDataRepository {
        return ScanDataRepository(scanDataDao)
    }

//    Dao
    @Provides
    @Singleton
    fun provideScanDataDao(): ScanDataDao {
        return App.db?.scanDataDao()!!
    }

    @Provides
    @Singleton
    fun provideErrorMessageDao(): ErrorMessageDao {
        return App.db?.errorMessageDao()!!
    }
}