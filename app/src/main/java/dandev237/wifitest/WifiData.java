package dandev237.wifitest;

import android.net.wifi.ScanResult;
import android.os.SystemClock;

import java.util.Date;

/**
 * Clase para modelar toda la información de una red wifi (Objeto ScanResult), además de atributos adicionales
 * empleados en otras clases del proyecto.
 *
 * Autor: Daniel Castro García
 * Email: dandev237@gmail.com
 * Fecha: 14/05/2016
 */
public class WifiData {

    private String bssid, ssid, capabilities;
    private int frequency, level;
    private double latitude, longitude;
    private Date timestamp;

    public WifiData(ScanResult scan){
        bssid = scan.BSSID;
        ssid = scan.SSID;
        capabilities = scan.capabilities;
        frequency = scan.frequency;
        level = scan.level;

        long scanResultTimestampInMillis = System.currentTimeMillis() - SystemClock.elapsedRealtime() + (scan.timestamp / 1000);
        timestamp = new Date(scanResultTimestampInMillis);
    }

    @Override
    public String toString(){
        return "BSSID: " + bssid + ", SSID: " + ssid + ", Capabilities: " + capabilities + ", Frequency: "
                + frequency + " MHz, Signal level: " + level + " dB, Timestamp: " + timestamp
                + ", Position: (" + latitude + "º, " + longitude + "º)";
    }

    //Getters

    public String getBssid() {
        return bssid;
    }

    public int getLevel() {
        return level;
    }

    //Setters

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
