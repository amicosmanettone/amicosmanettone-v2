package it.amicosmanettone.assistenza.assistenzaitalia;

import it.amicosmanettone.assistenza.assistenzaitalia.R;
import it.amicosmanettone.assistenza.assistenzaitalia.mysql.UserFunctions;
import it.amicosmanettone.assistenza.assistenzaitalia.util.GeneralFunction;

import org.json.JSONObject;

import com.todddavies.components.progressbar.ProgressWheel;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AssistenzaFragmentSend extends Fragment {

	View rootView;

	SharedPreferences sharedPref;
	Editor editor;

	public AssistenzaFragmentSend() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getActivity()
						.getApplicationContext());

		editor = sharedPref.edit();

		editor.putInt("lastViewVisited", 2).commit();

		rootView = inflater.inflate(R.layout.fragment_assistenza_send,
				container, false);

		final ProgressWheel pw = (ProgressWheel) rootView
				.findViewById(R.id.assistenza_spinner);

		pw.spin();

		class SendRichiesta extends AsyncTask<Void, Void, Boolean> {

			@Override
			protected Boolean doInBackground(Void... params) {

				UserFunctions userFunction = new UserFunctions();
				JSONObject sendRichiesta = userFunction
						.sendRichiestaAssistenza(getActivity());
				
				JSONObject incrementaNumRichieste = userFunction
						.incrementRichieste(sharedPref.getString("registrationId",
								""));

				return true;

			}// End doInBackground...

			protected void onPostExecute(Boolean result) {
				
				if(sharedPref.getBoolean("rememberModule", true) == false){
					
					Log.v("PREFERENCE","Elimino dati del modulo");
					
					editor.remove("assistenzaThisDevice").commit();
					editor.remove("assistenzaNome").commit();
					editor.remove("assistenzaMail").commit();
					editor.remove("assistenzaTel1").commit();
					editor.remove("assistenzaTel2").commit();
					editor.remove("assistenzaRegione").commit();
					editor.remove("assistenzaProvincia").commit();
					editor.remove("assistenzaLocalita").commit();
					editor.remove("assistenzaProblema").commit();
					editor.remove("assistenzaInternet").commit();
					editor.remove("assistenzaMessaggio").commit();
					
				}

				GeneralFunction.messageBox(
						"Richiesta inviata!\nVerrai contattato al più presto!",
						getActivity());

				AssistenzaFragment nextFragment = new AssistenzaFragment();
				FragmentTransaction transaction = getFragmentManager()
						.beginTransaction();
				transaction.replace(R.id.frame_container, nextFragment);
				transaction.commit();

			}// End postExecute

		}// End class + AsyncTask

		if (GeneralFunction.verifyConnection(getActivity()
				.getApplicationContext())) {

			new SendRichiesta().execute();
			
		} else {

			GeneralFunction
					.messageBox(
							"Non cè connessione!\nIl modulo è stato salvato.\nRiprova più tardi.",
							getActivity());
			
			AssistenzaFragment nextFragment = new AssistenzaFragment();
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.frame_container, nextFragment);
			transaction.commit();

		}

		return rootView;

	}// End method
}
