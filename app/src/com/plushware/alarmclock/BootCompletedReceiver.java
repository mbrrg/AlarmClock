package com.plushware.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver {
	public BootCompletedReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("BootCompletedReceiver", "Boot completed.");
				
		AlarmTrigger.enable();
	}
}
