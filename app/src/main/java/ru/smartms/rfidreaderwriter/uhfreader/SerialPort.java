package ru.smartms.rfidreaderwriter.uhfreader;

import com.magicrf.uhfreaderlib.readerInterface.DevicePowerInterface;

class SerialPort implements DevicePowerInterface {

    public void uhfPowerOn() {
        psampoweron();
    }

    public void uhfPowerOff() {
        psampoweroff();
    }

    static {
        System.loadLibrary("devapi");
        System.loadLibrary("uhf");
    }

    @SuppressWarnings("JniMissingFunction")
    public native void psampoweron();

    @SuppressWarnings("JniMissingFunction")
    public native void psampoweroff();
}
