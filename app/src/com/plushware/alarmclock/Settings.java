package com.plushware.alarmclock;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {
	static final String PREFS_NAME = "ALARMCLOCK_PREFS";
	
	static final String KEY_SENSOR_THRESHOLD = "SENSOR_THRESHOLD";
	static final String KEY_ALARM_HOUR = "ALARM_HOUR";
	static final String KEY_ALARM_MINUTE = "ALARM_MINUTE";
	
	final SharedPreferences mPreferences;
	
	public int sensorThreshold;	
	public int alarmHour;
	public int alarmMinute;
	
	public Settings(Context context) {
		mPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		
		load();
	}

	private void load() {
		sensorThreshold = (int)mPreferences.getLong(KEY_SENSOR_THRESHOLD, 22500);
		alarmHour = (int)mPreferences.getLong(KEY_ALARM_HOUR, 22);
		alarmMinute = (int)mPreferences.getLong(KEY_ALARM_MINUTE, 48);
	}	
	
	public void save() {
		SharedPreferences.Editor editor = mPreferences.edit();
		
		editor.putLong(KEY_SENSOR_THRESHOLD, sensorThreshold);
		editor.putLong(KEY_ALARM_HOUR, alarmHour);
		editor.putLong(KEY_ALARM_MINUTE, alarmMinute);
		
		editor.commit();
		load();
	}
}
