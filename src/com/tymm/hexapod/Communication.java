package com.tymm.hexapod;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Application;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

/** This class is used to maintain global application state about the bluetooth connection and to send out data over bluetooth */
public class Communication extends Application implements Runnable {
	private BluetoothSocket socket = null;
	private InputStream inStream = null;
	private OutputStream outStream = null;
	private String message = null;

	public Communication() {}

	// Sending happens in extra thread so we can dispatch messages without blocking anything
	@Override
	public void run() {
		while (true) {
			if (this.message != null) {
				write(this.message);
				this.message = null;
			} else {
				// Sleep
			}
		}
	}

	public void start() {
		Thread t = new Thread(this);
		t.start();
	}

	public void setSocket(BluetoothSocket socket) {
		this.socket = socket;
		InputStream tmpIn = null;
		OutputStream tmpOut = null;

		try {
			tmpIn = socket.getInputStream();
			tmpOut = socket.getOutputStream();
		} catch (IOException e) {
			Log.e("Hexapod", "Could not create input/output for bluetooth", e);
		}

		this.inStream = tmpIn;
		this.outStream = tmpOut;
	}

	public BluetoothSocket getSocket() {
		// isConnected() added in API level 14
		if (socket.isConnected()) {
			return socket;
		} else {
			return null;
		}
	}

	public void write(String str) {
		byte[] buffer = str.getBytes();
		try {
			outStream.write(buffer);
			Log.i("Hexapod", "Wrote to bluetooth socket");
		} catch(IOException e) {
			Log.e("Hexapod", "Could not write to bluetooth output stream", e);
		}
	}

	public void setGaitWaveOne() {
		this.message = "Gait:Wave1";
	}

	public void setGaitWaveTwo() {
		this.message = "Gait:Wave2";
	}

	public void setGaitWaveThree() {
		this.message = "Gait:Wave3";
	}

	public void sendUpUp() {
		this.message = "UPUP";
	}

	public void sendUpRight() {
		this.message = "UPUP";
	}

	public void sendRightRight() {
		this.message = "UPUP";
	}

	public void sendDownRight() {
		this.message = "UPUP";
	}

	public void sendDownDown() {
		this.message = "UPUP";
	}

	public void sendDownLeft() {
		this.message = "UPUP";
	}

	public void sendLeftLeft() {
		this.message = "UPUP";
	}

	public void sendUpLeft() {
		this.message = "UPUP";
	}
}
