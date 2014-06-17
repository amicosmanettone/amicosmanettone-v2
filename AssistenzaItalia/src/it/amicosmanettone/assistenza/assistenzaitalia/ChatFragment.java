package it.amicosmanettone.assistenza.assistenzaitalia;

import it.amicosmanettone.assistenza.assistenzaitalia.R;
import it.amicosmanettone.assistenza.assistenzaitalia.chat.ChatListAdapter;
import it.amicosmanettone.assistenza.assistenzaitalia.chat.ChatXMLParser;
import it.amicosmanettone.assistenza.assistenzaitalia.chat.ChatXmlBean;
import it.amicosmanettone.assistenza.assistenzaitalia.chat.SendData;
import it.amicosmanettone.assistenza.assistenzaitalia.configuration.Configuration;
import it.amicosmanettone.assistenza.assistenzaitalia.mysql.UserFunctions;
import it.amicosmanettone.assistenza.assistenzaitalia.util.GeneralFunction;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import com.todddavies.components.progressbar.ProgressWheel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class ChatFragment extends Fragment {

	static LinkedList list = new LinkedList();

	ListView listView = null;

	String UTENTE;

	ChatListAdapter adapter = null;

	View rootView;

	View topLevelLayout;
	View layoutSend;

	SharedPreferences sharedPref;
	Editor editor;

	ProgressWheel pw;
	EditText messaggioSend;
	ImageButton buttonSend;
	String messaggio = "";

	Timer timer = new Timer();

	ChatXmlBean mess;

	public ChatFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.rootView = inflater.inflate(R.layout.fragment_chat, container,
				false);

		this.topLevelLayout = rootView.findViewById(R.id.chat_top_layout);
		this.layoutSend = rootView.findViewById(R.id.chatLinerSend);

		this.pw = (ProgressWheel) rootView.findViewById(R.id.chat_spinner);
		this.messaggioSend = (EditText) rootView
				.findViewById(R.id.testoMessaggioInvia);
		this.buttonSend = (ImageButton) rootView.findViewById(R.id.buttonSend);

		this.sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getActivity()
						.getApplicationContext());

		this.editor = sharedPref.edit();

		this.editor.putInt("lastViewVisited", 1).commit();

		this.editor.putBoolean("chatIsActive", true).commit();

		this.listView = (ListView) rootView.findViewById(R.id.listChat);
		
		if(sharedPref.getBoolean("isAdministrator", false) == true){
			
			registerForContextMenu(listView);
			
		}		

		UTENTE = sharedPref.getString("usernameUtente", "");

		Log.v("CHAT", "Mi chiamo ----> " + UTENTE);

		if (GeneralFunction.verifyConnection(getActivity()
				.getApplicationContext())) {

			Log.i("CHAT", "Cè connessione");

			// Funzione che attiva internet nel mainThread
			GeneralFunction.activateStrictMode(false);

			new DownloadChat().execute();

			if (!sharedPref.getString("memoryMessage", "").equals("")) {

				messaggioSend
						.setText(sharedPref.getString("memoryMessage", ""));

			}

			buttonSend.setOnClickListener(new View.OnClickListener() {
				public void onClick(final View view) {

					int errore = 1;

					if (messaggioSend.getText() != null) {
						messaggio = messaggioSend.getText().toString();
						if (!messaggio.isEmpty()) {
							errore = 0;
						}
					}

					if (errore == 0) {

						messaggio = messaggio.replace("à", "a'");
						messaggio = messaggio.replace("á", "a'");
						messaggio = messaggio.replace("è", "e'");
						messaggio = messaggio.replace("é", "e'");
						messaggio = messaggio.replace("ì", "i'");
						messaggio = messaggio.replace("í", "i'");
						messaggio = messaggio.replace("ò", "o'");
						messaggio = messaggio.replace("ó", "o'");
						messaggio = messaggio.replace("ù", "u'");
						messaggio = messaggio.replace("ú", "u'");

						Log.v("CHAT", "Messaggio da inviare: " + messaggio);

						if (GeneralFunction.verifyConnection(getActivity()
								.getApplicationContext())) {

							new SendMessage().execute();

						} else {

							editor.putString("memoryMessage", messaggio)
									.commit();

							ChatFragment refreshFragment = new ChatFragment();
							FragmentTransaction transaction = getFragmentManager()
									.beginTransaction();
							transaction.replace(R.id.frame_container,
									refreshFragment);
							transaction.commit();
							messageBox("Messaggio salvato!");

						}

					}// End if no error
					else {

						messageBox("Inserire il messaggio da inviare!");

					}
				}

			});// End onClick

			Integer updateTime = Integer.valueOf(sharedPref.getString(
					"updateChatSecond", "20")) * 1000;
			Log.v("CHAT", "Aggiorno ogni ---> " + updateTime / 1000
					+ " secondi / " + updateTime + " millisecondi");

			/* OGNI TOT DI TEMPO VERIFICO LA CHAT */
			timer.scheduleAtFixedRate(new TimerTask() {
				public void run() {

					Boolean firstRun = sharedPref.getBoolean("firstChatStart",
							true);

					if (firstRun) {

						editor.putBoolean("firstChatStart", false).commit();

					} else {

						if (!GeneralFunction.verifyConnection(getActivity()
								.getApplicationContext())) {

							Log.v("CHAT",
									"Verifica di nuovi messaggi non riuscita, non cè connessione");

							if (!messaggioSend.getText().toString().isEmpty()) {

								Log.v("CHAT",
										"Cè un messaggio in corso da salvare ---> "
												+ messaggioSend.getText()
														.toString());

								editor.putString("memoryMessage",
										messaggioSend.getText().toString())
										.commit();

								getActivity().runOnUiThread(new Runnable() {
									public void run() {

										messageBox("Messaggio salvato!");

									}
								});

							}

							ChatFragment refreshFragment = new ChatFragment();
							FragmentTransaction transaction = getFragmentManager()
									.beginTransaction();
							transaction.replace(R.id.frame_container,
									refreshFragment);
							transaction.commit();

						} else {

							Log.v("CHAT", "Verifico nuovi messaggi");
							new UpdateChat().execute();
							// new SendUserOnline().execute();

						}
					}
				}
			}, Configuration.delay, updateTime);

		}// End if there are Internet
		else {

			Log.v("CHAT", "Non cè connessione");

			messageBox("Non cè connessione!");
			pw.setText("Attendo connessione...");
			pw.spin();

			/* OGNI 10 SECONDI VERIFICO LA CONNESSIONE */
			timer.scheduleAtFixedRate(new TimerTask() {
				public void run() {

					Log.v("CHAT", "Cerco una connessione");

					if (GeneralFunction.verifyConnection(getActivity()
							.getApplicationContext())) {

						Log.v("CHAT", "Connessione trovata");

						ChatFragment refreshFragment = new ChatFragment();
						FragmentTransaction transaction = getFragmentManager()
								.beginTransaction();
						transaction.replace(R.id.frame_container,
								refreshFragment);
						transaction.commit();

					} else {

						Log.v("CHAT", "Connessione non trovata");
						Log.v("CHAT", "Prossimo tentativo fra 10 secondi");

					}

				}
			}, 100, 10000);

		}

		return rootView;
	}

	/* SCARICO LA CHAT LA PRIMA VOLTA */
	class DownloadChat extends AsyncTask<Void, Void, List> {

		@Override
		protected void onPreExecute() {

			pw.spin();

			topLevelLayout.setVisibility(View.VISIBLE);

		}

		@Override
		protected List doInBackground(Void... params) {

			List entry = null;

			ChatXMLParser ChatXMLParser = new ChatXMLParser();

			try {

				entry = ChatXMLParser.parse(getActivity()
						.getApplicationContext(), Configuration.CHAT_URL);

				Log.e("CHAT_LOG", "Totale chattate recuperate: " + entry.size());

			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return entry;

		}// End doInBackground...

		protected void onPostExecute(List entry) {

			list.clear();

			adapter = new ChatListAdapter(
					getActivity().getApplicationContext(), R.layout.rigachat,
					list);

			adapter.addAll(entry);

			listView.setSelection(adapter.getCount() - 1);

			listView.setAdapter(adapter);

			topLevelLayout.setVisibility(View.INVISIBLE);

			pw.stopSpinning();

			listView.setSelection(listView.getCount());

			editor.remove("memoryMessage").commit();

		}// End postExecute

	}// End class + AsyncTask

	/* VERIFICO LA CHAT E SE NECESSARIO L'AGGIORNO */
	class UpdateChat extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {

			Boolean update = false;

			ChatXMLParser ChatXMLParser = new ChatXMLParser();

			String lastMessageTRUE = null;
			String lastMessageFALSE = null;

			try {

				final List entry = ChatXMLParser.parse(getActivity()
						.getApplicationContext(), Configuration.CHAT_URL);

				Log.e("CHAT", "Messaggi recuperati: " + entry.size());

				lastMessageTRUE = sharedPref.getString("lastNumberMessageChat",
						"0");
				lastMessageFALSE = sharedPref.getString(
						"lastNumberMessageChatFALSE", "0");

				if (Integer.parseInt(lastMessageTRUE) > Integer
						.parseInt(lastMessageFALSE)) {

					Log.v("CHAT",
							"Ho trovato nuovi messaggi, sono: "
									+ (Integer.parseInt(lastMessageTRUE) - Integer
											.parseInt(lastMessageFALSE)));

					update = true;

				} else {

					Log.v("CHAT", "Non ci sono nuovi messaggi");

				}

			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				editor.putString("lastNumberMessageChatFALSE", lastMessageTRUE)
						.commit();
			}

			return update;
		}

		protected void onPostExecute(Boolean update) {

			if (update) {

				Log.v("CHAT", "Aggiorno chat");
				pw.setText("Aggiorno chat...");
				new DownloadChat().execute();

			}

		}

	}// End updateChat class

	/* INVIO MESSAGGIO ALLA CHAT */
	class SendMessage extends AsyncTask<Void, Void, Boolean> {

		HashMap<String, String> dati = new HashMap();

		@Override
		protected void onPreExecute() {

			InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
					.getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getActivity()
					.getCurrentFocus().getWindowToken(), 0);
			pw.spin();
			topLevelLayout.setVisibility(View.VISIBLE);
			messaggioSend.setText("");

		}

		@Override
		protected Boolean doInBackground(Void... params) {

			String token = String.valueOf(System.currentTimeMillis());
			// UserFunctions userFunction = new UserFunctions();

			dati.put("txt", messaggio);
			dati.put("token", token);
			dati.put("user", UTENTE);

			SendData sendMailThread = new SendData(Configuration.CHAT_URL_SEND,
					dati);
			sendMailThread.start();

			// JSONObject json = userFunction.sendChatNotification();

			return true;
		}

		protected void onPostExecute(Boolean result) {

			topLevelLayout.setVisibility(View.INVISIBLE);
			pw.stopSpinning();
			messageBox("Messaggio inviato!");
			new UpdateChat().execute();

		}

	}// End class

	/* INVIO MESSAGGIO ALLA CHAT */
	class SendUserOnline extends AsyncTask<Void, Void, HttpResponse> {

		@Override
		protected HttpResponse doInBackground(Void... params) {

			HttpResponse response = null;

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					Configuration.CHAT_URL_SENDUSERONLINE);

			try {

				httppost.setHeader("Cookie", "kide_config=name%3D" + UTENTE);

				response = httpclient.execute(httppost);

			} catch (ClientProtocolException e) {
				Log.v("CHAT", "Network exception --> " + e);
			} catch (IOException e) {
				Log.v("CHAT", "Network exception --> " + e);
			}

			return response;
		}

	}// End class

	public void messageBox(String mymessage) {
		new AlertDialog.Builder(getActivity())
				.setMessage(mymessage)
				.setCancelable(true)
				.setNeutralButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						}).show();
	}// End errorBox

	@Override
	public void onResume() {
		Log.v("CHAT", "Sto in onResume");

		super.onResume();
	}

	@Override
	public void onPause() {
		Log.v("CHAT", "Sto in onPause");

		editor.remove("chatIsActive").commit();

		super.onPause();
	}

	@Override
	public void onDestroyView() {

		Log.v("CHAT", "Sto in onDestroyView");
		timer.cancel();

		editor.remove("lastNumberMessageChatFALSE").commit();
		editor.remove("firstChatStart").commit();
		editor.remove("chatIsActive").commit();

		super.onDestroyView();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

		mess = (ChatXmlBean) list.get(info.position);

		menu.setHeaderTitle(mess.getUser());
		menu.add("Rimuovi messaggio");
		menu.add("Cancella tutta la chat");

	}

	public boolean onContextItemSelected(android.view.MenuItem item) {

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();

		if (item.getTitle().equals("Rimuovi messaggio")) {

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			builder.setMessage("Vuoi veramente cancellare il messaggio?")
					.setCancelable(false)

					.setPositiveButton("Si",
							new DialogInterface.OnClickListener() {
								
								public void onClick(DialogInterface dialog, int id) {
									
									Log.v("CHAT", "Rimuovo id ---> " + mess.getId());
									new deleteMessageChat().execute();
									
								}
							})

					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {

									dialog.cancel();

								}
							});

			AlertDialog alert = builder.create();
			alert.show();
			

		} else if (item.getTitle().equals("Cancella tutta la chat")) {
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			builder.setMessage("Vuoi veramente cancellare TUTTO?")
					.setCancelable(false)

					.setPositiveButton("Si",
							new DialogInterface.OnClickListener() {
								
								public void onClick(DialogInterface dialog, int id) {
									
									Log.v("CHAT", "Rimuovo " + list.size() + " messaggi");
									new deleteAllMessageChat().execute();
									
								}
							})

					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {

									dialog.cancel();

								}
							});

			AlertDialog alert = builder.create();
			alert.show();

		}

		return true;
	}

	/* ELIMINO MESSAGGIO DALLA CHAT */
	class deleteMessageChat extends AsyncTask<Void, Void, Boolean> {

		ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
		
		@Override
		protected void onPreExecute() {

			mProgressDialog.setMessage("Elimino messaggio...");
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();

		}

		@Override
		protected Boolean doInBackground(Void... params) {

			Boolean result = false;

			UserFunctions userFunction = new UserFunctions();

			JSONObject json = userFunction.deleteMessage(mess.getId());

			try {
				if (json != null) {
					if (Integer.parseInt(json.getString("success")) == 1) {

						Log.v("CHAT","Eliminato");
						result = true;

					}else{
						
						Log.v("CHAT","errore");
						
					}

				}// End if json != null
			}// End try
			catch (JSONException e) {

				e.printStackTrace();

			}// End catch

			return result;
		}

		protected void onPostExecute(Boolean result) {
			
			if(result){
				
				mProgressDialog.dismiss();
				editor.remove("lastNumberMessageChatFALSE").commit();
				new UpdateChat().execute();
				Toast.makeText(getActivity(), "Messaggio cancellato!", Toast.LENGTH_SHORT).show();
				
			}

		}

	}// End class
	
	/* ELIMINO TUTTI I MESSAGGIO DALLA CHAT */
	class deleteAllMessageChat extends AsyncTask<Void, Void, Boolean> {

		ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
		
		@Override
		protected void onPreExecute() {

			mProgressDialog.setMessage("Elimino " + list.size() + " messaggi...");
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();

		}

		@Override
		protected Boolean doInBackground(Void... params) {

			Boolean result = false;

			UserFunctions userFunction = new UserFunctions();

			JSONObject json = userFunction.deleteMessage("0");

			try {
				if (json != null) {
					if (Integer.parseInt(json.getString("success")) == 1) {

						Log.v("CHAT","Eliminati ---> " + list.size());
						result = true;

					}else{
						
						Log.v("CHAT","errore");
						
					}

				}// End if json != null
			}// End try
			catch (JSONException e) {

				e.printStackTrace();

			}// End catch

			return result;
		}

		protected void onPostExecute(Boolean result) {
			
			if(result){
				
				mProgressDialog.dismiss();
				editor.remove("lastNumberMessageChatFALSE").commit();
				new UpdateChat().execute();
				Toast.makeText(getActivity(), list.size() + " messaggi cancellati!", Toast.LENGTH_SHORT).show();
				
			}else{
				
				mProgressDialog.dismiss();
				Toast.makeText(getActivity(), "ERRORE!", Toast.LENGTH_SHORT).show();
				
			}

		}
	}

}
