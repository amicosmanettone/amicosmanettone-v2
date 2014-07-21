package it.amicosmanettone.assistenza.assistenzaitalia;

import static it.amicosmanettone.assistenza.assistenzaitalia.configuration.Configuration.PLAYSTORE_URL;
import static it.amicosmanettone.assistenza.assistenzaitalia.configuration.Configuration.SENDER_ID;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import it.amicosmanettone.assistenza.assistenzaitalia.mysql.UserFunctions;
import it.amicosmanettone.assistenza.assistenzaitalia.util.GeneralFunction;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ProgressBar;

public class SplashActivity extends Activity {

	SharedPreferences sharedPref;
	Editor editor;

	String regId;
	
	Boolean update = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		editor = sharedPref.edit();

		setContentView(R.layout.activity_splash);

		ProgressBar progress = (ProgressBar) findViewById(R.id.progressBarSplash);
		
		if (GeneralFunction.verifyConnection(this)) {
	
			new OPERATION().execute();
		
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
	
	public class OPERATION extends AsyncTask<Void, Integer, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			String utente = GeneralFunction.setUserName(sharedPref, editor);

			Log.v("Splash", "Nome utente --> " + utente);

			regId = sharedPref.getString("registrationId", "");

			Log.v("Splash", "Recupero registration ID ---> " + regId);

		}

		@Override
		protected Boolean doInBackground(Void... params) {

			if (!sharedPref.getBoolean("registrationIdDB", false)) {

				Log.v("Splash", "Non sono registrato al GCM, mi registro");
				new registraGCM().execute();

			} else {

				Log.v("Splash", "Sono registrato al GCM");

			}

			if (!regId.isEmpty()) {

				if (sharedPref.getBoolean("updateUserOnDb", false) == true) {

					new updateUserOnDB().execute();

				}
			}

			new verifyVersionApp().execute();

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			if (!update) {

				Intent main = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(main);
				finish();

			} else {

				AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);

				builder.setMessage(
						"Hai una vecchia versione dell'applicazione!")
						.setCancelable(false)

						.setPositiveButton("Aggiorna",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int id) {

										Intent i = new Intent(
												Intent.ACTION_VIEW);
										i.setData(Uri.parse(PLAYSTORE_URL));
										startActivity(i);
										finish();

									}
								});

				AlertDialog alert = builder.create();
				alert.show();
			}
		}

	}

	private class registraGCM extends AsyncTask<Void, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {

			GoogleCloudMessaging gcm = null;

			if (regId.isEmpty()) {
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging
								.getInstance(getApplicationContext());
					}
					regId = gcm.register(SENDER_ID);
					Log.v("SPlash", "Registration ID -----> " + regId);
					editor.putString("registrationId", regId).commit();

				} catch (Exception ex) {
					System.out.println("Errore dati");
				}
			}

			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.registerDevice(regId,
					sharedPref.getString("usernameUtente", "Ospite_android"),
					android.os.Build.VERSION.RELEASE);

			try {
				if (json != null) {
					if (Integer.parseInt(json.getString("success")) == 1) {

						Log.v("Splash", "Registrazione nel db riuscita");
						editor.putBoolean("registrationIdDB", true).commit();

					} else {

						if (json.getString("error").equals("Gia esiste")) {

							Log.v("Splash",
									"Registrazione nel db riuscita, già esiste");
							editor.putBoolean("registrationIdDB", true)
									.commit();
							editor.putBoolean("updateUserOnDb", true).commit();

						}

					}

				}
			} catch (NumberFormatException e) {

				e.printStackTrace();
			} catch (JSONException e) {

				e.printStackTrace();
			}

			return true;
		}
	}

	private class verifyVersionApp extends AsyncTask<Void, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {

			Boolean result = false;

			PackageInfo pInfo = null;
			try {
				pInfo = SplashActivity.this.getPackageManager().getPackageInfo(
						SplashActivity.this.getPackageName(), 0);
			} catch (NameNotFoundException e) {

				e.printStackTrace();
			}

			String version = pInfo.versionName;

			Log.v("Splash", "VERSION ----->   " + version);

			UserFunctions userFunction = new UserFunctions();

			JSONObject updateUserDevice = userFunction.checkVersion(version);

			try {
				if (updateUserDevice != null) {
					if (Integer.parseInt(updateUserDevice.getString("success")) == 0) {

						result = true;

					}
				}
			} catch (JSONException e) {

				e.printStackTrace();

			}

			return result;

		}

		protected void onPostExecute(Boolean result) {

			if (result) {

				update = true;

			}

		}

	}

	private class updateUserOnDB extends AsyncTask<Void, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {

			Boolean result = false;

			UserFunctions userFunction = new UserFunctions();

			JSONObject updateUserDevice = userFunction.updateUserDevice(
					sharedPref.getString("registrationId", ""),
					sharedPref.getString("usernameUtente", ""));

			try {
				if (updateUserDevice != null) {
					if (Integer.parseInt(updateUserDevice.getString("success")) == 1) {

						result = true;

					}
				}
			} catch (JSONException e) {

				e.printStackTrace();

			}

			return result;
		}

		protected void onPostExecute(Boolean result) {

			if (result) {

				editor.remove("updateUserOnDb").commit();

			}

		}

	}

}
