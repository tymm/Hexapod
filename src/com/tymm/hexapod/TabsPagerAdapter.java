package com.tymm.hexapod;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
			case 0:
				// ControlPad fragment activity
				return new ControlPadFragment();
			case 1:
				// Control fragment activity
				return new ControlFragment();
			case 2:
				// Rotation fragment activity
				return new RotationFragment();
			case 3:
				// Settings fragment activity
				return new PrefFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 4;
	}

}
