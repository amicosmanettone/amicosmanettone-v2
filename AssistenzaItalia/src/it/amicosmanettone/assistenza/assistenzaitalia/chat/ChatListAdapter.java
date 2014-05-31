package it.amicosmanettone.assistenza.assistenzaitalia.chat;

import it.amicosmanettone.assistenza.assistenzaitalia.R;
import it.amicosmanettone.assistenza.assistenzaitalia.adapter.CircularImageView;
import it.amicosmanettone.assistenza.assistenzaitalia.mysql.DownloadBitmap;
import it.amicosmanettone.assistenza.assistenzaitalia.util.Image;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ChatListAdapter extends ArrayAdapter {

	List<ChatXmlBean> lista = null;
	Context adContext;

	public ChatListAdapter(Context context, int textViewResourceId, List objects) {
		super(context, textViewResourceId, objects);

		this.adContext = context;
		this.lista = objects;

	}// End method...

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		convertView = inflater.inflate(R.layout.rigachat, null);
		
		LayoutParams params = convertView.getLayoutParams();

		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();

		display.getSize(size);

		int width = size.x;
		int height = size.y;
		Log.v("CHAT ADP", "Altezza del dispositivo ---> " + height
				+ " - Larghezza del dispositivo ---> " + width);

		if (params == null) {
			
			params = new LayoutParams(width - 250, LayoutParams.MATCH_PARENT);
			
		} else {
			
			params.width = width - 250;
			
		}
		
		//convertView.setLayoutParams(params);

		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(adContext);
		String userPreference = sharedPref.getString("usernameUtente",
				"Ospite_android");

		final CircularImageView userAvatar = (CircularImageView) convertView
				.findViewById(R.id.imageChatUser);

		TextView user = (TextView) convertView.findViewById(R.id.textChatUser);

		TextView message = (TextView) convertView
				.findViewById(R.id.textChatMessage);

		TextView date = (TextView) convertView.findViewById(R.id.textChatDate);

		final ChatXmlBean mess = (ChatXmlBean) lista.get(position);

		if (!userPreference.equals(mess.getUser())) {

		}

		class DownloadImage extends AsyncTask<Void, Void, Bitmap> {

			@Override
			protected Bitmap doInBackground(Void... params) {

				Bitmap imageForUser = DownloadBitmap.getBitmapFromURL(mess
						.getImagePath());

				return imageForUser;

			}// End doInBackground...

			protected void onPostExecute(Bitmap imageForUser) {

				Image.saveImageToInternalStorage(adContext, imageForUser,
						mess.getUser());

				Bitmap avatar = Image.getAvatar(adContext, mess.getUser()
						+ ".png");

				userAvatar.setImageBitmap(avatar);

			}

		}
		
		if(Image.getAvatar(adContext, mess.getUser()
						+ ".png") == null){
			
			Log.v("CHAT","Avatar non trovato, lo scarico");
			
			new DownloadImage().execute();
			
		}else{
			
			Log.v("CHAT","Avatar trovato, lo mostro");
			
			userAvatar.setImageBitmap(Image.getAvatar(adContext, mess.getUser()
					+ ".png"));
			
		}

		String messageText = mess.getMessage().trim();

		//String smile = "<img alt=\":)\" src=\"/components/com_kide/templates/default/images/iconos/smile.png\" title=\":)\" class=\"KIDE_icono\">";

		// Log.v("CHAT ADP","MEss: ---->   " + smile);

		messageText = Html.fromHtml(messageText).toString();
		
		Typeface tf = Typeface.createFromAsset(adContext.getAssets(),"lightSans.ttf");

		user.setText(mess.getUser().trim());
		
		user.setTypeface(tf);
		
		String rango = mess.getRango();
		
		if(rango.equals("1")){
			
			user.setTextColor(Color.parseColor("#ff0000"));
			
		}else if(rango.equals("2")){
			
			user.setTextColor(Color.parseColor("#ffae42"));
			
		}
		
		message.setText(messageText);
		date.setText(mess.getDate().trim());

		return convertView;
	}// End method...

}// End class...