package com.tymm.hexapod;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

/** This class tries to connect to a bluetooth device which has to be done in a thread (otherwise it would block the UI) */
public class ConnectThread extends Thread {
	private final BluetoothSocket socket;
	private final BluetoothDevice device;
	private final BluetoothAdapter bluetoothAdapter;
	private final Handler handler;
	private final Context context;
	// UUID for SPP (Serial Port Profile)
	private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	public ConnectThread(BluetoothDevice device, BluetoothAdapter bluetoothAdapter, Handler handler, Context context) {
		BluetoothSocket tmp = null;
		this.device = device;
		this.handler = handler;
		this.bluetoothAdapter = bluetoothAdapter;
		this.context = context;

		// Get a BluetoothSocket to connect with the given BluetoothDevice
		try {
			// MY_UUID is the app's UUID string, also used by the server code
			tmp = this.device.createRfcommSocketToServiceRecord(MY_UUID);
			//tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
		} catch(IOException e) {
			Log.e("Hexapod", "Could not get bluetooth socket");
		}
		socket = tmp;
	}

	public void run() {
		// Cancel bluetooth discovery because it would slow down the connection
		bluetoothAdapter.cancelDiscovery();

		try {
			// Connect the device through the socket. This will block until it
			// succeeds or throws an exception
			socket.connect();
		} catch (IOException connectException) {
			// Unable to connect; close the socket and stop this thread
			handler.sendEmptyMessage(1);

			Log.e("Hexapod", "Not able to connect");
			Log.e("Hexapod", connectException.toString());

			try {
				socket.close();
			} catch (IOException closeException) { }

			return;
		}

		// Hold the socket in a singleton like Communication object
		Communication comm = ((Communication)context.getApplicationContext());
		comm.setSocket(socket);

		// Stop ProgressDialog in UI
		handler.sendEmptyMessage(0);
	}

	/** Will cancel an in-progress connection, and close the socket */
	public void cancel() {
		try {
			socket.close();
		} catch (IOException e) { }
	}
}
