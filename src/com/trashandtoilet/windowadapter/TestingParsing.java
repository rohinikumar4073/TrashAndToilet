package com.trashandtoilet.windowadapter;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.trashandtoilet.dto.Photo;
import com.trashandtoilet.dto.Toilet;

public class TestingParsing {
	private static final String TAG_RESULTS = "results";
	private static final String TAG_GEOMETRY = "geometry";
	private static final String TAG_LOCATION = "location";
	private static final String TAG_LATITUDE = "lat";
	private static final String TAG_LONGITUDE = "lng";
	private static final String TAG_ICON = "icon";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_OPENING_HRS = "opening_hours";
	private static final String TAG_TYPES = "types";
	private static final String TAG_REFERENCE = "reference";
	private static final String OPEN_NOW = "open_now";
	private static final String TAG_PHOTOS = "photos";
	private static final String TAG_HEIGHT = "height";
	private static final String TAG_WIDTH = "width";
	private static final String TAG_PHOTO_REFERENCE = "photo_reference";
	public List<Toilet> parseJSONObject(JSONObject finalResult) {
 	ArrayList<Toilet> toilets = new ArrayList<Toilet>();
		try {
			JSONArray results = finalResult.getJSONArray(TAG_RESULTS);
			for (int i = 0; i < results.length(); i++) {
				Toilet toilet = new Toilet();
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
				toilets.add(toilet);
				}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toilets;

	}

}
