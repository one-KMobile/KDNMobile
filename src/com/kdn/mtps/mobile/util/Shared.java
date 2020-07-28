package com.kdn.mtps.mobile.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Shared {
	//DB에 저장하기 부담스러운 부분들을 저장 할 수 있게 제공해주는 클래스(xml형태로 저장)
	public static SharedPreferences getSP(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
	public static SharedPreferences.Editor getEditor(Context context) {
		return getSP(context).edit();
	}
	
	public static void set(Context context, String key, String value) {
		getEditor(context).putString(key, value).commit();
	}
	public static void sets(Context context, String[] keys, String[] values) {
		SharedPreferences.Editor editor = getEditor(context);
		if (keys.length != values.length)
			return;
		for (int i=0; i<keys.length; i++)
			editor.putString(keys[i], values[i]);
		editor.commit();
	}
	public static void set(Context context, String key, int value) {
		getEditor(context).putInt(key, value).commit();
	}
	public static void set(Context context, String key, float value) {
		getEditor(context).putFloat(key, value).commit();
	}
	public static void set(Context context, String key, boolean value) {
		getEditor(context).putBoolean(key, value).commit();
	}
	
	public static String getString(Context context, String key) {
		return getSP(context).getString(key, "");
	}
	public static String getString(Context context, String key, String def) {
		return getSP(context).getString(key, def);
	}
	public static int getInt(Context context, String key) {
		return getSP(context).getInt(key, 0);
	}
	public static int getInt(Context context, String key, int def) {
		return getSP(context).getInt(key, def);
	}
	public static float getFloat(Context context, String key) {
		return getSP(context).getFloat(key, 0);
	}
	public static boolean getBoolean(Context context, String key) {
		return getSP(context).getBoolean(key, false);
	}
	public static boolean getBoolean(Context context, String key, boolean def) {
		return getSP(context).getBoolean(key, def);
	}
}
