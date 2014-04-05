package com.tymm.hexapod;

import java.util.ArrayList;
import android.app.Activity;
import android.bluetooth.BluetoothClass.Device;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomArrayAdapter extends ArrayAdapter<DeviceInfo> {
	private ArrayList<DeviceInfo> entries;
	private Activity activity;

	public static class ViewHolder {
		public TextView item1;
		public TextView item2;
	}

	public CustomArrayAdapter(Activity a, int textViewResourceId, ArrayList<DeviceInfo> entries) {
		super(a, textViewResourceId, entries);
		this.entries = entries;
		this.activity = a;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.grid_item, null);
			holder = new ViewHolder();
			holder.item1 = (TextView) v.findViewById(R.id.big);
			holder.item2 = (TextView) v.findViewById(R.id.small);
			v.setTag(holder);
		} else {
			holder = (ViewHolder)v.getTag();
		}

		final DeviceInfo device = entries.get(position);
		if (device != null) {
			holder.item1.setText(device.getName());
			holder.item2.setText(device.getMac());
		}
		return v;
	}
}
