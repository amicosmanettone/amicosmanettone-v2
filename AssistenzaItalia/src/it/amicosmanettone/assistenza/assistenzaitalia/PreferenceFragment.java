package it.amicosmanettone.assistenza.assistenzaitalia;

import it.amicosmanettone.assistenza.assistenzaitalia.R;
import it.amicosmanettone.assistenza.assistenzaitalia.util.GeneralFunction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;

public class PreferenceFragment extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	SharedPreferences prefs;
	Editor editor;

	Preference preference;

	PreferenceScreen root;

	ListPreference listChatUpdate;
	CheckBoxPreference checkRememberUser;
	CheckBoxPreference checkRememberModule;
	CheckBoxPreference checkReceiveNotification;

	String valueOfChatUpdate;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		this.editor = prefs.edit();

		this.prefs.registerOnSharedPreferenceChangeListener(this);

		addPreferencesFromResource(R.xml.preference);
		this.root = this.getPreferenceScreen();

		this.listChatUpdate = (ListPreference) root
				.findPreference("updateChatSecond");

		Log.v("PREFERENZE", "Valore di aggiornamento chat ---> "
				+ listChatUpdate.getValue());

		this.listChatUpdate.setSummary(this.valueOfChatUpdate);

		this.listChatUpdate.setPersistent(true);

		this.checkRememberUser = (CheckBoxPreference) getPreferenceManager()
				.findPreference("rememberUsername");

		Log.v("PREFERENZE", "Valore di ricorda utente ---> "
				+ checkRememberUser.isChecked());

		checkRememberUser
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						if (newValue.toString().equals("true")) {

						} else {

							Log.v("PREFERENZE",
									"Rimuovo nome utente salvato ---> "
											+ prefs.getString("oldUser", ""));
							editor.remove("oldUser").commit();

						}
						return true;
					}
				});

		this.checkRememberModule = (CheckBoxPreference) getPreferenceManager()
				.findPreference("rememberModule");

		Log.v("PREFERENZE", "Valore di ricorda modulo ---> "
				+ checkRememberModule.isChecked());

		this.checkReceiveNotification = (CheckBoxPreference) getPreferenceManager()
				.findPreference("receiveChatNotification");

		if (prefs.getBoolean("isTecnico", false) == true) {

			this.checkReceiveNotification.setEnabled(false);

		}
		
		if (prefs.getBoolean("isTecnico", false) == true) {

			GeneralFunction
					.messageBox(
							"I tecnici non possono disabilitare le notifiche!",
							this);

		}
			

		Log.v("PREFERENZE", "Valore di ricevi notifiche ---> "
				+ checkReceiveNotification.isChecked());

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {

		this.preference = findPreference(key);

		if (preference instanceof ListPreference) {

			listChatUpdate.setSummary(listChatUpdate.getValue());

		}

	}

	@Override
	public void onBackPressed() {

		Intent refresh = new Intent(this, MainActivity.class);
		refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(refresh);
		overridePendingTransition(R.xml.translate_in, R.xml.translate_out);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}