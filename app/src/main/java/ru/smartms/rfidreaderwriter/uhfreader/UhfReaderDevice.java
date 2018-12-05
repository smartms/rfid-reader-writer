package ru.smartms.rfidreaderwriter.uhfreader;

public class UhfReaderDevice {

    private static UhfReaderDevice readerDevice;
    private static SerialPort devPower;

    public static UhfReaderDevice getInstance() {
        if (devPower == null) {
            try {
                devPower = new SerialPort();
            } catch (Exception e) {
                return null;
            }
        }

        if (readerDevice == null) {
            readerDevice = new UhfReaderDevice();
        }
        return readerDevice;
    }

    public void powerOff() {
        if (devPower != null) {
            devPower = null;
        }
    }
}
