package it.amicosmanettone.assistenza.assistenzaitalia;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import it.amicosmanettone.assistenza.assistenzaitalia.R;

public class AssistenzaFragmentRiepilog extends Fragment {

	View rootView;

	SharedPreferences sharedPref;
	Editor editor;

	public AssistenzaFragmentRiepilog() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getActivity()
						.getApplicationContext());

		editor = sharedPref.edit();
		rootView = inflater.inflate(R.layout.fragment_assistenza_riepilog, container,
				false);
		
		TextView textRiepilog = (TextView) rootView
				.findViewById(R.id.textRiepilog);
		
		String messaggio = sharedPref.getString("assistenzaMessaggio", "");
		
		if(sharedPref.getBoolean("assistenzaThisDevice", false) == true){
			
			messaggio = messaggio + "\n\n" + Build.MANUFACTURER + "\n" + Build.MODEL + "\n" + Build.CPU_ABI + "\n" + Build.VERSION.RELEASE;
			
		}
		
		textRiepilog.setText("Mi chiamo: " + sharedPref.getString("assistenzaNome", "")
				+ ";\nLa mia mail: " + sharedPref.getString("assistenzaMail", "")
				+ ";\nIl mio numero principale: " + sharedPref.getString("assistenzaTel1", "")
				+ ";\nIl mio numero secondario: " + sharedPref.getString("assistenzaTel2", "NON SETTATO")
				+ ";\nLa mia località: " + sharedPref.getString("assistenzaLocalita", "")
				+ ";\nIl mio problema: " + sharedPref.getString("assistenzaProblema", "")
				+ ";\nHo una connessione internet: " + sharedPref.getString("assistenzaInternet", "")
				+ ";\nMessaggio: " + messaggio + ";");

		Button buttonSend = (Button) rootView
				.findViewById(R.id.assistenzaButtonSend);

		buttonSend.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View view) {

				AssistenzaFragmentSend nextFragment = new AssistenzaFragmentSend();
				FragmentTransaction transaction = getFragmentManager()
						.beginTransaction();
				transaction.replace(R.id.frame_container, nextFragment);
				transaction.commit();


			}
		});

		Button buttonModify = (Button) rootView
				.findViewById(R.id.assistenzaButtonModify);

		buttonModify.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View view) {

				AssistenzaFragmentSix nextFragment = new AssistenzaFragmentSix();
				FragmentTransaction transaction = getFragmentManager()
						.beginTransaction();
				transaction.replace(R.id.frame_container, nextFragment);
				transaction.commit();

			}
		});

		return rootView;

	}// End method
}
