package it.amicosmanettone.assistenza.assistenzaitalia;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class StartApp extends Activity {
	
	SharedPreferences sharedPref;
	Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.v("Start", "Sto partendo");
		
		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		editor = sharedPref.edit();
		
		Boolean reboot = sharedPref.getBoolean("rebootApp", false);

		if (reboot) {

			editor.remove("rebootApp").commit();

			Intent main = new Intent(this, MainActivity.class);
			startActivity(main);
			finish();

		} else {
			
			Intent splash = new Intent(this, SplashActivity.class);
			startActivity(splash);
			finish();
			
		}
		
	}

}
