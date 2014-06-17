package it.amicosmanettone.assistenza.assistenzaitalia.util;

import static it.amicosmanettone.assistenza.assistenzaitalia.configuration.Configuration.PLAYSTORE_URL;
import it.amicosmanettone.assistenza.assistenzaitalia.MainActivity;
import it.amicosmanettone.assistenza.assistenzaitalia.mysql.UserFunctions;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

public class GeneralFunction {

	public static void activateStrictMode(boolean ACTIVATE) {

		if (ACTIVATE) {

			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);

		}

	}

	public static boolean verifyConnection(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnectedOrConnecting()) {

			return true;

		}

		return false;

	}
	
	public static void messageBox(String message, Context context) {
		new AlertDialog.Builder(context)
				.setMessage(message)
				.setCancelable(true)
				.setNeutralButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						}).show();
	}
	
	public static String setUserName(SharedPreferences sharedPref, Editor editor){
		
		if (sharedPref.getString("usernameUtente", "").isEmpty()) {

			Log.v("General", "Non ho un nome, lo setto");

			Random r = new Random();
			int ospiteNumber = r.nextInt(10000 - 1000) + 999;

			String UTENTE = "Ospite_android" + ospiteNumber;
			editor.putString("usernameUtente", UTENTE).commit();

		}
		
		return sharedPref.getString("usernameUtente", "");
		
	}
	
	

}
