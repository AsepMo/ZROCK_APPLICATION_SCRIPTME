/*
rootadb
(c) 2013 Pau Oliva Fora <pof[at]eslack[dot]org>
*/
package zrock.application.scriptme;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import zrock.application.engine.Engine;

public class BootReceiver extends BroadcastReceiver{

	private Engine mUtils;

	@Override
	public void onReceive(Context context, Intent intent) {
		mUtils = new Engine(context);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		boolean start_onboot = prefs.getBoolean("start_onboot", false);
		if (start_onboot) {
			mUtils.doTheMeat();
		}
	}
}
