package ru.smartms.smallsmarty.di

import dagger.Component
import ru.smartms.rfidreaderwriter.lifecycle.BluetoothScannerLifecycle
import ru.smartms.rfidreaderwriter.lifecycle.RFIDScannerLifecycle
import javax.inject.Singleton

@Component(modules = [(AppModule::class), (NetModule::class), (RepositoryModule::class), (ContextModule::class)])
@Singleton
interface AppComponent {
    fun inject(bluetoothScannerLifecycle: BluetoothScannerLifecycle)
    fun inject(rfidScannerLifecycle: RFIDScannerLifecycle)
}