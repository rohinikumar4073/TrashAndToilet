package com.trashntoilet.windowadapter;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.trashntoilet.AddNew;
import com.trashntoilet.GlobalConstants;
import com.trashntoilet.dto.Component;
import com.trashntoilet.dto.Photo;
import com.trashntoilet.dto.Result;

public class TestingParsing {


	public static final String TAG_RESULTS = "results";
	public static final String TAG_GEOMETRY = "geometry";
	public static final String TAG_LOCATION = "location";
	public static final String TAG_LATITUDE = "lat";
	public static final String TAG_LONGITUDE = "lng";
	public static final String TAG_ICON = "icon";
	public static final String TAG_ID = "id";
	public static final String TAG_NAME = "name";
	public static final String TAG_OPENING_HRS = "opening_hours";
	public static final String TAG_TYPES = "types";
	public static final String TAG_REFERENCE = "reference";
	public static final String OPEN_NOW = "open_now";
	public static final String TAG_PHOTOS = "photos";
	public static final String TAG_HEIGHT = "height";
	public static final String TAG_WIDTH = "width";
	public static final String TAG_PHOTO_REFERENCE = "photo_reference";
	public static final String ACCURACY = "accuracy";
	public static final String LANGUAGE = "language";
	private static final String TAG_STATUS = "status";
	public ArrayList<Component> parseJSONObject(JSONObject finalResult, String mode, double latitudeHome, double longitudeHome) {
 	ArrayList<Component> toilets = new ArrayList<Component>();
		try {
			JSONArray results = finalResult.getJSONArray(TAG_RESULTS);
			for (int i = 0; i < results.length(); i++) {
				Component toilet = new Component();
				JSONObject object = results.getJSONObject(i);
				String name = object.optString(TAG_NAME);
				if (name != null && !name.equals("")) {
					toilet.setName(name);
				}
				String id = object.optString(TAG_ID);
				if (id != null && !id.equals("")) {
					toilet.setId(id);
				}
				String types = object.optString(TAG_TYPES);
				if (types != null && !types.equals("")) {
					toilet.setTypes(types);
				}
				String iconLink = object.optString(TAG_ICON);
				if (iconLink != null && !iconLink.equals(""))  {
					toilet.setIconLink(iconLink);
				}
				String reference = object.optString(TAG_REFERENCE);
				if (reference != null && !reference.equals("") ) {
					toilet.setReference(reference);
				}
				JSONObject geometry = object.optJSONObject(TAG_GEOMETRY);
				if (geometry != null) {
					JSONObject location = geometry.optJSONObject(TAG_LOCATION);
					if (location != null) {
						String latitude = location.optString(TAG_LATITUDE);
					if (latitude != null && !latitude.equals("") ) {
							toilet.setLatitude(Double.parseDouble(latitude));
						}
						String longitude = location.optString(TAG_LONGITUDE);
						if (longitude != null  && !latitude.equals("") ) {
							toilet.setLongitude(Double.parseDouble(longitude));
						}
					}
				}
				JSONObject openingHrs = object.optJSONObject(TAG_OPENING_HRS);
				if (openingHrs != null) {
					Boolean openNow = openingHrs.optBoolean(OPEN_NOW);
					toilet.setOpenNow(openNow);
				}
				Photo photo = new Photo();
				JSONArray photos = object.optJSONArray(TAG_PHOTOS);
				if (photos != null) {
					JSONObject photo2 = photos.optJSONObject(0);
					if (photo2 != null) {
						photo.setHeight(photo2.optString(TAG_HEIGHT));
						photo.setWidth(photo2.optString(TAG_WIDTH));
						photo.setPhotoReference(photo2
								.optString(TAG_PHOTO_REFERENCE));

					}
				}
				if(photo!=null)
				toilet.setPhotos(photo);
				if (mode.equals(GlobalConstants.ONLY_TOILETS)) {
					toilet.setToilet(true);
				} else if (mode.equals(GlobalConstants.ONLY_TRASH)) {
					toilet.setTrash(true);
				}
				toilet.setDistance(distance(latitudeHome, longitudeHome, toilet.getLatitude(), toilet.getLongitude(), 'K'));
				toilets.add(toilet);

				}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toilets;

	}
	
	
	private static double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
	      double theta = lon1 - lon2;
	      double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
	      dist = Math.acos(dist);
	      dist = rad2deg(dist);
	      dist = dist * 60 * 1.1515;
	      if (unit == 'K') {
	        dist = dist * 1.609344;
	      } else if (unit == 'N') {
	        dist = dist * 0.8684;
	        }
	      return (dist);
	    }

	    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	    /*::  This function converts decimal degrees to radians             :*/
	    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	    private static double deg2rad(double deg) {
	      return (deg * Math.PI / 180.0);
	    }

	    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	    /*::  This function converts radians to decimal degrees             :*/
	    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	    private static double rad2deg(double rad) {
	      return (rad * 180.0 / Math.PI);
	    }


		public void addNewComponent(AddNew addNew) {
			// TODO Auto-generated method stub
			
		}


		public Result parseResultJSONObject(JSONObject finalResult) {
			Result result=new Result();
			result.setStatus(finalResult.optString(TAG_STATUS));
			result.setReference(finalResult.optString(TAG_REFERENCE));
			result.setId(finalResult.optString(TAG_ID));
			return result;


		}

}
