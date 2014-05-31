package it.amicosmanettone.assistenza.assistenzaitalia.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;
import android.util.Log;

public class ReadDiagnosis {
	
	
	public static JSONObject readCategory(Resources resources, int FILE){
		
		InputStream is = resources.openRawResource(FILE);
		Writer writer = new StringWriter();
		char[] buffer = new char[1024];
		try {
			Reader reader = new BufferedReader(new InputStreamReader(is,
					"UTF-8"));
			int n;
			while ((n = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, n);
			}
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		JSONObject jsonObj = null;
		try {
			
			jsonObj = new JSONObject(writer.toString());
			
		} catch (JSONException e1) {
			
			e1.printStackTrace();
		}
		
		
		String jsonCategorieString = null;
		
		try {
			
			jsonCategorieString = new JSONObject(writer.toString()).toString(2);
			
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		
		Log.v("DIAGNOSI", "Leggo json ------>\n\r" + jsonCategorieString);
		
		return jsonObj;
		
	}

}
