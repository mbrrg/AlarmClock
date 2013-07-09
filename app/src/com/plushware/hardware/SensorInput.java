package com.plushware.hardware;

import android.os.Build;
import android.util.Log;

public class SensorInput {
	final static String TAG = "SensorInputManaged";

	public static native int getThreshold();
	public static native int setThreshold(int threshold);
	public static native int getValue();
	public static native int getRawValue();
	public static native int poll();
	public static native int init();

	public static boolean isEnabled()
	{
		return !("sdk".equals(Build.PRODUCT));
	}
	
	static {
		if (isEnabled()) {
			System.loadLibrary("sensorinput");
		} else {
			Log.d(TAG, "Not loading sensorinput library since running in the emulator.");
		}
	}
}
