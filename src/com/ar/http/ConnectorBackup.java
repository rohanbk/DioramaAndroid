/*package com.ar.http;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.math.*;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.ar.http.Data.POIFromObject;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;


public class ConnectorBackup extends Activity {
	private TextView latlonoutput,bearingoutput,jsonoutput;
	double latitude, longitude;
	float accuracy, phone_bearing;
	int ht;
	int wt;
	
	String outputString;
	private static String TAG = "COMPASS";
	private SensorManager sm;
	private static float[] orientation_sensor = new float[3];
	List<Data.POIFromObject> hotspots;
	Location location;
	final int FIELD_OF_VIEW =90;
	float degree_to_pixel;
	
	public void init() {
		latlonoutput = (TextView) findViewById(R.id.LatLonOutput);
		bearingoutput = (TextView) findViewById(R.id.BearingOutput);
		jsonoutput = (TextView) findViewById(R.id.jsonOutput);
		degree_to_pixel=wt/FIELD_OF_VIEW;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		ht = displaymetrics.heightPixels;
		wt = displaymetrics.widthPixels;

		init();

		LocationManager locationManager;
		String context = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) getSystemService(context);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(true);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		String provider = locationManager.getBestProvider(criteria, true);
		location = locationManager.getLastKnownLocation(provider);
		conn(location);

		locationManager.requestLocationUpdates(provider, 200, 1, locationListener);
		
		sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        int sensorType = Sensor.TYPE_ORIENTATION;
        Timer updatetimer = new Timer("UpdateTimer");
        updatetimer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				updateBearing();
			}
		}, 0,200);

	}
	
	public void conn(Location location) {
		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			accuracy = location.getAccuracy();
			
			
		} else {
			latitude = 0;
			longitude = 0;
			accuracy = 100;
		}
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(
				"http://dioramang.ecs.umass.edu/layardotnet/test.layar?lon="
						+ longitude + "&radius=5000&lat=" + latitude
						+ "&layerName=ardiorama&accuracy=" + accuracy + "");

		// Create a response handle
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = "";
		try {
			responseBody = httpclient.execute(httpget, responseHandler);
			Gson gson = new Gson();
			Data obj = new Data();
			obj = gson.fromJson(responseBody, Data.class);
			hotspots = obj.getHotspots();
		} catch (Exception e) {
			e.printStackTrace();
		}
		outputString = "lat: " + latitude + "\n lon: " + longitude
				+ "\n accuracy: " + accuracy + "\n";
		latlonoutput.setText(outputString);
		//jsonoutput.setText(responseBody);
		// Toast.makeText(Connector.this, responseBody.toString(),3).show();
		httpclient.getConnectionManager().shutdown();
	}
	
	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			conn(location);
		}

		public void onProviderDisabled(String provider) {
			conn(null);
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};
	
    private final SensorEventListener listener = new SensorEventListener() {
  		
  		@Override
  		public void onSensorChanged(SensorEvent event) {
  			ConnectorBackup.orientation_sensor = event.values;
  		}
  		
  		@Override
  		public void onAccuracyChanged(Sensor sensor, int accuracy) {
  		}
  	};
	
	@Override
    public void onResume()
    {
    	super.onResume();
    	sm.registerListener(listener, sm.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_NORMAL);
    }
    
    @Override
    public void onStop()
    {
    	super.onStop();
    	sm.unregisterListener(listener);
    }
    
    public void updateBearing()
    {
    	runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				phone_bearing=ConnectorBackup.orientation_sensor[0];
				float upper_bound=((phone_bearing+(FIELD_OF_VIEW/2))%360);
				float lower_bound=((phone_bearing-(FIELD_OF_VIEW/2))%360);
				if(lower_bound<0)
					lower_bound=((360+phone_bearing-(FIELD_OF_VIEW/2))%360);
				bearingoutput.setText("Bearing: "+phone_bearing+" ["+lower_bound+","+upper_bound+"]");
				checkHotspots();
			}
		});
    }
    public List<Data.POIFromObject> checkHotspots(){
    	//Iterate over contents of hotspots list
    	List<Data.POIFromObject> outputlist=null;
    	Data.POIFromObject p=null;
    	String POIoutputtext="***POI in view***\n";
    	for (Iterator<Data.POIFromObject> i = hotspots.iterator( ); i.hasNext( ); ) {
    		p = i.next();
    		double lat1=latitude;
    		double long1=longitude;
    		double lat2=p.getLat()/1000000;
    		double long2=p.getLon()/1000000;

    		int offset_from_center=0;
    		double poi_bearing=BearingDetermination.Bearing(lat1, long1, lat2, long2);
    		
    		float upper_bound=((phone_bearing+(FIELD_OF_VIEW/2))%360);
			float lower_bound=((phone_bearing-(FIELD_OF_VIEW/2))%360);
			if(lower_bound<0)
				lower_bound=((360+phone_bearing-(FIELD_OF_VIEW/2))%360);
			if(upper_bound>=0 && upper_bound<(FIELD_OF_VIEW))
				upper_bound=((360+phone_bearing+(FIELD_OF_VIEW/2)));
    		
    		if((poi_bearing>lower_bound && poi_bearing<upper_bound)){
    			//depending on the value of left_or_right
    			//determine what the offset from the center of the screen is
    			if(poi_bearing>phone_bearing || poi_bearing<phone_bearing)
    				offset_from_center=(int)(poi_bearing-phone_bearing);
    			else
    				offset_from_center=0;
    			float pixels=offset_from_center/degree_to_pixel;
    			double poi_distance=BearingDetermination.Distance(lat1, long1, lat2, long2);
    			POIoutputtext+="+id: "+p.getId()+", POIbearing: "+(int)poi_bearing+", Distance: "+poi_distance+"\n";
    			p.setBearing((int)poi_bearing);
    			p.setDistance((float)poi_distance);
    			outputlist.add(p);
    			
    		}
    	}
    	jsonoutput.setText(POIoutputtext);
    	return outputlist;
    }
}*/