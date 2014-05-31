package it.amicosmanettone.assistenza.assistenzaitalia;

import java.io.File;
import java.util.Random;

import it.amicosmanettone.assistenza.assistenzaitalia.R;
import it.amicosmanettone.assistenza.assistenzaitalia.configuration.Configuration;
import it.amicosmanettone.assistenza.assistenzaitalia.mysql.DownloadBitmap;
import it.amicosmanettone.assistenza.assistenzaitalia.mysql.UserFunctions;
import it.amicosmanettone.assistenza.assistenzaitalia.util.GeneralFunction;
import it.amicosmanettone.assistenza.assistenzaitalia.util.Image;

import org.json.JSONException;
import org.json.JSONObject;

import com.todddavies.components.progressbar.ProgressWheel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class HomeFragment extends Fragment {

	View rootView;

	public HomeFragment() {
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		GeneralFunction.activateStrictMode(false);

		final SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getActivity()
						.getApplicationContext());
		final Editor editor = sharedPref.edit();

		editor.putInt("lastViewVisited", 0).commit();

		Boolean isGuest = sharedPref.getBoolean("isGuest", true);
		Log.v("HOME", "Sono ospite ----> " + isGuest);

		if (isGuest) {

			if (sharedPref.getString("usernameUtente", "Ospite_android")
					.equals("Ospite_android")) {

				Log.v("HOME", "Non ho un nome, lo setto");

				Random r = new Random();
				int ospiteNumber = r.nextInt(10000 - 1000) + 999;

				String UTENTE = "Ospite_android" + ospiteNumber;
				editor.putString("usernameUtente", UTENTE).commit();
			}

			Log.v("HOME",
					"Mi chiamo ----> "
							+ sharedPref.getString("usernameUtente",
									"Ospite_android"));

			rootView = inflater.inflate(R.layout.fragment_home_guest,
					container, false);

			final ProgressWheel pw = (ProgressWheel) rootView
					.findViewById(R.id.home_spinner);

			TextView textWelcome = (TextView) rootView
					.findViewById(R.id.testoHomeGuest);
			textWelcome.setText("Benvenuto "
					+ sharedPref.getString("usernameUtente", "Ospite_android")
					+ ",\n\reffettua il login !");

			Button buttonLogin = (Button) rootView
					.findViewById(R.id.buttonLogin);

			final EditText inputUsername = (EditText) rootView
					.findViewById(R.id.loginUsername);

			String oldUser = sharedPref.getString("oldUser", "");

			if (!oldUser.equals("")) {

				Log.v("HOME", "Setto vecchio nome utente ---> " + oldUser);
				inputUsername.setText(oldUser);

			}

			final EditText inputPassword = (EditText) rootView
					.findViewById(R.id.loginPassword);

			buttonLogin.setOnClickListener(new View.OnClickListener() {

				public void onClick(final View view) {

					Log.v("HOME", "Nascondo tastiera");

					InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
							.getSystemService(Activity.INPUT_METHOD_SERVICE);
					inputMethodManager.hideSoftInputFromWindow(getActivity()
							.getCurrentFocus().getWindowToken(), 0);

					int erroreUsername = 1;
					int errorePassword = 1;

					String username = "";

					if (inputUsername.getText() != null) {

						username = inputUsername.getText().toString();
						if (!username.isEmpty()) {

							erroreUsername = 0;

						}

					}

					String password = "";

					if (inputPassword.getText() != null) {

						password = inputPassword.getText().toString();
						if (!password.isEmpty()) {

							errorePassword = 0;

						}

					}

					if (erroreUsername == 0 && errorePassword == 0) {

						Log.v("HOME", "Non ci sono errori");
						Log.v("HOME", "Verifico la connessione");

						if (GeneralFunction.verifyConnection(getActivity()
								.getApplicationContext())) {

							Log.v("HOME", "Cè connessione");

							final String USER = username;
							final String PASS = password;

							class Login extends AsyncTask<Void, Void, Boolean> {

								@Override
								protected void onPreExecute() {

									final ScrollView scrollview = ((ScrollView) rootView
											.findViewById(R.id.scrollHomeGuest));
									scrollview.post(new Runnable() {
										@Override
										public void run() {
											scrollview
													.fullScroll(ScrollView.FOCUS_DOWN);
										}
									});

									pw.setText("Autenticazione...");
									pw.spin();

								}

								@Override
								protected Boolean doInBackground(Void... params) {

									boolean result = false;
									String newUser = "";

									UserFunctions userFunction = new UserFunctions();
									JSONObject json = userFunction.loginUser(
											USER, PASS);

									try {
										if (json != null) {
											if (Integer.parseInt(json
													.getString("success")) == 1) {

												Log.v("HOME", "Login riuscito");

												pw.setText("Autenticato...");

												String nomeUtente = json
														.getString("name");
												String mailUtente = json
														.getString("email");
												String usernameUtente = json
														.getString("username");
												int idUtente = json
														.getInt("id");

												int isTecnico = json
														.getInt("isTecnico");
												
												int isAdmin = json
														.getInt("isAdministrator");

												editor.putString("nomeUtente",
														nomeUtente).commit();
												editor.putString("mailUtente",
														mailUtente).commit();
												editor.putString(
														"usernameUtente",
														usernameUtente)
														.commit();
												editor.putInt("idUtente",
														idUtente).commit();

												if (isTecnico == 1) {

													Log.v("HOME",
															"E' un tecnico");

													editor.putBoolean(
															"isTecnico", true)
															.commit();

												}
												
												if (isAdmin == 1) {

													Log.v("HOME",
															"E' un amministratore");

													editor.putBoolean(
															"isAdministrator", true)
															.commit();

												}

												editor.putBoolean("isGuest",
														false).commit();
												
												newUser = usernameUtente;

												result = true;

												Log.v("HOME",
														"Salvato correttamente");

											}// End if success == 1
											else {

												Log.v("HOME", "Login errato");
												result = false;

											}
											
											if(!newUser.isEmpty()){
												
												JSONObject updateUserDevice = userFunction
														.updateUserDevice(sharedPref.getString("registrationId", ""),
																newUser);
												
											}

										}// End if json != null
									}// End try
									catch (JSONException e) {

										e.printStackTrace();

									}// End catch

									return result;

								}// End doInBackground...

								protected void onPostExecute(Boolean result) {

									if (result) {

										Intent refresh = getActivity()
												.getBaseContext()
												.getPackageManager()
												.getLaunchIntentForPackage(
														getActivity()
																.getBaseContext()
																.getPackageName());
										refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										startActivity(refresh);

									} else {

										pw.stopSpinning();
										pw.setText("Premi Accedi...");
										errorBox("Username e/o password sbagliati!");

									}// End else

								}// End postExecute

							}// End class + AsyncTask

							new Login().execute();

						}// End if no error - INTERNET
						else {

							Log.v("HOME", "Non cè connessione");
							errorBox("Manca la connessione internet!");

						}

					}// End if no error - CAMPI
					else {

						Log.v("HOME", "Errore --> ci sono campi vuoti");
						errorBox("Riempi i campi vuoti!");

					}
				}// End run
			});// End onClick

		}// End if guest = true
		else {

			rootView = inflater.inflate(R.layout.fragment_home_user, container,
					false);

			final ImageView userAvatar = (ImageView) rootView
					.findViewById(R.id.imageUser);

			final String userAvatarPath = sharedPref.getString("userImagePath",
					"");

			if (userAvatarPath.equals("")) {

				class Logout extends AsyncTask<Void, Void, Bitmap> {

					@Override
					protected void onPreExecute() {

					}

					@Override
					protected Bitmap doInBackground(Void... params) {

						Log.v("HOME", "Recupero url avatar");

						Bitmap imageForUser = null;

						UserFunctions userFunction = new UserFunctions();
						JSONObject json = userFunction
								.recoveryUserImage(sharedPref.getInt(
										"idUtente", 0));

						// check for login response
						try {
							if (json != null) {
								if (Integer.parseInt(json.getString("success")) == 1) {

									Log.v("HOME", "Recuperato");

									String imagePath = json
											.getString("imagePath");

									String userAvatarPath = Configuration.avatarURL
											+ imagePath;

									Log.v("HOME", "Avatar User Url ----> "
											+ userAvatarPath);

									editor.putString("userImagePath",
											userAvatarPath).commit();

									Log.v("HOME", "Inizio download");

									imageForUser = DownloadBitmap
											.getBitmapFromURL(userAvatarPath);

									Log.v("HOME", "Download terminato");

								}// End if success == 1
							}// End if json != null
						}// End try
						catch (JSONException e) {

							e.printStackTrace();

						}// End catch

						return imageForUser;

					}// End doInBackground...

					protected void onPostExecute(Bitmap imageForUser) {

						Log.v("HOME", "Salvo l'avatar");

						Image.saveImageToInternalStorage(getActivity()
								.getApplicationContext(), imageForUser,
								sharedPref
										.getString("usernameUtente", "Ospite"));

						Bitmap avatar = Image.getAvatar(
								getActivity().getApplicationContext(),
								sharedPref
										.getString("usernameUtente", "Ospite")
										+ ".png");

						if (!(sharedPref.getString("usernameUtente", "Ospite") + ".png")
								.equals(".png")) {

							Log.v("HOME", "Setto l'avatar");
							userAvatar.setImageBitmap(avatar);

						}

					}// End postExecute

				}// End class + AsyncTask

				new Logout().execute();

			}

			Bitmap avatar = Image.getAvatar(getActivity()
					.getApplicationContext(),
					sharedPref.getString("usernameUtente", "Ospite") + ".png");

			if (!(sharedPref.getString("usernameUtente", "Ospite") + ".png")
					.equals(".png")) {

				userAvatar.setImageBitmap(avatar);

			}

			TextView textWelcome = (TextView) rootView
					.findViewById(R.id.testoHomeUser);
			textWelcome.setText("Benvenuto "
					+ sharedPref.getString("usernameUtente", "Ospite") + " !");

			Button buttonLogout = (Button) rootView
					.findViewById(R.id.buttonLogout);

			buttonLogout.setOnClickListener(new View.OnClickListener() {

				public void onClick(final View view) {

					Log.v("HOME", "Comincio logout");

					File avatarToDelete = new File(getActivity().getFilesDir()
							.getPath()
							+ "/"
							+ sharedPref.getString("usernameUtente", "Ospite")
							+ ".png");
					boolean deleted = avatarToDelete.delete();
					Log.v("HOME", "Avatar cancellato ---->" + deleted);

					editor.remove("nomeUtente").commit();
					editor.remove("mailUtente").commit();

					// Se è true salvo il nome utente dopo logout
					if (sharedPref.getBoolean("rememberUsername", true)) {

						Log.v("HOME", "Salvo nome utente");
						editor.putString("oldUser",
								sharedPref.getString("usernameUtente", ""))
								.commit();

					}

					editor.remove("usernameUtente").commit();
					editor.remove("idUtente").commit();
					editor.remove("userImagePath").commit();

					editor.remove("isTecnico").commit();
					editor.remove("isAdministrator").commit();

					editor.remove("isGuest").commit();
					
					editor.putBoolean("updateUserOnDb", true).commit();

					Intent refresh = getActivity()
							.getBaseContext()
							.getPackageManager()
							.getLaunchIntentForPackage(
									getActivity().getBaseContext()
											.getPackageName());
					refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(refresh);

					Log.v("HOME", "Logout terminato");

				}
			});

		}// End if guest = false

		return rootView;
	}// End method

	public void errorBox(String mymessage) {
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
}
