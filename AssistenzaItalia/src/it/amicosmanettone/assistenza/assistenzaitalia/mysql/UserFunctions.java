package it.amicosmanettone.assistenza.assistenzaitalia.mysql;

import it.amicosmanettone.assistenza.assistenzaitalia.configuration.Configuration;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

public class UserFunctions {

	private JSONParser jsonParser;

	// constructor
	public UserFunctions() {
		jsonParser = new JSONParser();
	}
	
	public JSONObject checkVersion(String version) {

		final List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Configuration.checkVersion_tag));
		params.add(new BasicNameValuePair("version", version));

		JSONObject json = jsonParser.getJSONFromUrl(Configuration.post_tag, Configuration.apiURL, params);

		return json;
	}

	public JSONObject loginUser(String username, String password) {

		final List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Configuration.login_tag));
		params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));

		JSONObject json = jsonParser.getJSONFromUrl(Configuration.post_tag, Configuration.apiURL, params);

		return json;
	}
	
	public JSONObject recoveryUserImage(int userId) {

		final List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Configuration.recoveryUserImage_tag));
		params.add(new BasicNameValuePair("userId", Integer.toString(userId)));

		JSONObject json = jsonParser.getJSONFromUrl(Configuration.post_tag, Configuration.apiURL, params);

		return json;
	}
	
	public JSONObject getUserKarma(String username) {

		final List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Configuration.getUserKarma_tag));
		params.add(new BasicNameValuePair("username", username));

		JSONObject json = jsonParser.getJSONFromUrl(Configuration.post_tag, Configuration.apiTECNICI, params);

		return json;
	}
	
	
	public JSONObject registerDevice(String deviceId, String user, String androidVersion) {

		final List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Configuration.registerDevice_tag));
		params.add(new BasicNameValuePair("deviceId", deviceId));
		params.add(new BasicNameValuePair("user", user));
		params.add(new BasicNameValuePair("androidVersion", androidVersion));

		JSONObject json = jsonParser.getJSONFromUrl(Configuration.post_tag, Configuration.apiGCM, params);

		return json;
	}
	
	public JSONObject updateUserDevice(String regId, String newUser) {

		final List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Configuration.updateUserDevice_tag));
		params.add(new BasicNameValuePair("regId", regId));
		params.add(new BasicNameValuePair("newUser", newUser));

		JSONObject json = jsonParser.getJSONFromUrl(Configuration.post_tag, Configuration.apiGCM, params);

		return json;
	}
	
	public JSONObject sendChatNotification() {

		final List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Configuration.sendChatNotification_tag));

		JSONObject json = jsonParser.getJSONFromUrl(Configuration.post_tag, Configuration.apiURL, params);

		return json;
	}
	
	public JSONObject checkDiagnosisVersion(String version) {

		final List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Configuration.checkDiagnosisVersion_tag));
		params.add(new BasicNameValuePair("version", version));

		JSONObject json = jsonParser.getJSONFromUrl(Configuration.post_tag, Configuration.apiURL, params);

		return json;
	}
	
	public JSONObject sendRichiestaAssistenza(Context context) {
		
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);

		final List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("form[formId]", "78"));
		params.add(new BasicNameValuePair("form[Submit]", "Invia"));
		params.add(new BasicNameValuePair("form[Mail]", sharedPref.getString("assistenzaMail", "")));
		params.add(new BasicNameValuePair("form[Nome]", sharedPref.getString("assistenzaNome", "")));
		params.add(new BasicNameValuePair("form[Regione]", sharedPref.getString("assistenzaRegione", "")));
		params.add(new BasicNameValuePair("form[Provincia]", sharedPref.getString("assistenzaProvincia", "")));
		params.add(new BasicNameValuePair("form[Localita]", sharedPref.getString("assistenzaLocalita", "")));
		params.add(new BasicNameValuePair("form[Telefono]", sharedPref.getString("assistenzaTel1", "")));
		params.add(new BasicNameValuePair("form[Telefono 2]", sharedPref.getString("assistenzaTel2", "NON SETTATO")));
		params.add(new BasicNameValuePair("form[Problema][]", sharedPref.getString("assistenzaProblema", "")));
		params.add(new BasicNameValuePair("form[Internet][]", sharedPref.getString("assistenzaInternet", "")));
		
		String messaggio = sharedPref.getString("assistenzaMessaggio", "");
		
		if(sharedPref.getBoolean("assistenzaThisDevice", false) == true){
			
			messaggio = messaggio + "\n\n" + Build.MANUFACTURER + "\n" + Build.MODEL + "\n" + Build.CPU_ABI + "\n" + Build.VERSION.RELEASE;
			
		}
		
		messaggio = messaggio + "\n\n --- RICHIESTA INVIATA DA ANDROID ---";
		
		params.add(new BasicNameValuePair("form[Messaggio]", messaggio));

		JSONObject json = jsonParser.getJSONFromUrl(Configuration.post_tag, Configuration.FORM_URL, params);

		return json;
	}
	
	public JSONObject incrementRichieste(String regId) {

		final List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Configuration.incrementRichieste_tag));
		params.add(new BasicNameValuePair("regId", regId));

		JSONObject json = jsonParser.getJSONFromUrl(Configuration.post_tag, Configuration.apiGCM, params);

		return json;
	}
	
	public JSONObject deleteMessage(String messageId) {

		final List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Configuration.deleteMessage_tag));
		params.add(new BasicNameValuePair("messageId", messageId));

		JSONObject json = jsonParser.getJSONFromUrl(Configuration.post_tag, Configuration.apiURL, params);

		return json;
	}
	
	

}