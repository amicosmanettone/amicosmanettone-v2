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

public class AssistenzaFragmentTwo extends Fragment {

	View rootView;

	SharedPreferences sharedPref;
	Editor editor;

	public AssistenzaFragmentTwo() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getActivity()
						.getApplicationContext());

		editor = sharedPref.edit();

		rootView = inflater.inflate(R.layout.fragment_assistenza_two,
				container, false);

		final EditText editName = (EditText) rootView
				.findViewById(R.id.editTextName);

		editName.setText(sharedPref.getString("assistenzaNome", ""));

		final EditText editMail = (EditText) rootView
				.findViewById(R.id.editTextEmail);

		editMail.setText(sharedPref.getString("assistenzaMail", ""));

		Button buttonNext = (Button) rootView
				.findViewById(R.id.assistenzaTwoButtonNext);

		buttonNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View view) {

				int erroreNome = 1;
				int erroreMail = 1;

				String nome = "";

				if (editName.getText() != null) {

					nome = editName.getText().toString();
					if (!nome.isEmpty()) {

						erroreNome = 0;

					}

				}

				String mail = "";

				if (editMail.getText() != null) {

					mail = editMail.getText().toString();
					if (!mail.isEmpty()) {

						erroreMail = 0;

					}

				}

				if (erroreNome == 0 && erroreMail == 0) {

					nome.trim();
					mail.trim();

					if (isValidEmail(mail) && nome.length() > 2) {

						editor.putString("assistenzaNome", nome).commit();
						editor.putString("assistenzaMail", mail).commit();
						Log.v("assistenza","Memorizzo nome --> " + nome);
						Log.v("assistenza","Memorizzo mail --> " + mail);
						
						InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
								.getSystemService(Activity.INPUT_METHOD_SERVICE);
						inputMethodManager.hideSoftInputFromWindow(getActivity()
								.getCurrentFocus().getWindowToken(), 0);

						AssistenzaFragmentThree nextFragment = new AssistenzaFragmentThree();
						FragmentTransaction transaction = getFragmentManager()
								.beginTransaction();
						transaction.replace(R.id.frame_container, nextFragment);
						transaction.commit();

					} else {

						GeneralFunction.messageBox("Dati errati!\r\nIl nome deve essere di minimo 3 caratteri e la mail deve essere valida!", getActivity());

					}

				} else {

					GeneralFunction.messageBox("Ci sono campi vuoti!", getActivity());

				}

			}
		});

		Button buttonBack = (Button) rootView
				.findViewById(R.id.assistenzaTwoButtonBack);

		buttonBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View view) {

				editor.putString("assistenzaNome",
						editName.getText().toString()).commit();
				editor.putString("assistenzaMail",
						editMail.getText().toString()).commit();

				AssistenzaFragment nextFragment = new AssistenzaFragment();
				FragmentTransaction transaction = getFragmentManager()
						.beginTransaction();
				transaction.replace(R.id.frame_container, nextFragment);
				transaction.commit();

			}
		});

		return rootView;

	}// End method

	public final static boolean isValidEmail(CharSequence mail) {
		if (mail == null) {

			return false;

		} else {

			return android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches();

		}
	}
}
