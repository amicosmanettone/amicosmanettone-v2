package it.amicosmanettone.assistenza.assistenzaitalia;

import it.amicosmanettone.assistenza.assistenzaitalia.util.GeneralFunction;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ProgressBar;

public class SplashActivity extends Activity {

	SharedPreferences sharedPref;
	Editor editor;

	String regId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		editor = sharedPref.edit();

		setContentView(R.layout.activity_splash);

		ProgressBar progress = (ProgressBar) findViewById(R.id.progressBarSplash);
		
		if (GeneralFunction.verifyConnection(this)) {
	
			Operations operations = new Operations(this, sharedPref, editor);
		
		}
	}

	@Override
	public void onBackPressed() {

	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.v("Splash", "onPause");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v("Splash", "onResume");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.v("Splash", "onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.v("Splash", "onDestroy");
	}

}
