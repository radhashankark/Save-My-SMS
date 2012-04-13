/*
 * Started as a class to reduce the number of steps it takes to read/write Preferences
 * Turns out, this adds one more step, without much advantage. Abandoning.
 */

package com.shankarlabs.sms.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class PrefHelper {

	private static Context appContext = null;
	private static PrefHelper prefHelper; // = null;
	private static String LOGTAG = "SaveMySMS";
	private SharedPreferences prefs;
	private SharedPreferences.Editor prefsEditor;
	
	private PrefHelper() {
		if(prefHelper == null) {
			prefHelper = new PrefHelper();
			prefs = PreferenceManager.getDefaultSharedPreferences(appContext);
	    	prefsEditor = prefs.edit();
		}
	}
	
	/**
	 * PrefHelper getInstance(Context mContext)
	 * Returns a singleton instance of PrefHelper. 
	 * @param mContext	Requires a Context to use Preferences
	 * @return PrefHelper	Returns an instance of PrefHelper
	 */
	public static PrefHelper getInstance(Context mContext) {

		if(appContext == null) {
			appContext = mContext;
		}
		
		if (prefHelper == null) {
			Log.d(LOGTAG, "PrefHelper : getInstance : is null, creating new instance");
			prefHelper = new PrefHelper();
		}
		
		return prefHelper;
	}
	
	/**
	 * boolean writeString(String where, String key, String value)
	 * Writes the Preferences to the specified Preference file
	 * @param where	The name of the Preference file to write to
	 * @param key	Key for the Value to write to
	 * @param value	Value to be written to Preferences
	 * @return boolean	true if successful, false if unsuccessful
	 */
	public boolean writeString(String where, String key, String value) {
		
		// Get the Shared Preferences for the Application so we can update the status
		Log.d(LOGTAG, "PrefHelper : writePref : Getting PreferenceManager and editing it");
		prefsEditor.putString(key, value);
    	prefsEditor.commit();
		return true;
	}
	
	/**
	 * boolean writeInt(String where, String key, int value)
	 * Writes the Preferences to the specified Preference file
	 * @param where	The name of the Preference file to write to
	 * @param key	Key for the Value to write to
	 * @param value	Value to be written to Preferences
	 * @return boolean	true if successful, false if unsuccessful
	 */
	public boolean writeInt(String where, String key, int value) {
		
		// Get the Shared Preferences for the Application so we can update the status
		Log.d(LOGTAG, "PrefHelper : writePref : Getting PreferenceManager and editing it");
    	prefsEditor.putInt(key, value);
    	prefsEditor.commit();
		return true;
	}
	
	/**
	 * Object writePref(String where, String key)
	 * Reads the Preferences and returns the Object stored for the key
	 * @param where	Name of the Preferences to read from. 
	 * @param key	Name of the key for which the value needs retrieved
	 * @return Object	The Object of the value stored for the specified key
	 */
	public String readPref(String where, String key) {
		prefs.getString(key, null);
		return prefs.getString(key, null);
	}
}
