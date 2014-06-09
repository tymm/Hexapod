package com.tymm.hexapod;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> fragments;

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
		this.fragments = new ArrayList<Fragment>();
		fragments.add(new ControlPadFragment());
		fragments.add(new ControlFragment());
		fragments.add(new RotationFragment());
		fragments.add(new PrefFragment());
	}

	@Override
	public Fragment getItem(int index) {
		return fragments.get(index);
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return fragments.size();
	}

}
