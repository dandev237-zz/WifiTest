package dandev237.wifitest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    WifiManager wifiManager;
    WifiScanReceiver wifiReceiver;
    String wifiList[];
    List<WifiData> wifiDataList;
    ListView wifiListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiListView = (ListView) findViewById(R.id.wifiListView);
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiReceiver = new WifiScanReceiver();

        wifiManager.startScan();
    }

    @Override
    protected void onResume(){
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    @Override
    protected void onPause(){
        unregisterReceiver(wifiReceiver);
        super.onPause();
    }

    private class WifiScanReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            List<ScanResult> scanResultList = wifiManager.getScanResults();
            wifiList = new String[scanResultList.size()];
            wifiDataList = new ArrayList<>();

            for (ScanResult s : scanResultList) {
                WifiData w = new WifiData(s);
                wifiDataList.add(w);
            }

            for (int i = 0; i < scanResultList.size(); i++) {
                //wifiList[i] = (scanResultList.get(i)).toString();
                wifiList[i] = wifiDataList.get(i).toString();
            }

            ArrayAdapter<String> wifiAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, wifiList);
            wifiListView.setAdapter(wifiAdapter);
        }
    }

}
