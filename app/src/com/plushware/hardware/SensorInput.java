package com.plushware.hardware;

public class SensorInput {
	public native int poll(String param);
	
	 static {
	        System.loadLibrary("sensorinput");
	}	
}
