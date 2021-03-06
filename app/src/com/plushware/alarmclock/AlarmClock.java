package com.plushware.alarmclock;

import android.content.Context;

public final class AlarmClock extends android.app.Application {
	public static final boolean DEBUG_MODE = true;
	
	public static final String INTENT_ALARM_ON = "com.plushware.alarmclock.ALARM_ON";
	public static final String INTENT_ALARM_OFF = "com.plushware.alarmclock.ALARM_OFF";
	public static final String INTENT_SENSOR_HIT_SHORT = "com.plushware.alarmclock.SENSOR_HIT_SHORT";
	public static final String INTENT_SENSOR_HIT_LONG = "com.plushware.alarmclock.SENSOR_HIT_LONG";
	public static final String INTENT_SCREEN_ON_OFF = "com.plushware.alarmclock.SCREEN_ON_OFF";
	public static final String INTENT_SCREEN_ON = "com.plushware.alarmclock.SCREEN_ON";
	public static final String INTENT_SCREEN_OFF = "com.pluswhare.alarmclock.SCREEN_OFF";
	
	private static AlarmClock instance;
	
	public AlarmClock() {
		instance = this;
	}
	
	public static Context getContext() {
		return instance;
	}
}
