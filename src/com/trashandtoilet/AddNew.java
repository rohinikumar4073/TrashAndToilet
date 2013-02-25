package com.trashandtoilet;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.RadioButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.trashandtoilet.service.GPSTracker;

public class AddNew extends android.support.v4.app.FragmentActivity {
	private static final String MAP_FRAGMENT_TAG = "map";
	private GoogleMap mMap;
	private static String reportingType;
	private static double longitude;
	private static double latitude;

	public static String getReportingType() {
		return reportingType;
	}

	public static void setReportingType(String reportingType) {
		AddNew.reportingType = reportingType;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new);
		GPSTracker gps = new GPSTracker(AddNew.this);
		// check if GPS enabled
		if (gps.canGetLocation()) {
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
			CameraPosition cLocation = new CameraPosition.Builder()
					.target(new LatLng(latitude, longitude)).zoom(12.5f)
					.bearing(300).tilt(50).build();
			
			SupportMapFragment fragment=((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map1));
			mMap = fragment.getMap();
			mMap.clear();
			mMap.moveCamera(CameraUpdateFactory
					.newCameraPosition(cLocation));
			
			

		}
  
	}

	public void clickedToilet(View view) {
		ImageView imageView = (ImageView) view;
		imageView.setImageResource(R.drawable.icon_toliets_only);
		ImageView imageView2 = (ImageView) findViewById(R.id.imageView3);
		imageView2.setImageResource(R.drawable.icon_dustbin_deselect);
		reportingType = GlobalConstants.ONLY_TOILETS;

	}

	public void clickedDustbin(View view) {
		ImageView imageView = (ImageView) view;
		imageView.setImageResource(R.drawable.icon_dustbin);
		ImageView imageView2 = (ImageView) findViewById(R.id.imageView2);
		imageView2.setImageResource(R.drawable.icon_toilet_deselect);
		reportingType = GlobalConstants.ONLY_TRASH;

	}

	public void onRadioButtonClicked(View view) {
		RadioButton button = (RadioButton) view;
		String content = (String) button.getContentDescription();
		if (content.equals(GlobalConstants.LOCATION_ELSE_WHERE)) {
			
			findViewById(R.id.map1).setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 600 ));
			findViewById(R.id.map1).bringToFront();

		}
	}
}
