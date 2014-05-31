package it.amicosmanettone.assistenza.assistenzaitalia.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;

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

}
