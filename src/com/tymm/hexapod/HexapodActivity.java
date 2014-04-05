package com.tymm.hexapod;

import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class HexapodActivity extends Activity {
	private ArrayList<DeviceInfo> devices = new ArrayList<DeviceInfo>();
	private CustomArrayAdapter adapter;
	private BluetoothAdapter bluetoothAdapter;
	private ProgressDialog pd;
	private int REQUEST_ENABLE_BT = 1;
	private ConnectThread connect;

	/** Called after trying to connect to a bluetooth device*/
	private Handler bluetooth_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
			if (msg.what == 1) {
				Toast toast = Toast.makeText(getApplicationContext(), "Could not connect", Toast.LENGTH_SHORT);
				toast.show();
			} else {
				Log.i("Hexapod", "Connected to device");
				Toast toast = Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT);
				toast.show();
				// Start Control Activity
				// Intent i = new Intent(HexapodActivity.this, ControlActivity.class);
				// startActivity(i);
			}
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final ListView listview = (ListView) findViewById(R.id.listview);

		adapter = new CustomArrayAdapter(this, R.id.listview, devices);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				final DeviceInfo item = (DeviceInfo) parent.getItemAtPosition(position);
				// Connect to the selected device
				pd = ProgressDialog.show(HexapodActivity.this, "Connecting...", item.getName(), true, false);
				connect = new ConnectThread(item.getBluetoothDevice(), bluetoothAdapter, bluetooth_handler, HexapodActivity.this);
				connect.start();
			}
		});

		adapter.notifyDataSetChanged();

		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter == null) {
			// Device does not support Bluetooth
		}

		if (!bluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}

		// Search through paired devices
		Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
			// Loop through paired devices
			for (BluetoothDevice device : pairedDevices) {
				// Add the name and address to an array adapter to show in a ListView
				DeviceInfo d = new DeviceInfo(device);
				devices.add(d);

				adapter.notifyDataSetChanged();
			}
		}

		// Register BroadcastReceiver for Bluetooth Discovery
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter);
	}

	public void onDestroy() {
		// Don't forget to unregister during onDestroy
		unregisterReceiver(mReceiver);
	}

	public void discoverBluetooth(View view) {
		Toast toast = Toast.makeText(getApplicationContext(), "Discover", Toast.LENGTH_SHORT);
		toast.show();
		// Discovering devices
		bluetoothAdapter.startDiscovery();
	}

	/* Called after the activity asked the user to enable Bluetooth */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == RESULT_OK) {
				// User activated Bluetooth
			} else if (resultCode == RESULT_CANCELED) {
				// User denied activating Bluetooth
			}
		}
	}

	// Discover Bluetooth Devices
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			adapter.notifyDataSetChanged();
			String action = intent.getAction();
			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// Do something with name and address
				// device.getName() device.getAddress()
				Log.i("Hexapod", "Found bluetooth device");
				DeviceInfo d = new DeviceInfo(device);

				if (!devices.contains(d)) {
					devices.add(d);
					adapter.notifyDataSetChanged();
				}
			}
		}
	};
}
