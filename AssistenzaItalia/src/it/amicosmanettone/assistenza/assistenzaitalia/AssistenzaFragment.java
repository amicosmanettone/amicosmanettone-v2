package it.amicosmanettone.assistenza.assistenzaitalia;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import it.amicosmanettone.assistenza.assistenzaitalia.R;

public class AssistenzaFragment extends Fragment {

	View rootView;

	SharedPreferences sharedPref;
	Editor editor;

	public AssistenzaFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getActivity()
						.getApplicationContext());

		editor = sharedPref.edit();

		editor.putInt("lastViewVisited", 2).commit();

		rootView = inflater.inflate(R.layout.fragment_assistenza, container,
				false);

		Button buttonDevice = (Button) rootView
				.findViewById(R.id.assistenzaButtonDevice);

		buttonDevice.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View view) {

				Log.v("ASS", "Setto assistenzaThisDevice su TRUE");
				editor.putBoolean("assistenzaThisDevice", true).commit();

				AssistenzaFragmentTwo nextFragment = new AssistenzaFragmentTwo();
				FragmentTransaction transaction = getFragmentManager()
						.beginTransaction();
				transaction.replace(R.id.frame_container, nextFragment);
				transaction.commit();

			}
		});

		Button buttonOther = (Button) rootView
				.findViewById(R.id.assistenzaButtonOther);

		buttonOther.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View view) {

				Log.v("ASS", "Setto assistenzaThisDevice su FALSE");
				editor.putBoolean("assistenzaThisDevice", false).commit();

				AssistenzaFragmentTwo nextFragment = new AssistenzaFragmentTwo();
				FragmentTransaction transaction = getFragmentManager()
						.beginTransaction();
				transaction.replace(R.id.frame_container, nextFragment);
				transaction.commit();

			}
		});

		TextView resetModule = (TextView) rootView
				.findViewById(R.id.textResetModule);
		
		resetModule.setPaintFlags(resetModule.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

		resetModule.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View view) {

				AlertDialog.Builder alert = new AlertDialog.Builder(
						getActivity());
				alert.setMessage("Vuoi cancellare tutti i dati del modulo?");
				alert.setCancelable(false);

				alert.setPositiveButton("Si",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

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
						});

				alert.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								dialog.cancel();

							}

						});

				AlertDialog alertDialog = alert.create();
				alertDialog.show();

			}
		});

		return rootView;

	}// End method
}
