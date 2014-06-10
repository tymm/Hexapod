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
import java.lang.Math;

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
		dispatchCommand("G:1");
	}

	public void setGaitWaveTwo() {
		dispatchCommand("G:2");
	}

	public void setGaitWaveThree() {
		dispatchCommand("G:3");
	}

	public void setGaitTripod() {
		dispatchCommand("G:4");
	}

	public void setGaitOnRoad() {
		dispatchCommand("G:5");
	}

	public void setGaitOffRoad() {
		dispatchCommand("G:6");
	}

	public void sendUpUp() {
		dispatchCommand("a");
	}

	public void sendUpRight() {
		dispatchCommand("b");
	}

	public void sendRightRight() {
		dispatchCommand("c");
	}

	public void sendDownRight() {
		dispatchCommand("d");
	}

	public void sendDownDown() {
		dispatchCommand("e");
	}

	public void sendDownLeft() {
		dispatchCommand("f");
	}

	public void sendLeftLeft() {
		dispatchCommand("g");
	}

	public void sendUpLeft() {
		dispatchCommand("h");
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

	public void sendSpeed(int speed) {
		dispatchCommand("S:"+translate_speed(speed));
	}

	private int translate_speed(int value) {
		// speed is between 0 and 100
		// we need to scale it to 200-6000
		int left = 100;
		int right = 6000-200;

		float scaled = (float)value / (float)left;
		return (int)(200 + (scaled * right));
	}

	public void sendResetLegs() {
		dispatchCommand("L");
	}

	public void sendRotationReset() {
		dispatchCommand("R:"+0+";"+0+";"+0);
	}

	public void sendMove(int degree, int speed) {
		int[] xy = new int[2];
		xy = translate_degree(degree, speed);

		int X = xy[0];
		int Y = xy[1];

		dispatchCommand("M:"+Y+";"+X+";"+0);
	}

	private int[] translate_degree(int degree, int speed) {
		double x = Math.cos(Math.toRadians(degree));
		double y = Math.sin(Math.toRadians(degree));
		int[] result = new int[2];

		if (x < 0) {
			x = x * 128;
		} else {
			x = x * 127;
		}

		if (y < 0) {
			y = y * 128;
		} else {
			y = y * 127;
		}

		result[0] = (int)(x * speed / 100.0);
		result[1] = (int)(y * speed / 100.0);

		return result;
	}
}
