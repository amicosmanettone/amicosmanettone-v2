package it.amicosmanettone.assistenza.assistenzaitalia;

import java.util.ArrayList;
import java.util.HashMap;

import it.amicosmanettone.assistenza.assistenzaitalia.R;
import it.amicosmanettone.assistenza.assistenzaitalia.util.ReadDiagnosis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ChildrenDiagnosisFragment extends Fragment {

	View rootView;
	
	ListView listaCategorie;
	TextView textTitle;
	
	String json;
	
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

	public ChildrenDiagnosisFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		Bundle data = getArguments();
		json = data.getString("json");
		
		Log.v("CHILDREN DIAGNOSIS","JSON RECUPERATO ----->" + json);

		rootView = inflater.inflate(R.layout.fragment_diagnosi, container,
				false);
		
		textTitle = (TextView)rootView.findViewById(R.id.textTitleDiagnosis);
		listaCategorie = (ListView)rootView.findViewById(R.id.list_diagnosis_categories);
		
		String[] from = new String[] {"nome"};
		int[] to = new int[] { R.id.diagnosis_category_title};
		
		SimpleAdapter adapter = new SimpleAdapter(getActivity().getApplicationContext(), list, R.layout.rigadiagnosiprincipale, from, to);
		
		scriviCategorie();
		
		listaCategorie.setAdapter(adapter);
		
		listaCategorie.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view,
		        int position, long id) { 
		    	
		    	
		    	
		    	Log.v("DIAGNOSI",
						"CLICCO SU ------>   " + list.get(position).get("nome"));
		    	
		    }
		  
		});

		return rootView;

	}// End method
	
	public void scriviCategorie() {

		JSONObject jsonCategorie = ReadDiagnosis.readCategory(
				getResources(), getResources().getIdentifier(json, 
						"raw", getActivity().getPackageName()));

		if (jsonCategorie != null) {

			String title = null;

			try {

				title = jsonCategorie.getString("title");

			} catch (JSONException e) {

				e.printStackTrace();
			} finally {

				textTitle.setText(title);

				JSONArray categorie = null;
				try {

					categorie = jsonCategorie.getJSONArray("categorie");

				} catch (JSONException e) {

					e.printStackTrace();
				}

				Log.v("DIAGNOSI CHILDREN", "NUMERO CATEGORIE ------>   " + categorie.length());

				for (int i = 0; i < categorie.length(); i++) {

					try {

						String categoriaSingola = categorie.get(i).toString();

						Log.v("DIAGNOSI CHILDREN", "Categoria singola ------>   "
								+ categoriaSingola);

						JSONObject jsonCategoriaSingola = new JSONObject(
								categoriaSingola);

						String nomeCategoria = jsonCategoriaSingola
								.getString("nome");

						String haFigli = jsonCategoriaSingola
								.getString("figli");

						if (haFigli.equals("si")) {

							Log.v("DIAGNOSI CHILDREN", "La categoria \"" + nomeCategoria
									+ "\" ha un figlio");

							String nextJson = jsonCategoriaSingola
									.getString("categoriaFiglio");

							HashMap<String, String> map = new HashMap<String, String>();

							map.put("nome", nomeCategoria);
							map.put("nomeFiglio", nextJson);

							list.add(map);

							Log.v("DIAGNOSI CHILDREN",
									"Figlio della categoria ------>   "
											+ nextJson);

						}//End if figli != null
						else {

							Log.v("DIAGNOSI CHILDREN", "La categoria \"" + nomeCategoria
									+ "\" non ha un figlio");

							HashMap<String, String> map = new HashMap<String, String>();

							map.put("nome", nomeCategoria);
							list.add(map);

						}

					} catch (JSONException e) {

						e.printStackTrace();
					}

				}//End for

			}//End finally
		}//End if json != null
	}//End method
}
