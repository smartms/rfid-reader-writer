package ru.smartms.rfidreaderwriter

import android.app.Application
import com.generalscan.bluetooth.BluetoothConnect
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.smartms.rfidreaderwriter.db.AppDatabase

import ru.smartms.smallsmarty.di.*

class App : Application() {
    companion object {
        var db: AppDatabase? = null
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
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