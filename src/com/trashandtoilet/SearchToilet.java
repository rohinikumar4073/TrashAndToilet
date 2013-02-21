package com.trashandtoilet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.trashandtoilet.service.GPSTracker;

@SuppressLint("NewApi")
public class SearchToilet extends FragmentActivity /*
													 * implements'
													 * OnMapClickListener,
													 * OnMapLongClickListener,
													 * OnCameraChangeListener
													 */{
	static final LatLng SecondToilet = new LatLng(17.447729806707645,
			78.3633230254054);
	static final  LatLng FirstToilet = new LatLng(17.4438208734482,
			78.36638074368238);
	
	
	private GoogleMap map;

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
			map.addMarker(new MarkerOptions().position(
					new LatLng(latitude, longitude)).title("You").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_locator)));

			addToiletsAndDustbins(map);
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

	private void addToiletsAndDustbins(GoogleMap map) {
		map.addMarker(new MarkerOptions().position(FirstToilet).title("toilet").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_toilet_marker)));
		map.addMarker(new MarkerOptions().position(SecondToilet)
				.title("Dustbin").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_dustbin_marker)));

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
}