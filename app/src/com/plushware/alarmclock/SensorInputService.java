package com.plushware.alarmclock;

import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.util.Log;

import com.plushware.hardware.SensorInput;

public class SensorInputService extends Service {
	private static final String TAG = "SensorInputService";
	
	public SensorInputService() {
	}
	
	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
		
		SensorInput test = new SensorInput();
		
		test.poll("asdf asdf");
	}

	@Override
	public int onStartCommand(android.content.Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand");
		
		return Service.START_STICKY;	
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
