package it.amicosmanettone.assistenza.assistenzaitalia;

import it.amicosmanettone.assistenza.assistenzaitalia.R;
import it.amicosmanettone.assistenza.assistenzaitalia.configuration.Configuration;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class InformationFragment extends Fragment {

	View rootView;
	
	SharedPreferences sharedPref;
	Editor editor;

	public InformationFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getActivity()
						.getApplicationContext());

		editor = sharedPref.edit();
		
		if (sharedPref.getBoolean("isTecnico", false)) {

			editor.putInt("lastViewVisited", 4).commit();
			
		}else{
			
			editor.putInt("lastViewVisited", 3).commit();
			
		}

		rootView = inflater
				.inflate(R.layout.fragment_information, container, false);
		
		Typeface moon = Typeface.createFromAsset(getActivity().getAssets(), "club.otf");
		
		Button buttonFacebook = (Button) rootView.findViewById(R.id.buttonFacebook);
		Button buttonTwitter = (Button) rootView.findViewById(R.id.buttonTwitter);
		Button buttonGoogle = (Button) rootView.findViewById(R.id.buttonGoogle);
		Button buttonAmicosmanettone = (Button) rootView.findViewById(R.id.buttonAmicosmanettone);
		TextView textHead = (TextView) rootView.findViewById(R.id.textInformationHead);
		TextView textBottom = (TextView) rootView.findViewById(R.id.textInformationBottom);
		
		PackageInfo pInfo = null;
		try {
			pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
		} catch (NameNotFoundException e) {

			e.printStackTrace();
		}
		
		String version = pInfo.versionName;
		
		textHead.setText(getText(R.string.app_name).toString() + "\n\r" + version);
		textHead.setTypeface(moon);
		textHead.refreshDrawableState();
		
		buttonFacebook.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(Configuration.FACEBOOK_URL));
				startActivity(i);
				
			}
		});
		
		buttonTwitter.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(Configuration.TWITTER_URL));
				startActivity(i);
				
			}
		});
		
		buttonGoogle.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(Configuration.GOOGLE_URL));
				startActivity(i);
				
			}
		});
		
		buttonAmicosmanettone.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(Configuration.AMICOSMANETTONE_URL));
				startActivity(i);
				
			}
		});
		
		

		return rootView;

	}// End method
}
