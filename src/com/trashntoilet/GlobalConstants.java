package com.trashntoilet;

import java.util.ArrayList;

public class GlobalConstants {
	public static final String RADIUS = "50000";
	public static final String REPORTING = "reporting";
	public static String VIEW_ALL = "viewAll";
	public static String APIKey = "AIzaSyDCJxCI4_aLf-SXLU716ZRlwWHdqlSFMZA";
	public static String mapUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
	public static String ONLY_TOILETS = "toilet";
	public static String ONLY_TRASH = "trashcan";
	public static String MAP_VIEW = "mapview";
	public static String LIST_VIEW = "listview";
	public static String LOCATION_HERE = "Report for this location";
	public static String LOCATION_ELSE_WHERE = "Report for different location";
	public static String REPORT_TYPE = "reportType";
	public static final String LAT = "latitude";
	public static final String LONG = "longitude";
	public static final String FROM_VIEW = "fromView";
	public static final String ADD_REPORT_URL = "maps.googleapis.com/maps/api/place/add/json";
	public static final String SUGGEST_TOILETS = "TandtSuggestType1";
	public static final String SUGGEST_TRASH = "TandtSuggestType2";
	public static final String ADD_NEW = "addNew";
	public static final String SUGGEST_NEW = "suggestNew";
	public static final String NOT_EMPTY = "notEmpty";
	public static final int NO_OF_ATTEMPTS = 12;
	public static final String TRUE = "true";
	public static ArrayList<String> sanitationTips = new ArrayList<String>();
	public static boolean ADD_OR_SUGGESTED =false;
	static {
		sanitationTips
				.add("Wash hands after using toilets with soap or hand wash");

		sanitationTips.add("Wash hands before eating your food");

		sanitationTips
				.add("Never wash vegetables after cutting, it will wash away nutrients from the vegetables");

		sanitationTips
				.add("Use organics while growing vegetables in your farm, it will add nutrition values.");

		sanitationTips.add("Never eat food kept in open place");

		sanitationTips
				.add("Always dump garbage in trash can. It will keep your surrounding hygienic");
		sanitationTips
				.add("Reuse kitchen water for gardening. It will naturally increase nutrition ingredients in plants");

		sanitationTips
				.add("Keep you wet and dry garbage separately. Separated garbage can be used for composting");
		sanitationTips
				.add("Water going out from industries should be processed and filtered before mixing with river water");
		sanitationTips.add("Use gourd to scoop drinking water");

		sanitationTips.add("Do not allow open defection near drinking water");

		sanitationTips
				.add("Standing water become breeding ground for insects and mosquitoes.");
	}
}
