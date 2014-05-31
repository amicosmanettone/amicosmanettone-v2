package it.amicosmanettone.assistenza.assistenzaitalia;

import it.amicosmanettone.assistenza.assistenzaitalia.R;
import it.amicosmanettone.assistenza.assistenzaitalia.util.GeneralFunction;
import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AssistenzaFragmentFour extends Fragment {

	View rootView;

	SharedPreferences sharedPref;
	Editor editor;

	public AssistenzaFragmentFour() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getActivity()
						.getApplicationContext());

		editor = sharedPref.edit();

		rootView = inflater.inflate(R.layout.fragment_assistenza_four,
				container, false);

		final Spinner spinnerRegioni = (Spinner) rootView
				.findViewById(R.id.assistenzaSpinnerRegioni);
		ArrayAdapter<CharSequence> adapterRegioni = ArrayAdapter
				.createFromResource(getActivity().getApplicationContext(),
						R.array.regioniItalia, R.layout.spinner_item);
		adapterRegioni.setDropDownViewResource(R.layout.spinner_item);
		spinnerRegioni.setAdapter(adapterRegioni);

		final Spinner spinnerProvince = (Spinner) rootView
				.findViewById(R.id.assistenzaSpinnerProvince);

		final EditText editLocalita = (EditText) rootView
				.findViewById(R.id.editTextLocalita);
		
		editLocalita.setText(sharedPref.getString("assistenzaLocalita", ""));

		spinnerRegioni.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapter, View view,
					int pos, long id) {
				String selected = (String) adapter.getItemAtPosition(pos)
						.toString();
				
				if(selected.equals("Valle d'Aosta")){
					
					selected = selected.replace("Valle d'Aosta", "Valle Aosta");
					
				}

				selected = selected.replace(" ", "_");

				int resID = getResources().getIdentifier(
						"it.amicosmanettone:array/" + selected, null, null);

				if (resID > 0) {

					ArrayAdapter<CharSequence> adapterProvince = ArrayAdapter
							.createFromResource(getActivity()
									.getApplicationContext(), resID,
									R.layout.spinner_item);
					adapterProvince
							.setDropDownViewResource(R.layout.spinner_item);
					spinnerProvince.setAdapter(adapterProvince);
				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		Button buttonNext = (Button) rootView
				.findViewById(R.id.assistenzaFourButtonNext);

		buttonNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View view) {

				int erroreRegione = 1;
				int erroreProvincia = 1;
				int erroreLocalita = 1;

				String regione = spinnerRegioni.getSelectedItem().toString();
				String provincia = "";
				if (!regione.isEmpty()) {

					erroreRegione = 0;
					provincia = spinnerProvince.getSelectedItem().toString();
					if (!provincia.isEmpty()) {

						erroreProvincia = 0;

					}

				}

				String localita = "";
				if (editLocalita.getText() != null) {

					localita = editLocalita.getText().toString();
					if (!localita.isEmpty()) {

						erroreLocalita = 0;

					}

				}

				if (erroreRegione == 0 && erroreProvincia == 0
						&& erroreLocalita == 0) {
					
					localita.trim();
					
					if (localita.length() < 4) {
						
						GeneralFunction.messageBox("La località deve avere minimo 4 caratteri !", getActivity());

					} else {
						
						editor.putString("assistenzaRegione", regione).commit();
						editor.putString("assistenzaProvincia", provincia).commit();
						editor.putString("assistenzaLocalita", localita).commit();
						
						Log.v("assistenza","Memorizzo assistenzaRegione ---> " + regione);
						Log.v("assistenza","Memorizzo assistenzaProvincia ---> " + provincia);
						Log.v("assistenza","Memorizzo assistenzaLocalita ---> " + localita);
						
						InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
								.getSystemService(Activity.INPUT_METHOD_SERVICE);
						inputMethodManager.hideSoftInputFromWindow(getActivity()
								.getCurrentFocus().getWindowToken(), 0);

						AssistenzaFragmentFive nextFragment = new AssistenzaFragmentFive();
						FragmentTransaction transaction = getFragmentManager()
								.beginTransaction();
						transaction.replace(R.id.frame_container, nextFragment);
						transaction.commit();

					}

				} else {

					GeneralFunction.messageBox("Ci sono campi vuoti!", getActivity());

				}

			}
		});

		Button buttonBack = (Button) rootView
				.findViewById(R.id.assistenzaFourButtonBack);

		buttonBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View view) {

				editor.putString("assistenzaLocalita",
						editLocalita.getText().toString()).commit();

				AssistenzaFragmentThree nextFragment = new AssistenzaFragmentThree();
				FragmentTransaction transaction = getFragmentManager()
						.beginTransaction();
				transaction.replace(R.id.frame_container, nextFragment);
				transaction.commit();

			}
		});

		return rootView;

	}// End method
}
