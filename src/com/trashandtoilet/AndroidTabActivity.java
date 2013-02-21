package com.trashandtoilet;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class AndroidTabActivity extends TabActivity {
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.tablayout);
	 
	        TabHost tabHost = getTabHost();
	 
	        // Tab for Photos
	        TabSpec ViewAll = tabHost.newTabSpec("View All");
	        // setting Title and Icon for the Tab
	        ViewAll.setIndicator("ViewAll", getResources().getDrawable(R.drawable.ic_map_view));
	        Intent photosIntent = new Intent(this, ViewAll.class);
	        ViewAll.setContent(photosIntent);
	 
	        // Tab for Songs
	        TabSpec mapView = tabHost.newTabSpec("MapView");
	        mapView.setIndicator("MapView", getResources().getDrawable(R.drawable.ic_map_view));
	        Intent mapViewIntent = new Intent(this, MapActivity.class);
	        mapView.setContent(mapViewIntent);
	 
	        // Tab for Videos
	        TabSpec listSpec = tabHost.newTabSpec("ListView");
	        listSpec.setIndicator("Videos", getResources().getDrawable(R.drawable.ic_map_view));
	        Intent ListIntent = new Intent(this, ListActivity.class);
	        listSpec.setContent(ListIntent);

	        // Tab for Songs
	        TabSpec toiletsOnly = tabHost.newTabSpec("toiletsOnly");
             toiletsOnly.setIndicator("toiletsOnly", getResources().getDrawable(R.drawable.ic_map_view));
	        Intent toiletOnlyIntent = new Intent(this, ViewToilets.class);
	        toiletsOnly.setContent(toiletOnlyIntent);
	 
	        // Tab for Videos
	        TabSpec trashOnly = tabHost.newTabSpec("");
	        trashOnly.setIndicator("toiletsOntrashOnlyly", getResources().getDrawable(R.drawable.ic_map_view));
	        Intent trashIntent = new Intent(this, ViewTrash.class);
	        trashOnly.setContent(trashIntent);
	 
	        // Adding all TabSpec to TabHost
	        tabHost.addTab(mapView); // Adding photos tab
	        tabHost.addTab(listSpec); // Adding songs tab
	        tabHost.addTab(ViewAll); // Adding videos tab
	        tabHost.addTab(toiletsOnly); // Adding songs tab
	        tabHost.addTab(trashOnly); // Adding videos tab
  
	 }
	}





