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
import android.widget.Button;
import android.widget.EditText;

public class AssistenzaFragmentSix extends Fragment {

	View rootView;

	SharedPreferences sharedPref;
	Editor editor;

	public AssistenzaFragmentSix() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getActivity()
						.getApplicationContext());

		editor = sharedPref.edit();

		rootView = inflater.inflate(R.layout.fragment_assistenza_six,
				container, false);

		final EditText editMessaggio = (EditText) rootView
				.findViewById(R.id.editTextMessaggio);

		editMessaggio.setText(sharedPref.getString("assistenzaMessaggio", ""));

		Button buttonNext = (Button) rootView
				.findViewById(R.id.assistenzaSixButtonNext);

		buttonNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View view) {

				String messaggio = "";
				if (editMessaggio.getText() != null) {

					messaggio = editMessaggio.getText().toString();

				}

				if (!messaggio.isEmpty()) {

					messaggio.trim();

					if (messaggio.length() < 30) {

						GeneralFunction.messageBox(
								"Serve una descrizione più accurata!",
								getActivity());

					} else if (messaggio.length() > 399) {
						
						GeneralFunction.messageBox(
								"Messaggio troppo lungo!",
								getActivity());

					} else {

						editor.putString("assistenzaMessaggio", messaggio)
								.commit();
						Log.v("assistenza",
								"Memorizzo assistenzaMessaggio ---> "
										+ messaggio);
						
						InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
								.getSystemService(Activity.INPUT_METHOD_SERVICE);
						inputMethodManager.hideSoftInputFromWindow(getActivity()
								.getCurrentFocus().getWindowToken(), 0);
						
						AssistenzaFragmentRiepilog nextFragment = new AssistenzaFragmentRiepilog();
						FragmentTransaction transaction = getFragmentManager()
								.beginTransaction();
						transaction.replace(R.id.frame_container, nextFragment);
						transaction.commit();
					}

				} else {

					GeneralFunction.messageBox("Devi compilare il messaggio!",
							getActivity());

				}

			}
		});

		Button buttonBack = (Button) rootView
				.findViewById(R.id.assistenzaSixButtonBack);

		buttonBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View view) {

				editor.putString("assistenzaMessaggio",
						editMessaggio.getText().toString()).commit();

				AssistenzaFragmentFive nextFragment = new AssistenzaFragmentFive();
				FragmentTransaction transaction = getFragmentManager()
						.beginTransaction();
				transaction.replace(R.id.frame_container, nextFragment);
				transaction.commit();

			}
		});

		return rootView;

	}// End method
}
