package com.example.mag.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    BluetoothAdapter bluetoothAdapter;
    Button BluetoothDiscoverable;
    ListView foundDevices;

    public ArrayList<BluetoothDevice> BluetoothDevices = new ArrayList<>();

    public DeviceListAdapter deviceListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnONOFF = (Button) findViewById(R.id.mainBluetoothONOFF);

        bluetoothAdapter = bluetoothAdapter.getDefaultAdapter();
        BluetoothDiscoverable = (Button) findViewById(R.id.mainBluetoothDiscoverableONOFF);
        foundDevices = (ListView) findViewById(R.id.mainDeviceListView);
        BluetoothDevices = new ArrayList<>();

        btnONOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onoffBluetooth();
            }
        });
    }

    public void onoffBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBluetoothIntent);
        }
        if (bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
        }
    }

    public void discoverableClicked(View view) {
        Intent discoverbaleIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverbaleIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 10);
        startActivity(discoverbaleIntent);
    }

    public void btnDiscoverClicked(View view) {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();

            /*checkBTPermissions();*/

            bluetoothAdapter.startDiscovery();


            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(broadcastReceiver, discoverDevicesIntent);
        }

        if (!bluetoothAdapter.isDiscovering()) {

            /*checkBTPermissions();*/

            bluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(broadcastReceiver, discoverDevicesIntent);
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BluetoothDevices.add(device);
                deviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter, BluetoothDevices);
                foundDevices.setAdapter(deviceListAdapter);
            }
        }
    };


}


/*
    Metoden skal bruges hvis man kører en api højere end 23 og man vil discover.

    private void checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }
    }
}
*/
