package it.amicosmanettone.assistenza.assistenzaitalia;

import it.amicosmanettone.assistenza.assistenzaitalia.adapter.NavDrawerItem;
import it.amicosmanettone.assistenza.assistenzaitalia.adapter.NavDrawerListAdapter;
import it.amicosmanettone.assistenza.assistenzaitalia.R;

import java.util.ArrayList;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mTitle;

	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	SharedPreferences sharedPref;
	Editor editor;

	String regId;
	GoogleCloudMessaging gcm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		editor = sharedPref.edit();
		
		setContentView(R.layout.activity_main);

		mTitle = getTitle();

		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));

		// Chat
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));

		// Assistenza
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1)));

		// Area tecnici
		if (sharedPref.getBoolean("isTecnico", false) == false) {

			navDrawerItems.remove(new NavDrawerItem(navMenuTitles[3],
					navMenuIcons.getResourceId(3, -1)));

		} else {

			navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
					.getResourceId(3, -1)));

		}

		// Autodiagnosi
		// Al momento è OUT
		/*
		 * navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
		 * .getResourceId(3, -1)));
		 */

		// Informazioni
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
				.getResourceId(4, -1)));

		// Preference
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
				.getResourceId(5, -1)));
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);

				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle("Menu");

				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {

			displayView(sharedPref.getInt("lastViewVisited", 2));
		}
	}

	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			displayView(position);
			Log.v("MAIN", "POSITION ----->" + position);
		}
	}

	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		PreferenceFragment Pfrag = null;

		if (sharedPref.getBoolean("isTecnico", false) == false) {

			switch (position) {

			case 0:
				fragment = new HomeFragment();
				break;

			case 1:
				fragment = new ChatFragment();
				break;

			case 2:
				fragment = new AssistenzaFragment();
				break;

			case 3:
				fragment = new InformationFragment();
				break;

			case 4:
				Pfrag = new PreferenceFragment();
				break;

			default:
				break;
			}

		} else {

			switch (position) {

			case 0:
				fragment = new HomeFragment();
				break;

			case 1:
				fragment = new ChatFragment();
				break;

			case 2:
				fragment = new AssistenzaFragment();
				break;

			case 3:
				fragment = new TecniciFragment();
				break;

			case 4:
				fragment = new InformationFragment();
				break;

			case 5:
				Pfrag = new PreferenceFragment();
				break;

			default:
				break;
			}

		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else if (Pfrag != null) {

			Intent frag = new Intent(this, PreferenceFragment.class);
			startActivity(frag);

			// update selected item and title, then close the drawer
			// mDrawerList.setItemChecked(position, true);
			// mDrawerList.setSelection(position);
			// setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);

		} else {

			Log.e("MainActivity", "Error in creating fragment");

		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			if (mDrawerLayout.isDrawerVisible(mDrawerList) != false) {
				mDrawerLayout.closeDrawer(mDrawerList);
			}

		}
		return true;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage("Vuoi chiudere l\'applicazione?")
				.setCancelable(false)

				.setPositiveButton("Si", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {

						editor.remove("lastNumberMessageChatFALSE").commit();
						editor.remove("firstChatStart").commit();
						editor.remove("chatIsActive").commit();

						dialog.dismiss();

						android.os.Process.killProcess(android.os.Process
								.myPid());
						System.exit(0);

					}
				})

				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						dialog.cancel();

					}
				});

		AlertDialog alert = builder.create();
		alert.show();

	}// End onBackPressed

	
}
