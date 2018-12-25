package ru.smartms.rfidreaderwriter

import android.app.Application
import androidx.room.Room
import com.facebook.stetho.Stetho
import com.generalscan.bluetooth.BluetoothConnect
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.smartms.rfidreaderwriter.db.AppDatabase

import ru.smartms.rfidreaderwriter.di.*

class App : Application() {
    companion object {
        var db: AppDatabase? = null
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        App.db = Room.databaseBuilder(this, AppDatabase::class.java, "rfid-rw-db").build()
        component = buildComponent()
        BluetoothConnect.BindService(this)
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

        // Create an InitializerBuilder
        val initializerBuilder = Stetho.newInitializerBuilder(this)

        // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
            Stetho.defaultInspectorModulesProvider(this)
        )

        // Enable command line interface
        initializerBuilder.enableDumpapp(
            Stetho.defaultDumperPluginsProvider(this)
        )

        // Use the InitializerBuilder to generate an Initializer
        val initializer = initializerBuilder.build()

        // Initialize Stetho with the Initializer
        Stetho.initialize(initializer)
    }

    private fun buildComponent(): AppComponent {
        return DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .netModule(NetModule())
                .repositoryModule(RepositoryModule())
                .contextModule(ContextModule(this))
                .build()
    }
}