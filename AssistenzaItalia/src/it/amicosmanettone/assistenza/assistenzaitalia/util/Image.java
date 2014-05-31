package it.amicosmanettone.assistenza.assistenzaitalia.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class Image {

	public static boolean saveImageToInternalStorage(Context context,
			Bitmap image, String nomeFile) {

		try {

			FileOutputStream fos = context.openFileOutput(nomeFile + ".png",
					Context.MODE_PRIVATE);

			// Writing the bitmap to the output stream
			image.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();

			return true;
		} catch (Exception e) {
			Log.e("saveToInternalStorage()", e.getMessage());
			return false;
		}
	}

	

	public static Bitmap getAvatar(Context context, String filename) {

		Bitmap avatar = null;

		// Look for the file on the external storage
		try {
			if (isSdReadable() == true) {
				avatar = BitmapFactory.decodeFile(context.getFilesDir().getPath() + "/" + filename);
			}
		} catch (Exception e) {
			Log.e("getThumbnail() on external storage", e.getMessage());
		}

		// If no file on external storage, look in internal storage
		if (avatar == null) {
			try {
				File filePath = context.getFileStreamPath(filename);
				FileInputStream fi = new FileInputStream(filePath);
				avatar = BitmapFactory.decodeStream(fi);
			} catch (Exception ex) {
				Log.e("getThumbnail() on internal storage", ex.getMessage());
			}
		}
		return avatar;
	}
	
	public static boolean isSdReadable() {

		boolean mExternalStorageAvailable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = true;
			Log.i("isSdReadable", "External storage card is readable and writable.");
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			Log.i("isSdReadable", "External storage card is only readable.");
			mExternalStorageAvailable = true;
		} else {
			// Something else is wrong. It may be one of many other
			// states, but all we need to know is we can neither read nor write
			mExternalStorageAvailable = false;
		}

		return mExternalStorageAvailable;
	}

}
