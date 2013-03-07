package com.trashntoilet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.trashntoilet.dto.Component;
import com.trashntoilet.dto.ComponentComparatore;
import com.trashntoilet.service.CustomizedAdapter;
import com.trashntoilet.service.GPSTracker;
import com.trashntoilet.windowadapter.TestingParsing;

@SuppressLint("NewApi")
public class SearchTrashAndToilets extends FragmentActivity implements
		OnInfoWindowClickListener, OnMapLongClickListener/*
														 * implements'
														 * OnMapClickListener,
														 * OnMapLongClickListener
														 * ,
														 * OnCameraChangeListener
														 */{
	LatLng cLocation;
	public static String viewType = GlobalConstants.MAP_VIEW;
	public static String filterType = GlobalConstants.VIEW_ALL;
	public GoogleMap map;
	public static ArrayList<Component> toilets = new ArrayList<Component>();
	public static ArrayList<Component> trashcans = new ArrayList<Component>();
	public static double longitude;
	public static double latitude;
	public static float accuracy;
	private Handler progressBarHandler = new Handler();
	private int progressBarStatus;
	public ProgressDialog progressBar;
	public static String reportingType;
	public static String fromView;
	public Context mContext;
	public static int attempts;

	public GoogleMap getMap() {
		return map;
	}

	public void setMap(GoogleMap map) {
		this.map = map;
	}

	public ArrayList<Component> getToilets() {
		return toilets;
	}

	public void setToilets(ArrayList<Component> toilets) {
		SearchTrashAndToilets.toilets = toilets;
	}

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		mContext = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_toilet);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			fromView = extras.getString(GlobalConstants.FROM_VIEW);
			reportingType = extras.getString(GlobalConstants.REPORT_TYPE);
		}

		boolean mobileDataEnabled = false;
		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

		boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnectedOrConnecting();
		try {
			Class cmClass = Class.forName(manager.getClass().getName());
			Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
			method.setAccessible(true); // Make the method callable
			// get the setting for "mobile data"
			mobileDataEnabled = (Boolean) method.invoke(manager);
		} catch (Exception e) {
			// Some problem accessible private API
			// TODO do whatever error handling you want here
		}
		if (!mobileDataEnabled && !isWifi) {
			ViewGroup group = (ViewGroup) findViewById(R.layout.activity_search_toilet);
			if (group != null)
				group.invalidate();
			showDataSettingsAlert();
		} else {

			GPSTracker gps = new GPSTracker(SearchTrashAndToilets.this);
			// check if GPS enabled
			if (gps.canGetLocation()) {

				latitude = gps.getLatitude();
				longitude = gps.getLongitude();
				cLocation = new LatLng(latitude, longitude);
				CameraPosition cLocation = new CameraPosition.Builder()
						.target(new LatLng(latitude, longitude)).zoom(12.5f)
						.bearing(300).tilt(50).build();

				map = ((SupportMapFragment) getSupportFragmentManager()
						.findFragmentById(R.id.map)).getMap();
				map.moveCamera(CameraUpdateFactory.newCameraPosition(cLocation));
				Location locationNew = gps.getLocationDefault();
				if (locationNew != null)
					accuracy = locationNew.getAccuracy();
				// Vaish
				if (!GlobalConstants.ADD_NEW.equals(fromView)
						&& !GlobalConstants.SUGGEST_NEW.equals(fromView)) {
					map.setInfoWindowAdapter(new MarkerWindowAdapter(this));
					map.setOnInfoWindowClickListener(this);
					map.addMarker(new MarkerOptions()
							.position(new LatLng(latitude, longitude))
							.title("You")
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.icon_locator)));
					if ((toilets.size() > 0 || trashcans.size() > 0)
							&& !GlobalConstants.ADD_OR_SUGGESTED) {

						for (Iterator iterator = toilets.iterator(); iterator
								.hasNext();) {
							Component toilet = (Component) iterator.next();
							String status = "";
							if (GlobalConstants.NOT_EMPTY.equals(toilet
									.getOpeningHours())) {
								if (toilet.getOpenNow())
									status = "Opened";
								else
									status = "Closed";
							}
							Marker marker = map
									.addMarker(new MarkerOptions()
											.position(
													new LatLng(
															toilet.getLatitude(),
															toilet.getLongitude()))
											.title(toilet.getName())
											.icon(BitmapDescriptorFactory
													.fromResource(R.drawable.icons_toilet_marker)));
							if (!status.equals("")) {
								marker.setSnippet(status);

							}
						}
						for (Iterator iterator = trashcans.iterator(); iterator
								.hasNext();) {

							Component toilet = (Component) iterator.next();
							String status = "";
							if (GlobalConstants.NOT_EMPTY.equals(toilet
									.getOpeningHours())) {
								if (toilet.getOpenNow())
									status = "Opened";
								else
									status = "Closed";
							}
							Marker marker = map
									.addMarker(new MarkerOptions()
											.position(
													new LatLng(
															toilet.getLatitude(),
															toilet.getLongitude()))
											.title(toilet.getName())
											.icon(BitmapDescriptorFactory
													.fromResource(R.drawable.icon_dustbin_marker)));
							if (!status.equals("")) {
								marker.setSnippet(status);

							}
						}

					} else {
						if (!GlobalConstants.TRUE.equals(GPSTracker.error)) {
							progressBar = new ProgressDialog(this);
							progressBar
									.setMessage("Loading Toilets & TrashCans");
							progressBar.setCancelable(false);
							/*
							 * progressBar.setButton(DialogInterface.BUTTON_NEGATIVE
							 * , "Cancel", new DialogInterface.OnClickListener()
							 * {
							 * 
							 * @Override public void onClick(DialogInterface
							 * dialog, int which) { dialog.dismiss(); } });
							 */
							progressBar
									.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							progressBar.setProgress(0);
							progressBar.setMax(100);
							progressBar.show();
							progressBarHandler.post(new Runnable() {
								public void run() {
									progressBar.setProgress(progressBarStatus);
								}
							});

							addToiletsAndDustbins(String.valueOf(latitude),
									String.valueOf(longitude), map);
						}
					}
				} else {
					findViewById(R.id.imageView2).setLayoutParams(
							new LayoutParams(0, 0));
					findViewById(R.id.imageView3).setLayoutParams(
							new LayoutParams(0, 0));
					findViewById(R.id.imageView4).setLayoutParams(
							new LayoutParams(0, 0));
					findViewById(R.id.imageView5).setLayoutParams(
							new LayoutParams(0, 0));
					findViewById(R.id.imageView6).setLayoutParams(
							new LayoutParams(0, 0));
					findViewById(R.id.mapText).setLayoutParams(
							new LayoutParams(LayoutParams.FILL_PARENT,
									LayoutParams.FILL_PARENT));

					map.setOnMapLongClickListener(this);
				}
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
				gps.showGPSSettingsAlert();
			}
		}
	}

	private void addToiletsAndDustbins(String lat, String lag, GoogleMap map) {
		new GetToilets(GlobalConstants.ONLY_TOILETS).execute(getUrl(lat, lag,
				GlobalConstants.ONLY_TOILETS));
		new GetToilets(GlobalConstants.ONLY_TRASH).execute(getUrl(lat, lag,
				GlobalConstants.ONLY_TRASH));

	}

	private URI getUrl(String _latitude, String _longitude, String _searchKey) {
		String _location = _latitude + "," + _longitude;
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("key", GlobalConstants.APIKey));
		qparams.add(new BasicNameValuePair("location", _location));
		qparams.add(new BasicNameValuePair("sensor", "true"));
		qparams.add(new BasicNameValuePair("name", _searchKey));
		qparams.add(new BasicNameValuePair("radius", GlobalConstants.RADIUS));

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

	public List<Component> getDustbins() {
		return trashcans;
	}

	public void setDustbins(ArrayList<Component> trashcans) {
		SearchTrashAndToilets.trashcans = trashcans;
	}

	private class GetToilets extends AsyncTask<URI, Integer, String> {
		private String mode;

		public GetToilets(String mode) {
			this.mode = mode;
			// TODO Auto-generated constructor stub
		}

		@SuppressWarnings("unused")
		public GetToilets() {
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			setProgressPercent(progress[0]);
		}

		private void setProgressPercent(Integer integer) {
			progressBarStatus = integer;
		}

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
				if (mode.equals(GlobalConstants.ONLY_TOILETS)) {
					toilets = new TestingParsing().parseJSONObject(finalResult,
							mode, latitude, longitude);
				} else if (mode.equals(GlobalConstants.ONLY_TRASH)) {
					setDustbins(new TestingParsing().parseJSONObject(
							finalResult, mode, latitude, longitude));
				}
				System.out.println(finalResult);
			} catch (ClientProtocolException e) {
				e.printStackTrace();

			} catch (IOException e) {

				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			if (mode.equals(GlobalConstants.ONLY_TOILETS)) {
				if (toilets.size() == 0) {
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							mContext);

					// Setting Dialog Title
					alertDialog.setTitle("No toilets");

					// Setting Dialog Message
					alertDialog
							.setMessage("Sorry no toilets added for your place");
					alertDialog.show();
				}
				for (Iterator iterator = toilets.iterator(); iterator.hasNext();) {
					Component toilet = (Component) iterator.next();
					String status = "";
					if (GlobalConstants.NOT_EMPTY.equals(toilet
							.getOpeningHours())) {
						if (toilet.getOpenNow())
							status = "Opened";
						else
							status = "Closed";
					}
					Marker marker = map
							.addMarker(new MarkerOptions()
									.position(
											new LatLng(toilet.getLatitude(),
													toilet.getLongitude()))
									.title(toilet.getName())
									.icon(BitmapDescriptorFactory
											.fromResource(R.drawable.icons_toilet_marker)));
					if (!status.equals("")) {
						marker.setSnippet(status);

					}

				}
				// progressBar.dismiss();
			} else if (mode.equals(GlobalConstants.ONLY_TRASH)) {
				if (trashcans.size() == 0) {
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							mContext);

					// Setting Dialog Title
					alertDialog.setTitle("No Trashcan");

					// Setting Dialog Message
					alertDialog
							.setMessage("Sorry no trashcans added for your place");
					alertDialog.show();
				}
				GlobalConstants.ADD_OR_SUGGESTED = false;
				for (Iterator iterator = trashcans.iterator(); iterator
						.hasNext();) {
					Component toilet = (Component) iterator.next();
					String status = "";
					if (GlobalConstants.NOT_EMPTY.equals(toilet
							.getOpeningHours())) {
						if (toilet.getOpenNow())
							status = "Opened";
						else
							status = "Closed";
					}
					Marker marker = map
							.addMarker(new MarkerOptions()
									.position(
											new LatLng(toilet.getLatitude(),
													toilet.getLongitude()))
									.title(toilet.getName())
									.icon(BitmapDescriptorFactory
											.fromResource(R.drawable.icons_toilet_marker)));
					if (!status.equals("")) {
						marker.setSnippet(status);

					}
				}
			}
			progressBar.dismiss();
		}

		@SuppressWarnings("unused")
		public String getMode() {
			return mode;
		}

		@SuppressWarnings("unused")
		public void setMode(String mode) {
			this.mode = mode;
		}

	}

	public void onlyToilets(View view) {
		ImageView imageView = (ImageView) findViewById(R.id.imageView5);
		imageView.setImageResource(R.drawable.icon_toliets_only);
		imageView = (ImageView) findViewById(R.id.imageView4);
		imageView.setImageResource(R.drawable.icon_view_all_deslect);
		imageView = (ImageView) findViewById(R.id.imageView6);
		imageView.setImageResource(R.drawable.icon_dustbin_deselect);
		filterType = GlobalConstants.ONLY_TOILETS;
		if (viewType.equals(GlobalConstants.MAP_VIEW)) {
			map.clear();
			if (toilets.size() > 0 && latitude != 0 && longitude != 0) {
				map.addMarker(new MarkerOptions()
						.position(new LatLng(latitude, longitude))
						.title("You")
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.icon_locator)));
				for (Iterator iterator = toilets.iterator(); iterator.hasNext();) {
					Component toilet = (Component) iterator.next();
					map.addMarker(new MarkerOptions()
							.position(
									new LatLng(toilet.getLatitude(), toilet
											.getLongitude()))
							.title(toilet.getName())
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.icons_toilet_marker)));
				}
			}
		} else if (viewType.equals(GlobalConstants.LIST_VIEW)) {
			listView(GlobalConstants.ONLY_TOILETS);
		}
	}

	public void onlyTrashCans(View view) {
		filterType = GlobalConstants.ONLY_TRASH;
		ImageView imageView = (ImageView) findViewById(R.id.imageView6);
		imageView.setImageResource(R.drawable.icon_dustbin);
		imageView = (ImageView) findViewById(R.id.imageView4);
		imageView.setImageResource(R.drawable.icon_view_all_deslect);
		imageView = (ImageView) findViewById(R.id.imageView5);
		imageView.setImageResource(R.drawable.icon_toilet_deselect);
		if (viewType.equals(GlobalConstants.MAP_VIEW)) {
			map.clear();
			if (trashcans.size() > 0 && latitude != 0 && longitude != 0) {
				map.addMarker(new MarkerOptions()
						.position(new LatLng(latitude, longitude))
						.title("You")
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.icon_locator)));
				for (Iterator iterator = trashcans.iterator(); iterator
						.hasNext();) {
					Component toilet = (Component) iterator.next();
					map.addMarker(new MarkerOptions()
							.position(
									new LatLng(toilet.getLatitude(), toilet
											.getLongitude()))
							.title(toilet.getName())
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.icon_dustbin_marker)));
				}
			}
		} else if (viewType.equals(GlobalConstants.LIST_VIEW)) {
			listView(GlobalConstants.ONLY_TRASH);
		}

	}

	public void viewAll(View view) {
		ImageView imageView = (ImageView) findViewById(R.id.imageView4);
		imageView.setImageResource(R.drawable.icon_view_all);
		imageView = (ImageView) findViewById(R.id.imageView5);
		imageView.setImageResource(R.drawable.icon_toilet_deselect);
		imageView = (ImageView) findViewById(R.id.imageView6);
		imageView.setImageResource(R.drawable.icon_dustbin_deselect);
		filterType = GlobalConstants.VIEW_ALL;
		if (viewType.equals(GlobalConstants.MAP_VIEW)) {
			map.clear();
			if ((toilets.size() > 0 || trashcans.size() > 0) && latitude != 0
					&& longitude != 0) {
				map.addMarker(new MarkerOptions()
						.position(new LatLng(latitude, longitude))
						.title("You")
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.icon_locator)));
				for (Iterator iterator = trashcans.iterator(); iterator
						.hasNext();) {
					Component toilet = (Component) iterator.next();
					map.addMarker(new MarkerOptions()
							.position(
									new LatLng(toilet.getLatitude(), toilet
											.getLongitude()))
							.title(toilet.getName())
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.icon_dustbin_marker)));
				}
			}
			if (toilets.size() > 0 && latitude != 0 && longitude != 0) {
				for (Iterator iterator = toilets.iterator(); iterator.hasNext();) {
					Component toilet = (Component) iterator.next();
					map.addMarker(new MarkerOptions()
							.position(
									new LatLng(toilet.getLatitude(), toilet
											.getLongitude()))
							.title(toilet.getName())
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.icons_toilet_marker)));
				}
			}
		} else if (viewType.equals(GlobalConstants.LIST_VIEW)) {
			listView(GlobalConstants.VIEW_ALL);
		}
	}

	public void listView(View view) {
		viewType = GlobalConstants.LIST_VIEW;
		findViewById(R.id.map).setLayoutParams(
				new android.widget.RelativeLayout.LayoutParams(0, 0));
		ListView listView = (ListView) findViewById(R.id.list1);
		listView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));

		ImageView imageView = (ImageView) findViewById(R.id.imageView3);
		imageView.setImageResource(R.drawable.icon_list_view);
		imageView = (ImageView) findViewById(R.id.imageView2);
		imageView.setImageResource(R.drawable.icon_map_view_deselect);
		ArrayList<Component> components = null;
		if (filterType.equals(GlobalConstants.VIEW_ALL)) {
			components = new ArrayList<Component>(toilets);
			components.addAll(trashcans);
		} else if (filterType.equals(GlobalConstants.ONLY_TOILETS)) {
			components = toilets;
		} else if (filterType.equals(GlobalConstants.ONLY_TRASH)) {
			components = trashcans;
		}
		Collections.sort(components, new ComponentComparatore());
		CustomizedAdapter adapter = new CustomizedAdapter(this,
				R.layout.activity_list_view, components);
		listView.setAdapter(adapter);

	}

	public void listView(String filter) {
		findViewById(R.id.map).setLayoutParams(
				new android.widget.RelativeLayout.LayoutParams(0, 0));
		ListView listView = (ListView) findViewById(R.id.list1);
		listView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		ImageView imageView = (ImageView) findViewById(R.id.imageView3);
		imageView.setImageResource(R.drawable.icon_list_view);
		imageView = (ImageView) findViewById(R.id.imageView2);
		imageView.setImageResource(R.drawable.icon_map_view_deselect);
		ArrayList<Component> components = null;
		if (filter.equals(GlobalConstants.VIEW_ALL)) {
			components = new ArrayList<Component>(toilets);
			components.addAll(trashcans);
		} else if (filter.equals(GlobalConstants.ONLY_TOILETS)) {
			components = toilets;
		} else if (filter.equals(GlobalConstants.ONLY_TRASH)) {
			components = trashcans;
		}
		Collections.sort(components, new ComponentComparatore());
		CustomizedAdapter adapter = new CustomizedAdapter(this,
				R.layout.activity_list_view, components);
		listView.setAdapter(adapter);

	}

	public void mapView(View view) {
		viewType = GlobalConstants.MAP_VIEW;
		findViewById(R.id.list1).setLayoutParams(new LayoutParams(0, 0));
		findViewById(R.id.map)
				.setLayoutParams(
						new android.widget.RelativeLayout.LayoutParams(
								android.widget.RelativeLayout.LayoutParams.FILL_PARENT,
								android.widget.RelativeLayout.LayoutParams.FILL_PARENT));
		ImageView imageView = (ImageView) findViewById(R.id.imageView2);
		imageView.setImageResource(R.drawable.icon_map_view);
		imageView = (ImageView) findViewById(R.id.imageView3);
		imageView.setImageResource(R.drawable.icon_list_view_deselect);
	}

	@Override
	public void onInfoWindowClick(Marker marker) {

		LatLng destAddr = marker.getPosition();
		Double destLat = destAddr.latitude;
		Double destLon = destAddr.longitude;

		Double srcLat = cLocation.latitude;
		Double srcLon = cLocation.longitude;

		// System.out.println("URL = " + Uri.parse(
		// "http://maps.google.com/maps?" +
		// "saddr=" + srcLat + "," + srcLon + "&daddr=" + destLat + "," +
		// destLon));

		final Intent intent = new Intent(Intent.ACTION_VIEW,
		/** Using the web based turn by turn directions url. */

		Uri.parse("http://maps.google.com/maps?" + "saddr=" + srcLat + ","
				+ srcLon + "&daddr=" + destLat + "," + destLon));

		intent.setClassName("com.google.android.apps.maps",
				"com.google.android.maps.MapsActivity");
		startActivity(intent);
	}

	public void getDirections(View view) {
		ImageView view2 = (ImageView) view;
		String contentDescription = (String) view2.getContentDescription();
		String[] str = contentDescription.split("\\$");
		if (str.length == 2) {
			Double destLat = Double.parseDouble(str[0]);
			Double destLon = Double.parseDouble(str[1]);
			final Intent intent = new Intent(Intent.ACTION_VIEW,
			/** Using the web based turn by turn directions url. */

			Uri.parse("http://maps.google.com/maps?" + "saddr=" + latitude
					+ "," + longitude + "&daddr=" + destLat + "," + destLon));

			intent.setClassName("com.google.android.apps.maps",
					"com.google.android.maps.MapsActivity");
			startActivity(intent);
		}

	}

	public static LatLng reportingLatLng;

	@Override
	public void onMapLongClick(LatLng latLng) {
		reportingLatLng = latLng;
		map.clear();
		if (GlobalConstants.ONLY_TOILETS.equals(reportingType)) {
			map.addMarker(new MarkerOptions()
					.position(latLng)
					.title("toilet")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.icons_toilet_marker)));
		} else if (GlobalConstants.ONLY_TRASH.equals(reportingType)) {
			map.addMarker(new MarkerOptions()
					.position(latLng)
					.title("toilet")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.icon_dustbin_marker)));
		} else {
			map.addMarker(new MarkerOptions()
					.position(latLng)
					.title("toilet")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.icon_marker_black)));
		}

		findViewById(R.id.buttonConfirm)

				.setLayoutParams(
						new android.widget.RelativeLayout.LayoutParams(
								android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
								android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT));

		findViewById(R.id.buttonConfirm).setLeft(10);
		findViewById(R.id.buttonConfirm).setTop(10);

	}

	public void confirmButton(View view) {
		Intent intent = new Intent(this, AndroidTabMainActivity.class);
		intent.putExtra(GlobalConstants.LAT, reportingLatLng.latitude);
		intent.putExtra(GlobalConstants.LONG, reportingLatLng.longitude);
		intent.putExtra(GlobalConstants.FROM_VIEW, fromView);
		intent.putExtra(GlobalConstants.REPORT_TYPE, reportingType);
		startActivity(intent);

	}

	class MarkerWindowAdapter implements InfoWindowAdapter {
		Context mContext;

		@SuppressLint("ResourceAsColor")
		@Override
		public View getInfoContents(Marker marker) {

			LinearLayout layout = new LinearLayout(mContext);
			layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));

			LinearLayout layout2 = new LinearLayout(mContext);
			layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			layout2.setOrientation(LinearLayout.HORIZONTAL);
			TextView textView = new TextView(mContext);
			textView.setText(marker.getTitle());
			textView.setTextColor(Color.BLACK);
			textView.setTypeface(null, Typeface.BOLD);
			layout.addView(textView);
			TextView isOpen = new TextView(mContext);
			ImageView imageView = new ImageView(mContext);
			imageView.setImageResource(R.drawable.direction);
			String snippet = marker.getSnippet();
			if (snippet != null && !snippet.equals("")) {
				isOpen.setText(snippet);
				layout2.addView(isOpen);
				layout2.addView(imageView);
				layout.setOrientation(LinearLayout.VERTICAL);

				layout.addView(layout2);
			} else {
				layout.addView(imageView);

			}

			return layout;
		}

		@Override
		public View getInfoWindow(Marker marker) {

			return null;
		}

		public MarkerWindowAdapter(Context context) {
			this.mContext = context;
			// TODO Auto-generated constructor stub
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		super.onResume();
	}

	public String roundPosition(double latitude2) {
		// TODO Auto-generated method stub
		return String
				.valueOf((double) Math.round(latitude2 * 10000000) / 10000000);
	}

	public void showDataSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
	 {
			// Setting Dialog Title
			alertDialog.setTitle("Data Connection");

			// Setting Dialog Message
			alertDialog
					.setMessage("WiFi and data Connection is not enabled. Do you want to go to settings menu ?");
		}

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_WIRELESS_SETTINGS);
						mContext.startActivity(intent);
					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

}
