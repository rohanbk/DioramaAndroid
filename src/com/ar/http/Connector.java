package com.ar.http;


import com.android.imageoverlay.*;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.ar.http.Data.POIFromObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

/*!
* @author Rohan Balakrishnan
* \class Connector
* \brief Activity class for ARDiorama project
*/
public class Connector extends Activity {
	int ht;
	int wt;
	POIReceiver poiReceiver; /*!<Object instance of broadcast receiver class that will receive intents from service*/
	public static Map<String, Victims> POIs; /*!<hash-table to store POIs that need to be displayed to view*/
	private LinearLayout linear; 
	private static final int RADIUS = 20;
	static final private int ADD_NEW_TODO = Menu.FIRST;
	static final private int REMOVE_TODO = Menu.FIRST + 1;
	/*
	 * @author Rohan Balakrishnan
	 * @param savedInstanceState
	 * 
	 * @return void
	 * 
	 * \brief Ostensible Main method of the application. 
	 * Starts service, initializes hash-table, and creates all Views
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		POIs = new HashMap<String, Victims>();
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);

		Intent scv = new Intent(Connector.this, ConnectorService.class);
		scv.putExtra("ORIENTATION", Configuration.ORIENTATION_PORTRAIT);
		startService(scv);
		linear = (LinearLayout) findViewById(R.id.layoutid);
		// DrawOnTop mtop = new DrawOnTop(this,100,50,20);
		Preview mpreview = new Preview(this);
		// DrawOnTop mtop = new DrawOnTop(this, POIs);

		linear.addView(mpreview);

	}
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    super.onCreateOptionsMenu(menu);

	    // Create and add new menu items.
	    MenuItem itemAdd = menu.add(0, ADD_NEW_TODO, Menu.NONE,
	                                R.string.add_new);
	    MenuItem itemRem = menu.add(0, REMOVE_TODO, Menu.NONE,
	                                R.string.remove);

	    // Allocate shortcuts to each of them.
	    itemAdd.setShortcut('0', 'a');
	    itemRem.setShortcut('1', 'r');

	    return true;
	  }
	  @Override
	  public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
			case (REMOVE_TODO): {
				Toast.makeText(this, "Remove pressed", Toast.LENGTH_SHORT);
				return true;
			}
			case (ADD_NEW_TODO): {
				Toast.makeText(this, "Add pressed", Toast.LENGTH_SHORT);
				return true;
			}
		}

		return false;
	}
	/*
	 * @author Rohan Balakrishnan
	 * @param
	 * @return void
	 * 
	 * \brief Stop running the service 
	 */
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		stopService(new Intent(Connector.this, ConnectorService.class));
	}

	/*
	 * @author Rohan Balakrishnan
	 * @param 
	 * @return void
	 * 
	 * Create a filter to capture any new intent messages which indicate new POI
	 * objects
	 */
	@Override
	public void onResume() {
		super.onResume();
		IntentFilter filter;

		filter = new IntentFilter(ConnectorService.POI_IN_VIEW);
		poiReceiver = new POIReceiver();
		registerReceiver(poiReceiver, filter);

	}
	
	
	@Override
	public void onPause()
	{
		super.onPause();
		unregisterReceiver(poiReceiver);
	}

	/*
	 * @author Siddhesh Gandhi
	 * 
	 * @return void
	 * 
	 * \brief 
	 */
	public void update() {
		runOnUiThread(new Runnable() {

			public void run() {
	
				Log.d("Update POI", String.valueOf(POIs.size()));
				DrawOnTop d = new DrawOnTop(getParent(), POIs);
				float height = (float) (getWindow().getWindowManager()
						.getDefaultDisplay().getHeight());
				float width = (float) getWindow().getWindowManager()
						.getDefaultDisplay().getWidth();
				// Log.d("Image", String.valueOf(width));
				DrawOnTop.setwidth(width);
				// DrawOnTop.setAspectRatio(width, height);
				addContentView(d, new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
				d.invalidate();

				//POIs.clear();
			}
		});
	}

	/*!
	* @author Rohan Balakrishnan
	* \class POIReceiver
	* \brief Broadcast receiver class that is called whenever an
	* intent requires reception from the ConnectorService service class 
	*/
	public class POIReceiver extends BroadcastReceiver {

		/*
		 * @author Rohan Balakrishnan
		 * @param context, intent
		 * 
		 * @return void
		 * 
		 * Grab any intents sent from the Service which contain new POI objects.
		 * Strip them from the intent, and update the Hash table that is storing
		 * them.
		 */
		@Override
		public void onReceive(Context context, Intent intent) {
			Data.POIFromObject p = new Data.POIFromObject();
			boolean empty=intent.getBooleanExtra("empty_poi", false);
			boolean done = intent.getBooleanExtra("listiterationdone", false);
//			Log.d("Before if", String.valueOf(POIs.size()));
			Log.d("Done", String.valueOf(done));
			Log.d("Empty", String.valueOf(empty));
			
			//empty indicates whether any POI are in view
			if(!empty)
			{
				//take the necessary information from the intent
				int id = intent.getIntExtra("id", -1);
				double lat = intent.getDoubleExtra("lat", -1) / 1000000;
				double lon = intent.getDoubleExtra("lon", -1) / 1000000;
				float bearing = intent.getFloatExtra("bearing", -1);
				double distance = intent.getDoubleExtra("distance", -1);
				int angle_offset = intent.getIntExtra("angle_offset", 1000);
				int type = intent.getIntExtra("type", -1);
				float phone_bearing=intent.getFloatExtra("phone_bearing", -1);
				p.setId(id);
				p.setBearing(bearing);
				p.setDistance(distance);
				p.setLat((double) lat);
				p.setLon((double) lon);
				p.setAngle_Offset(angle_offset);
				p.setType(type);
				Log.d("ANGLE_ACTIVITY",String.valueOf(angle_offset));
			
				/*if the hash-table already contains the discovered POI
				* then remove it. 
				* */
				if (POIs.containsKey(String.valueOf(id))) {
					POIs.remove((String.valueOf(id)));
				}
				
				/* if a valid POI angle-offset was provided by the intent
				 * then add that particular POI to the hash-table
				*/
				if (!(angle_offset >= 50))
				{
					float latitude= 150;
					POIs.put(String.valueOf(id), new Victims((float) p.getLon(), latitude,
							RADIUS, (int) p.getBearing(), String.valueOf(p.getId()),
							p.getAngle_Offset(),p.getDistance(),p.getType()));
					Log.d("Size of POIs", String.valueOf(POIs.size()));
				}

			}
			else
			{
				//Flush hash-table so that old entries are removed
				if (!(POIs.isEmpty()))
				{
					POIs.clear();
				}
			}
			// Calling update() to display above POIs on the screen
			Log.d("After if", String.valueOf(POIs.size()));
			
			/* If all POI entries in view have been iterated over
			 * then update the View and clear the hash-table
			*/
			if (done) {
				int size = POIs.size();
				update();
				POIs.clear();
			}

		}
	}

	/*
	 * @author Rohan Balakrishnan
	 * @param newConfig
	 * 
	 * @return void
	 * 
	 */
	@Override
    public void onConfigurationChanged(Configuration newConfig)
    {
    	super.onConfigurationChanged(newConfig);
    	   	
    	switch (newConfig.orientation) {
		
    	case Configuration.ORIENTATION_PORTRAIT:
			
			ConnectorService.ChangeInModeToLandscape=false;
			DrawOnTop.ChangeInModeToLandscape=false;
			//DrawOnTop.ChangeInModeToPortrait = true;
   	        break;
   	        
		case Configuration.ORIENTATION_LANDSCAPE:
			
			//	DrawOnTop.ChangeInModeToPortrait = false;
			ConnectorService.ChangeInModeToLandscape=true;
			DrawOnTop.ChangeInModeToLandscape=true;
			
			float height= (float)(getWindow().getWindowManager().getDefaultDisplay().getHeight());
	    	float width= (float) getWindow().getWindowManager().getDefaultDisplay().getWidth();
			DrawOnTop.setAspectRatio(width, height);
			
			break;

		default:
			break;
		}
    	
    }

}