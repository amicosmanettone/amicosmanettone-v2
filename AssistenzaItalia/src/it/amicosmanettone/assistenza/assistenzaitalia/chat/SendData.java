package it.amicosmanettone.assistenza.assistenzaitalia.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class SendData extends Thread {
	
	String baseUrl;
	HashMap parameter;
	
	public SendData(String baseUrl, HashMap parameter) {
		
		this.baseUrl = baseUrl;
		this.parameter = parameter;
	
	}

	public void run() {
		
		SendData.SendPostData(baseUrl, parameter);
		
	}

	public static void SendPostData(String baseUrl, HashMap parameter) {
		
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(baseUrl);
		
		try {
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					parameter.size());
			Log.v("HTTP", "URL: " + baseUrl);

			Log.v("HTTP", "PARAMETER SIZE " + parameter.size());

			String token = (String) parameter.get("token");

			String txt = (String) parameter.get("txt");
			
			String nomeUtente = (String) parameter.get("user");

			nameValuePairs.add(new BasicNameValuePair("txt", txt));
			nameValuePairs.add(new BasicNameValuePair("token", token));
			Log.v("HTTP", "TXT: " + txt + " - TOKEN: " + token);
			
			httppost.setHeader("Cookie", "kide_config=name%3D" + nomeUtente);


			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			String out = "";

			HttpResponse response = httpclient.execute(httppost);

			
			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null) {

				out = EntityUtils.toString(responseEntity);

			}

			Log.v("myApp", out);

		} catch (Exception e) {
			Log.w("myApp", e.getCause());
			e.printStackTrace();
		}// End try...catch...
	}// End method...
}// End class...
