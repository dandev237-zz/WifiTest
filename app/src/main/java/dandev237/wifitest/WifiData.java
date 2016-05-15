package dandev237.wifitest;

import android.net.wifi.ScanResult;
import android.os.SystemClock;

import java.util.Date;

/**
 * Clase para modelar toda la información de una red wifi (Objeto ScanResult)
 *
 * Autor: Daniel Castro García
 * Email: dandev237@gmail.com
 * Fecha: 14/05/2016
 */
public class WifiData {

    String bssid, ssid, capabilities;
    int centerFreq0, centerFreq1, channelWidth, frequency, level;
    Date timestamp;
    String distance;

    public WifiData(ScanResult scan){
        bssid = scan.BSSID;
        ssid = scan.SSID;
        capabilities = scan.capabilities;
        //centerFreq0 = scan.centerFreq0; //Centro de frecuencia del primer segmento
        //centerFreq1 = scan.centerFreq1; //Centro de frecuencia del segundo segmento
        //channelWidth = scan.channelWidth;
        frequency = scan.frequency;
        level = scan.level;

        long scanResultTimestampInMillis = System.currentTimeMillis() - SystemClock.elapsedRealtime() + (scan.timestamp / 1000);
        timestamp = new Date(scanResultTimestampInMillis);

        /*DecimalFormat df = new DecimalFormat("#.##");
        distance = df.format(trilaterate(level, frequency));*/
    }


    /*
     * Trilateración para determinar la distancia al AP/dispositivo a partir del
     * nivel de la señal.
     *
     * Para esto se ha empleado la fórmula para el cálculo del Free-space path loss (FSPL)
     * Referencia:
     *
     * @param level Nivel de la señal, en dB.
     * @param freq  Frecuencia de la señal, en MHz
     */

    /*
    public double trilaterate(double level, double freq){
        double exp = (27.55 - (20 * Math.log10(freq)) + Math.abs(level)) / 20.0;
        return Math.pow(10.0, exp);
    }
    */

    @Override
    public String toString(){
        return "BSSID: " + bssid + ", SSID: " + ssid + ", Capabilities: " + capabilities + /*", centerFreq0: " + centerFreq0 + ", centerFreq1: " + centerFreq1 +
                ", Channel Width: " + channelWidth + */", Frequency: " + frequency + " MHz, Signal level: " + level + " dB, Timestamp: " + timestamp /*". Distance: " +
                distance + "."*/;
    }
}
