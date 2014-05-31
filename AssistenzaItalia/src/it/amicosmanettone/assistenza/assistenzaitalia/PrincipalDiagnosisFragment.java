package it.amicosmanettone.assistenza.assistenzaitalia;

import java.util.ArrayList;
import java.util.HashMap;

import it.amicosmanettone.assistenza.assistenzaitalia.R;
import it.amicosmanettone.assistenza.assistenzaitalia.mysql.UserFunctions;
import it.amicosmanettone.assistenza.assistenzaitalia.util.ReadDiagnosis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class PrincipalDiagnosisFragment extends Fragment {

	View rootView;

	SharedPreferences sharedPref;
	Editor editor;

	ListView listaCategoriePrincipali;
	TextView textTitle;

	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

	public PrincipalDiagnosisFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getActivity()
						.getApplicationContext());

		editor = sharedPref.edit();

		editor.putInt("lastViewVisited", 3).commit();

		rootView = inflater.inflate(R.layout.fragment_diagnosi, container,
				false);

		textTitle = (TextView) rootView.findViewById(R.id.textTitleDiagnosis);
		listaCategoriePrincipali = (ListView) rootView
				.findViewById(R.id.list_diagnosis_categories);

		String[] from = new String[] { "nome" };
		int[] to = new int[] { R.id.diagnosis_category_title };

		SimpleAdapter adapter = new SimpleAdapter(getActivity()
				.getApplicationContext(), list,
				R.layout.rigadiagnosiprincipale, from, to);

		scriviCategorie();

		listaCategoriePrincipali.setAdapter(adapter);

		listaCategoriePrincipali
				.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						
						String figlio = list.get(position).get("nomeFiglio");

						Log.v("DIAGNOSI",
								"FIGLIO ------>   "
										+ figlio);
						
						if (figlio != null) {
							
							Bundle data = new Bundle();
							data.putString("json", figlio);
							

							Fragment childrenFragment = new ChildrenDiagnosisFragment();
							childrenFragment.setArguments(data);
							FragmentManager fragmentManager = getFragmentManager();
							fragmentManager
									.beginTransaction()
									.replace(R.id.frame_container,
											childrenFragment).commit();

						}

					}

				});

		return rootView;

	}// End method

	public void scriviCategorie() {

		JSONObject jsonCategorie = ReadDiagnosis.readCategory(
				getResources(), R.raw.lista_categorie_principali);

		if (jsonCategorie != null) {

			String version = null;
			String title = null;

			try {

				version = jsonCategorie.getString("version");
				title = jsonCategorie.getString("title");

			} catch (JSONException e) {

				e.printStackTrace();
			} finally {

				textTitle.setText(title);

				try {
					UserFunctions userFunction = new UserFunctions();
					JSONObject json = userFunction
							.checkDiagnosisVersion(version);
					if (json != null) {

						if (Integer.parseInt(json.getString("success")) != 1) {

							Log.v("DIAGNOSI", "VERSIONE PIU' RECENTE TROVATA");

						} else {

							Log.v("DIAGNOSI", "HAI LA VERSIONE PIU' RECENTE");

						}

					}
				} catch (NumberFormatException e) {

					e.printStackTrace();
				} catch (JSONException e) {

					e.printStackTrace();
				}

				JSONArray categorie = null;
				try {

					categorie = jsonCategorie.getJSONArray("categorie");

				} catch (JSONException e) {

					e.printStackTrace();
				}

				Log.v("DIAGNOSI", "NUMERO CATEGORIE ------>   " + categorie.length());

				for (int i = 0; i < categorie.length(); i++) {

					try {

						String categoriaSingola = categorie.get(i).toString();

						Log.v("DIAGNOSI", "Categoria singola ------>   "
								+ categoriaSingola);

						JSONObject jsonCategoriaSingola = new JSONObject(
								categoriaSingola);

						String nomeCategoria = jsonCategoriaSingola
								.getString("nome");

						String haFigli = jsonCategoriaSingola
								.getString("figli");

						if (haFigli.equals("si")) {

							Log.v("DIAGNOSI", "La categoria \"" + nomeCategoria
									+ "\" ha un figlio");

							String nextJson = jsonCategoriaSingola
									.getString("categoriaFiglio");

							HashMap<String, String> map = new HashMap<String, String>();

							map.put("nome", nomeCategoria);
							map.put("nomeFiglio", nextJson);

							list.add(map);

							Log.v("DIAGNOSI",
									"Figlio della categoria ------>   "
											+ nextJson);

						}//End if figli != null
						else {

							Log.v("DIAGNOSI", "La categoria \"" + nomeCategoria
									+ "\" non ha un figlio");

							HashMap<String, String> map = new HashMap<String, String>();

							map.put("nome", nomeCategoria);
							map.put("nomeFiglio", null);
							list.add(map);

						}

					} catch (JSONException e) {

						e.printStackTrace();
					}

				}//End for

			}//End finally
		}//End if json != null
	}//End method
}//End class
