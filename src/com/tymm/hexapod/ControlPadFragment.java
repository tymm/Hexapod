package com.tymm.hexapod;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.Button;
import android.view.MenuItem;
import android.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem.OnMenuItemClickListener;
import android.hardware.SensorManager;
import com.zerokol.views.JoystickView;
import com.zerokol.views.JoystickView.OnJoystickMoveListener;
import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class ControlPadFragment extends Fragment implements FragmentLifecycle {
	// Bluetooth Communication
	private Communication comm;

	// Joystick
	private JoystickView joystick;

	private Thread thread_left;
	private Thread thread_right;
	private boolean stop_right = false;
	private boolean stop_left = false;
	private int i;

	private void setJoystickListener(JoystickView jstick) {
		jstick.setOnJoystickMoveListener(new OnJoystickMoveListener() {

			@Override
			public void onValueChanged(int angle, int power, int direction) {
				// TODO Auto-generated method stub
				Log.i("Hexapod", " " + String.valueOf(angle) + "°");
				Log.i("Hexapod", " " + String.valueOf(power) + "%");

				// send direction
				comm.sendMove(angle, power);
			}
		}, JoystickView.DEFAULT_LOOP_INTERVAL);
	}

	private void unsetJoystickListener(JoystickView jstick) {
		jstick.setOnJoystickMoveListener(new OnJoystickMoveListener() {

			@Override
			public void onValueChanged(int angle, int power, int direction) {
			}
		}, JoystickView.DEFAULT_LOOP_INTERVAL);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_controlpad, container, false);

		// Joystick
		joystick = (JoystickView) rootView.findViewById(R.id.joystickView);
		setJoystickListener(joystick);
		Log.i("First", "OnCreateView");

		// Turn left/right buttons
		ImageView button_turn_left = (ImageView) rootView.findViewById(R.id.turn_left);
		ImageView button_turn_right = (ImageView) rootView.findViewById(R.id.turn_right);

		button_turn_left.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					// start thread
					Runnable runnable = new Runnable() {
						public void run() {
							while(!stop_left) {
								comm.sendTurnLeft();
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							stop_left = false;
						}
					};
					thread_left = new Thread(runnable);
					thread_left.start();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					// stop thread
					stop_left = true;
				}
				return true;
			}
		});

		button_turn_right.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					// start thread
					Runnable runnable = new Runnable() {
						public void run() {
							while(!stop_right) {
								comm.sendTurnRight();
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							stop_right = false;
						}
					};
					thread_right = new Thread(runnable);
					thread_right.start();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					// stop thread
					stop_right = true;
				}
				return true;
			}
		});

		return rootView;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i("First", "OnCreate");
		// Bluetooth Communication
		comm = (Communication)getActivity().getApplication();
	}

	@Override
	public void onPauseFragment() {
		// stop joystick
		unsetJoystickListener(joystick);
	}

	@Override
	public void onResumeFragment() {
		// start joystick
		setJoystickListener(joystick);
	}
}
