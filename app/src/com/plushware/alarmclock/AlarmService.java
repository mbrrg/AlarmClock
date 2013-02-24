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
		
		Intent alarmIntent = new Intent(this, AlarmActivity.class);
		alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		startActivity(alarmIntent);
	}
}
