package com.trashandtoilet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.trashandtoilet.dto.Toilet;
import com.trashandtoilet.service.GPSTracker;
import com.trashandtoilet.windowadapter.TestingParsing;

@SuppressLint("NewApi")
public class SearchToilet extends FragmentActivity /*
													 * implements'
													 * OnMapClickListener,
													 * OnMapLongClickListener,
													 * OnCameraChangeListener
													 */{
	static final LatLng SecondToilet = new LatLng(17.447729806707645,
			78.3633230254054);
	static final LatLng FirstToilet = new LatLng(17.4438208734482,
			78.36638074368238);

	private GoogleMap map;
	private List<Toilet> toilets;

	public GoogleMap getMap() {
		return map;
	}

	public void setMap(GoogleMap map) {
		this.map = map;
	}

	public List<Toilet> getToilets() {
		return toilets;
	}

	public void setToilets(List<Toilet> toilets) {
		this.toilets = toilets;
	}

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("Starting Search");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_toilet);
		GPSTracker gps = new GPSTracker(SearchToilet.this);
		// check if GPS enabled
		if (gps.canGetLocation()) {

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			CameraPosition cLocation = new CameraPosition.Builder()
					.target(new LatLng(latitude, longitude)).zoom(15.5f)
					.bearing(300).tilt(50).build();

			map = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();

			map.moveCamera(CameraUpdateFactory.newCameraPosition(cLocation));
			map.addMarker(new MarkerOptions()
					.position(new LatLng(latitude, longitude))
					.title("You")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.icon_locator)));

			addToiletsAndDustbins(String.valueOf(latitude),
					String.valueOf(longitude), map);
			/*
			 * map.setOnMapClickListener(this);
			 * map.setOnMapLongClickListener(this);
			 * map.setOnCameraChangeListener(this);
			 */
			// \n is for new line
		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showSettingsAlert();
		}

	}

	private void addToiletsAndDustbins(String lat, String lag, GoogleMap map) {
		new GetToilets().execute(getUrl(lat, lag));

	}

	private URI getUrl(String _latitude, String _longitude) {
		String _location = _latitude + "," + _longitude;
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("key", GlobalConstants.APIKey));
		qparams.add(new BasicNameValuePair("location", _location));
		qparams.add(new BasicNameValuePair("sensor", "true"));
		qparams.add(new BasicNameValuePair("name", "toilet"));
		qparams.add(new BasicNameValuePair("radius", "10000"));

		URI uri = null;
		try {
			uri = URIUtils.createURI("https", "maps.googleapis.com", -1,
					"/maps/api/place/nearbysearch/json",
					URLEncodedUtils.format(qparams, "UTF-8"), null);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uri;

	}

	/*
	 * @Override public void onMapClick(LatLng point) {
	 * System.out.println("tapped, point=" + point); }
	 * 
	 * @Override public void onMapLongClick(LatLng point) { count++;
	 * System.out.println(count+" long pressed, point=" + point); }
	 * 
	 * @Override public void onCameraChange(final CameraPosition position) {
	 * System.out.println(position.toString()); } public static int count=0;
	 */

	private class GetToilets extends AsyncTask<URI, Integer, String> {

		@Override
		protected String doInBackground(URI... params) {
			HttpClient httpclient = new DefaultHttpClient();
			JSONObject finalResult = null;
			HttpGet httpget = new HttpGet(params[0]);
			try {
				HttpResponse response = httpclient.execute(httpget);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));
				StringBuilder builder = new StringBuilder();
				for (String line = null; (line = reader.readLine()) != null;) {
					builder.append(line).append("\n");
				}
				finalResult = new JSONObject(builder.toString());
				toilets = new TestingParsing().parseJSONObject(finalResult);

				System.out.println(finalResult);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if(toilets!=null){
			  for (Iterator iterator = toilets.iterator(); iterator.hasNext();) {
				  Toilet toilet = (Toilet) iterator.next();
				  map.addMarker(new MarkerOptions()
					.position(new LatLng(toilet.getLatitude(), toilet.getLongitude()))
					.title("toilet")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.icon_toilet_marker)));
				
			}
			}
			
		
		}

	}

}