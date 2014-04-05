package com.tymm.hexapod;

import android.bluetooth.BluetoothDevice;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class DeviceInfo {
	private String name;
	private String MAC;
	private BluetoothDevice device;

	public DeviceInfo(BluetoothDevice device) {
		this.device = device;
		this.name = device.getName();
		this.MAC = device.getAddress();
	}

	public BluetoothDevice getBluetoothDevice() {
		return this.device;
	}

	public String getName() {
		return this.name;
	}

	public String getMac() {
		return this.MAC;
	}

	public int hashCode() {
		return new HashCodeBuilder(17, 37).
		append(name).
		append(MAC).
		toHashCode();
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof DeviceInfo)) {
			return false;
		}

		DeviceInfo rhs = (DeviceInfo) obj;
		return new EqualsBuilder().
			append(name, rhs.name).
			append(MAC, rhs.MAC).
			isEquals();
	}
}
