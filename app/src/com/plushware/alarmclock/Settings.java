package com.plushware.alarmclock;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {
	//static final String PREFS_NAME = "ALARMCLOCK_PREFS";
	
	public static final String KEY_ALARM_ENABLED = "pref_alarm_enabled";
	public static final String KEY_ALARM_TIME = "pref_alarm_time";
	
	static final String KEY_SENSOR_THRESHOLD = "SENSOR_THRESHOLD";
	static final String KEY_SCREEN_WAKE_TIME = "SCREEN_WAKE_TIME";
	
	//final SharedPreferences mPreferences;

	public boolean alarmEnabled;
	public int alarmHour;
	public int alarmMinute;	
	public int sensorThreshold;	
	public int screenWakeTime;
	
	public Settings(Context context) {
		//mPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		load(prefs);
	}
	
	public Settings(SharedPreferences prefs) {
		load(prefs);		
	}

	private void load(SharedPreferences prefs) {		
		alarmEnabled = prefs.getBoolean(KEY_ALARM_ENABLED, false);
		
		String alarmTime = prefs.getString(KEY_ALARM_TIME, "08:00");
		
		alarmHour = TimePreference.getHour(alarmTime);
		alarmMinute = TimePreference.getMinute(alarmTime);
	
		screenWakeTime = (int)prefs.getLong(KEY_SCREEN_WAKE_TIME, 10);
		sensorThreshold = (int)prefs.getLong(KEY_SENSOR_THRESHOLD, 22500);		
	}	
	/*
	public void save() {
		SharedPreferences.Editor editor = mPreferences.edit();
		
		editor.putLong(KEY_SENSOR_THRESHOLD, sensorThreshold);
		editor.putLong(KEY_ALARM_HOUR, alarmHour);
		editor.putLong(KEY_ALARM_MINUTE, alarmMinute);
		editor.putLong(KEY_SCREEN_WAKE_TIME, screenWakeTime);
		
		editor.commit();
		load();
	}*/
}
