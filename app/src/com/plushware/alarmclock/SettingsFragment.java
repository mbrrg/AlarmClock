package com.plushware.alarmclock;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.settings);
	    PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	public void onDestroy() {
	    super.onDestroy();

	    PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(Settings.KEY_ALARM_TIME)) {
			Settings settings = new Settings(sharedPreferences);
			
			String time = String.format("%02d:%02d", settings.alarmHour, settings.alarmMinute);
			
			findPreference(Settings.KEY_ALARM_TIME).setSummary(time);
		}			
	}
}
