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
import android.widget.ImageButton;

public class ControlPadFragment extends Fragment {
	// Bluetooth Communication
	private Communication comm;

	// Joystick
	private JoystickView joystick;

	private void setJoystickListener(JoystickView jstick) {
		jstick.setOnJoystickMoveListener(new OnJoystickMoveListener() {

			@Override
			public void onValueChanged(int angle, int power, int direction) {
				// TODO Auto-generated method stub
				Log.i("Hexapod", " " + String.valueOf(angle) + "°");
				Log.i("Hexapod", " " + String.valueOf(power) + "%");
				switch (direction) {
					case JoystickView.FRONT:
						Log.i("Hexapod", "Front");
						comm.sendUpUp();
						break;
					case JoystickView.FRONT_RIGHT:
						Log.i("Hexapod", "FRONT LEFT");
						comm.sendUpLeft();
						break;
					case JoystickView.RIGHT:
						Log.i("Hexapod", "LEFT");
						comm.sendLeftLeft();
						break;
					case JoystickView.RIGHT_BOTTOM:
						Log.i("Hexapod", "LEFT BOTTOM");
						comm.sendDownLeft();
						break;
					case JoystickView.BOTTOM:
						Log.i("Hexapod", "BOTTOM");
						comm.sendDownDown();
						break;
					case JoystickView.BOTTOM_LEFT:
						Log.i("Hexapod", "BOTTOM RIGHT");
						comm.sendDownRight();
						break;
					case JoystickView.LEFT:
						Log.i("Hexapod", "RIGHT");
						comm.sendRightRight();
						break;
					case JoystickView.LEFT_FRONT:
						Log.i("Hexapod", "RIGHT FRONT");
						comm.sendUpRight();
						break;
					default:
						Log.i("Hexapod", "Center");
				}
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

		// Turn left/right buttons
		ImageButton button_turn_left = (ImageButton) rootView.findViewById(R.id.turn_left);
		ImageButton button_turn_right = (ImageButton) rootView.findViewById(R.id.turn_right);

		button_turn_left.setOnLongClickListener(new View.OnLongClickListener() {
			public boolean onLongClick(View v) {
				return true;
			}
		});

		return rootView;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Bluetooth Communication
		comm = (Communication)getActivity().getApplication();
		comm.start();
	}

	@Override
	public void onResume() {
		super.onResume();

		if (isVisible()) {
			// start joystick
			try {
				setJoystickListener(joystick);
			} catch (NullPointerException e) {}
		} else {
			// stop joystick
			try {
				unsetJoystickListener(joystick);
			} catch (NullPointerException e) {}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		// Stop joystick
		unsetJoystickListener(joystick);
	}
}
