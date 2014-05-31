package it.amicosmanettone.assistenza.assistenzaitalia.mysql;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DownloadBitmap {

	public static Bitmap getBitmapFromURL(String imageUrl) {

				try {
					URL url = new URL(imageUrl);
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setDoInput(true);
					connection.setUseCaches(true);
					connection.connect();
					InputStream input = connection.getInputStream();
					Bitmap myBitmap = BitmapFactory.decodeStream(input);
					return myBitmap;
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
		
	}

}// End class...
