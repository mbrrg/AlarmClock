package com.plushware.hardware;

public class SensorInput {
	public static native int getThreshold();
	public static native int setThreshold(int threshold);
	public static native int getValue();
	public static native int getRawValue();
	public static native int poll();
	public static native int init();
	
	 static {
	        System.loadLibrary("sensorinput");
	}	
}
