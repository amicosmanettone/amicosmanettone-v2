package it.amicosmanettone.assistenza.assistenzaitalia;

import it.amicosmanettone.assistenza.assistenzaitalia.R;
import it.amicosmanettone.assistenza.assistenzaitalia.mysql.UserFunctions;
import it.amicosmanettone.assistenza.assistenzaitalia.util.GeneralFunction;

import org.json.JSONException;
import org.json.JSONObject;

import com.todddavies.components.progressbar.ProgressWheel;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TecniciFragment extends Fragment {

	View rootView;

	SharedPreferences sharedPref;
	Editor editor;

	View topLevelLayout;
	View bottomLevelLayout;

	public TecniciFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		GeneralFunction.activateStrictMode(false);

		sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getActivity()
						.getApplicationContext());

		editor = sharedPref.edit();

		editor.putInt("lastViewVisited", 3).commit();

		this.rootView = inflater.inflate(R.layout.fragment_tecnici, container,
				false);

		this.topLevelLayout = rootView.findViewById(R.id.tecnici_top_layout);
		this.bottomLevelLayout = rootView.findViewById(R.id.tecnici_bottom_layout);

		final ProgressWheel pw = (ProgressWheel) rootView
				.findViewById(R.id.tecnici_spinner);

		class DownloadInterventi extends AsyncTask<Void, Void, Boolean> {

			String karmaUtente;

			@Override
			protected void onPreExecute() {

				pw.spin();

			}

			@Override
			protected Boolean doInBackground(Void... params) {

				UserFunctions userFunction = new UserFunctions();
				JSONObject json = userFunction.getUserKarma(sharedPref
						.getString("usernameUtente", "Ospite"));

				try {
					if (json != null) {
						if (Integer.parseInt(json.getString("success")) == 1) {

							karmaUtente = json.getString("userKarma");

						}// End if success == 1

					}// End if json != null
				}// End try
				catch (JSONException e) {

					e.printStackTrace();

				}// End catch

				return true;

			}// End doInBackground...

			protected void onPostExecute(Boolean result) {

				TextView karmaText = (TextView) rootView
						.findViewById(R.id.textKarma);

				karmaText.setText("Mio KARMA: " + karmaUtente);

				topLevelLayout.setVisibility(View.INVISIBLE);
				pw.stopSpinning();

			}// End postExecute

		}// End class + AsyncTask

		new DownloadInterventi().execute();

		return rootView;

	}// End method
}
