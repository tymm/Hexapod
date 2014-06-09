package com.tymm.hexapod;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Application;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/** This class is used to maintain global application state about the bluetooth connection and to send out data over bluetooth */
public class Communication extends Application implements Runnable {
	private BluetoothSocket socket = null;
	private InputStream inStream = null;
	private OutputStream outStream = null;
	private String message = null;

	public Handler mHandler;

	public Communication() {}

	// Sending happens in extra thread so we can dispatch messages without blocking anything
	@Override
	public void run() {
		Looper.prepare();

		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				write(msg.getData().getString("command"));
			}
		};
		Looper.loop();
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
		// Append \n as the end of a command (important for the microcontroller)
		str = str + '\n';
		byte[] buffer = str.getBytes();
		try {
			outStream.write(buffer);
			Log.i("Hexapod", "Wrote to bluetooth socket");
		} catch(IOException e) {
			Log.e("Hexapod", "Could not write to bluetooth output stream", e);
		}
	}

	private void dispatchCommand(String str) {
		Message msg = mHandler.obtainMessage();

		Bundle b = new Bundle();
		b.putString("command", str);

		msg.setData(b);

		mHandler.sendMessage(msg);
	}


	public void setGaitWaveOne() {
		dispatchCommand("Gait:Wave1");
	}

	public void setGaitWaveTwo() {
		dispatchCommand("Gait:Wave2");
	}

	public void setGaitWaveThree() {
		dispatchCommand("Gait:Wave3");
	}

	public void sendUpUp() {
		dispatchCommand("a");
	}

	public void sendUpRight() {
		dispatchCommand("e");
	}

	public void sendRightRight() {
		dispatchCommand("b");
	}

	public void sendDownRight() {
		dispatchCommand("f");
	}

	public void sendDownDown() {
		dispatchCommand("c");
	}

	public void sendDownLeft() {
		dispatchCommand("");
	}

	public void sendLeftLeft() {
		dispatchCommand("d");
	}

	public void sendUpLeft() {
		dispatchCommand("");
	}

	public void sendRotation(int X, int Y, int Z) {
		dispatchCommand("R:"+X+";"+Y+";"+Z);
	}

	public void sendWakeUp() {
		dispatchCommand("W");
	}

	public void sendSleep() {
		dispatchCommand("S");
	}

	public void sendShutdown() {
		dispatchCommand("X");
	}

	public void sendTurnLeft() {
		dispatchCommand("l");
	}

	public void sendTurnRight() {
		dispatchCommand("r");
	}

}
