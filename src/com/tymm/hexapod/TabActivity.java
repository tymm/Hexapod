package com.tymm.hexapod;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;

public class TabActivity extends Activity implements ActionBar.TabListener {
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	private Communication comm;
	// Tab titles
	private String[] tabs = { "Joystick", "Control", "Rotation", "Settings" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs);

		// Start bluetooth communication
		comm = (Communication)getApplication();
		comm.start();

		// If there are saved settings, send them to the Hexapod
		loadSettings();

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getFragmentManager());

		viewPager.setAdapter(mAdapter);
		viewPager.setOffscreenPageLimit(4);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		}

		// When swiping view, select tab
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			int currentPosition = 0;

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);

				// Let the fragment know when it really is visible or not
				FragmentLifecycle fragmentToShow = (FragmentLifecycle)mAdapter.getItem(position);
				fragmentToShow.onResumeFragment();

				FragmentLifecycle fragmentToHide = (FragmentLifecycle)mAdapter.getItem(currentPosition);
				fragmentToHide.onPauseFragment();

				currentPosition = position;
			}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		});
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	// Send settings to Hexapod
	private void loadSettings() {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

		String gait = sharedPref.getString("pref_gait","");
		String road = sharedPref.getString("pref_road","");
		int speed = sharedPref.getInt("pref_speed", -1);

		// Set gait
		if (!gait.equals("")) {
			switch(Integer.parseInt(gait)) {
				case 1:
					comm.setGaitTripod();
					break;
				case 2:
					comm.setGaitWaveTwo();
					break;
				case 3:
					comm.setGaitWaveThree();
					break;
				case 4:
					comm.setGaitTripod();
					break;
			}
		}

		// Set road
		if (!road.equals("")) {
			switch(Integer.parseInt(road)) {
				case 1:
					comm.setGaitOnRoad();
					break;
				case 2:
					comm.setGaitOffRoad();
					break;
			}
		}

		// Set speed
		if (speed != -1) {
			comm.sendSpeed(speed);
		}
	}
}
