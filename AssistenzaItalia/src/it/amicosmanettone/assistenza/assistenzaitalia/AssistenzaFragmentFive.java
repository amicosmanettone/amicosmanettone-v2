package it.amicosmanettone.assistenza.assistenzaitalia;

import it.amicosmanettone.assistenza.assistenzaitalia.R;
import it.amicosmanettone.assistenza.assistenzaitalia.util.GeneralFunction;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class AssistenzaFragmentFive extends Fragment {

	View rootView;

	SharedPreferences sharedPref;
	Editor editor;

	public AssistenzaFragmentFive() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getActivity()
						.getApplicationContext());

		editor = sharedPref.edit();

		rootView = inflater.inflate(R.layout.fragment_assistenza_five,
				container, false);

		final Spinner spinnerProblema = (Spinner) rootView
				.findViewById(R.id.assistenzaSpinnerProblema);
		ArrayAdapter<CharSequence> adapterProblema = ArrayAdapter
				.createFromResource(getActivity().getApplicationContext(),
						R.array.problemi, R.layout.spinner_item);
		adapterProblema.setDropDownViewResource(R.layout.spinner_item);
		spinnerProblema.setAdapter(adapterProblema);

		final Spinner spinnerInternet = (Spinner) rootView
				.findViewById(R.id.assistenzaSpinnerInternet);
		ArrayAdapter<CharSequence> adapterInternet = ArrayAdapter
				.createFromResource(getActivity().getApplicationContext(),
						R.array.internet, R.layout.spinner_item);
		adapterInternet.setDropDownViewResource(R.layout.spinner_item);
		spinnerInternet.setAdapter(adapterInternet);

		Button buttonNext = (Button) rootView
				.findViewById(R.id.assistenzaFiveButtonNext);

		buttonNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View view) {

				int erroreProblema = 1;
				int erroreInternet = 1;
				
				String problema = spinnerProblema.getSelectedItem().toString();
				if (!problema.isEmpty()) {

					erroreProblema = 0;

				}
				
				String internet = spinnerInternet.getSelectedItem().toString();
				if (!internet.isEmpty()) {

					erroreInternet = 0;

				}

				if (erroreProblema == 0 && erroreInternet == 0) {
					
					editor.putString("assistenzaProblema", problema).commit();
					editor.putString("assistenzaInternet", internet).commit();
					
					Log.v("assistenza","Memorizzo assistenzaProblema ---> " + problema);
					Log.v("assistenza","Memorizzo assistenzaInternet ---> " + internet);

					AssistenzaFragmentSix nextFragment = new AssistenzaFragmentSix();
					FragmentTransaction transaction = getFragmentManager()
							.beginTransaction();
					transaction.replace(R.id.frame_container, nextFragment);
					transaction.commit();
					
				} else {

					GeneralFunction.messageBox("Ci sono dei campi vuoti!",
							getActivity());

				}

			}
		});

		Button buttonBack = (Button) rootView
				.findViewById(R.id.assistenzaFiveButtonBack);

		buttonBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View view) {

				AssistenzaFragmentFour nextFragment = new AssistenzaFragmentFour();
				FragmentTransaction transaction = getFragmentManager()
						.beginTransaction();
				transaction.replace(R.id.frame_container, nextFragment);
				transaction.commit();

			}
		});

		return rootView;

	}// End method
}
