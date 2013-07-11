package com.plushware.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WakeupReceiver extends BroadcastReceiver {
	public static final String TAG = "WakeupReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		
		if (action == null) {
			alarmOn(context);
		} else if (action == AlarmClock.INTENT_ALARM_OFF) {
			// TODO?
		}
	}

	private void alarmOn(Context context) {
		Log.d(TAG, "Time to wake up!");
		
		Intent clockIntent = new Intent(context, ClockActivity.class);
		clockIntent.putExtra("ALARM_IS_ON", true);
		clockIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(clockIntent);

		context.sendBroadcast(new Intent(AlarmClock.INTENT_ALARM_ON));		
		context.sendBroadcast(new Intent(AlarmClock.INTENT_SCREEN_ON));
	}
}