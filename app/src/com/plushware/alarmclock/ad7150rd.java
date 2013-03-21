package com.plushware.alarmclock;

public class ad7150rd {
	public native int test(String param);
	
	 static {
	        System.loadLibrary("ad7150rd");
	}	
}
