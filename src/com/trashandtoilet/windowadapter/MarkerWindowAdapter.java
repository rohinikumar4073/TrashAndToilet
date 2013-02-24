package com.trashandtoilet.windowadapter;

import android.content.Context;
import android.view.View;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class MarkerWindowAdapter implements InfoWindowAdapter {
Context mContext;
	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		// TODO Auto-generated method stub
		return null;
	}
public MarkerWindowAdapter(Context context) {
	this.mContext=context;
	// TODO Auto-generated constructor stub
}
}
