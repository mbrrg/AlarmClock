package com.plushware.alarmclock;

import android.content.Intent;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;

public class AlarmService extends WakefulIntentService {
	public AlarmService() {
		super("AlarmService");
	}
	
	@Override
	protected void doWakefulWork(Intent intent) {
		Log.d("AlarmService", "Sound the alarm!");
	}
}
