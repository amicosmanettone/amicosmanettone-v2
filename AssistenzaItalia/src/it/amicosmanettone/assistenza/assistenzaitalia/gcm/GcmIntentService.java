package it.amicosmanettone.assistenza.assistenzaitalia.gcm;

import it.amicosmanettone.assistenza.assistenzaitalia.R;
import it.amicosmanettone.assistenza.assistenzaitalia.MainActivity;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class GcmIntentService extends IntentService {

	static String TAG = "GcmIntentService";

	SharedPreferences sharedPref;
	Editor editor;

	public GcmIntentService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		this.sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		this.editor = sharedPref.edit();

		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		Log.v("SERVICE", "Sono dentro intentService");
		Log.v("SERVICE",
				"Extra ----> " + intent.getExtras().getString("message"));

		if (!extras.isEmpty()) { 
			
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification("Deleted messages on server: "
						+ extras.toString());
				
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {

				if (sharedPref.getBoolean("receiveChatNotification", true) == true) {

					if (sharedPref.getBoolean("chatIsActive", false) == false) {

						sendNotification(extras.getString("message"));

					} else {

						Log.v(TAG, "Non mostro notifica, la chat è gia aperta");

					}

				} else if (sharedPref.getBoolean("isTecnico", false) == true) {

					if (sharedPref.getBoolean("chatIsActive", false) == false) {

						sendNotification(extras.getString("message"));

					} else {

						Log.v(TAG, "Non mostro notifica, la chat è gia aperta");

					}

				}

				Log.i(TAG, "Received: " + extras.toString());
			}
		}

		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(String msg) {

		
		editor.putInt("lastViewVisited", 1).commit();

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
			.setLights(0xff00ffff, 300, 1000)
			.setAutoCancel(true)
			.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentTitle(getText(R.string.app_name))
			.setContentText(msg);

		Intent resultIntent = new Intent(this, MainActivity.class);

		
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		
		stackBuilder.addParentStack(MainActivity.class);
		
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		mNotificationManager.notify(1, mBuilder.build());
	}
}