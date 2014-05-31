package it.amicosmanettone.assistenza.assistenzaitalia.chat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Xml;

public class ChatXMLParser {

	private static final String ns = null;

	public List parse(Context context, String URL)
			throws XmlPullParserException, IOException {

		try {

			URL url = new URL(URL);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			InputStream input = urlConnection.getInputStream();
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(input, null);
			parser.nextTag();
			return readFeed(context, parser);
		} finally {
			// input.close();
		}
	}

	private List readFeed(Context context, XmlPullParser parser)
			throws XmlPullParserException, IOException {

		final SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		final Editor editor = sharedPref.edit();

		List entries = new ArrayList();
		try {

			while (parser.next() != XmlPullParser.END_DOCUMENT) {

				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}

				String name = parser.getName();

				if (name.equals("last_id")) {

					// Log.v("PARSER", "LAST MESSAGE ----> " +
					// parser.nextText());
					editor.putString("lastNumberMessageChat", parser.nextText())
							.commit();

				} else if (name.equals("mensaje")) {

					entries.add(readEntry(parser));

				} else {

					skip(parser);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return entries;
	}

	private ChatXmlBean readEntry(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "mensaje");

		String user = null;
		String messaggio = null;
		String date = null;
		String imagePath = null;
		String rango = null;
		String id = null;

		int eventType = parser.getEventType();
		if (eventType == XmlPullParser.START_TAG
				&& parser.getName().equalsIgnoreCase("MENSAJE")) {

			int totaleAttributi = parser.getAttributeCount();
			for (int i = 0; i < totaleAttributi; i++) {

				String nomeAttributo = parser.getAttributeName(i);
				String valoreAttributo = parser.getAttributeValue(i);
				if (nomeAttributo.equalsIgnoreCase("name")) {
					user = valoreAttributo;
				} else if (nomeAttributo.equalsIgnoreCase("date")) {
					date = valoreAttributo;
				} else if (nomeAttributo.equalsIgnoreCase("img")) {
					imagePath = valoreAttributo;
				} else if (nomeAttributo.equalsIgnoreCase("rango")) {
					rango = valoreAttributo;
				} else if (nomeAttributo.equalsIgnoreCase("id")) {
					id = valoreAttributo;
				}

			}

			parser.next();
			messaggio = parser.getText();

		}

		return new ChatXmlBean(messaggio, user, date, imagePath, rango, id);
	}

	private void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}

}
