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

public class AssistenzaFragmentThree extends Fragment {

	View rootView;

	SharedPreferences sharedPref;
	Editor editor;

	public AssistenzaFragmentThree() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getActivity()
						.getApplicationContext());

		editor = sharedPref.edit();

		rootView = inflater.inflate(R.layout.fragment_assistenza_three,
				container, false);

		Button buttonNext = (Button) rootView
				.findViewById(R.id.assistenzaThreeButtonNext);

		final EditText editTel1 = (EditText) rootView
				.findViewById(R.id.editTextTelephone);

		editTel1.setText(sharedPref.getString("assistenzaTel1", ""));

		final EditText editTel2 = (EditText) rootView
				.findViewById(R.id.editTextTelephone2);

		editTel2.setText(sharedPref.getString("assistenzaTel2", ""));

		buttonNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View view) {

				int erroreTel = 1;

				String tel1 = "";

				if (editTel1.getText() != null) {

					tel1 = editTel1.getText().toString();
					if (!tel1.isEmpty()) {

						erroreTel = 0;

					}

				}

				if (erroreTel == 0) {

					tel1.trim();

					editor.putString("assistenzaTel1", tel1).commit();
					Log.v("assistenza", "Memorizzo telefono 1 --> " + tel1);

					if (editTel2.getText() != null) {

						String tel2 = editTel2.getText().toString();
						if (!tel2.isEmpty()) {

							tel2.trim();

							editor.putString("assistenzaTel2", tel2).commit();
							Log.v("assistenza", "Memorizzo telefono 2 --> "
									+ tel2);
							
							InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
									.getSystemService(Activity.INPUT_METHOD_SERVICE);
							inputMethodManager.hideSoftInputFromWindow(getActivity()
									.getCurrentFocus().getWindowToken(), 0);

							AssistenzaFragmentFour nextFragment = new AssistenzaFragmentFour();
							FragmentTransaction transaction = getFragmentManager()
									.beginTransaction();
							transaction.replace(R.id.frame_container,
									nextFragment);
							transaction.commit();

						} else {
							
							InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
									.getSystemService(Activity.INPUT_METHOD_SERVICE);
							inputMethodManager.hideSoftInputFromWindow(getActivity()
									.getCurrentFocus().getWindowToken(), 0);

							AssistenzaFragmentFour nextFragment = new AssistenzaFragmentFour();
							FragmentTransaction transaction = getFragmentManager()
									.beginTransaction();
							transaction.replace(R.id.frame_container,
									nextFragment);
							transaction.commit();

						}
					}

				} else {

					GeneralFunction.messageBox(
							"Il primo campo è obbligatorio!", getActivity());

				}

			}
		});

		Button buttonBack = (Button) rootView
				.findViewById(R.id.assistenzaThreeButtonBack);

		buttonBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View view) {

				editor.putString("assistenzaTel1",
						editTel1.getText().toString()).commit();
				editor.putString("assistenzaTel2",
						editTel2.getText().toString()).commit();

				AssistenzaFragmentTwo nextFragment = new AssistenzaFragmentTwo();
				FragmentTransaction transaction = getFragmentManager()
						.beginTransaction();
				transaction.replace(R.id.frame_container, nextFragment);
				transaction.commit();

			}
		});

		return rootView;

	}// End method

}
