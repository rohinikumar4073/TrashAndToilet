package com.trashntoilet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.trashntoilet.dto.Result;
import com.trashntoilet.windowadapter.TestingParsing;

public class SuggestNew extends Activity {

	/*
	 * private static final String MAP_FRAGMENT_TAG = "map"; public static
	 * GoogleMap mMap; private static double longitude; private static double
	 * latitude;
	 */
	private static String reportingType;
	private static double reportingLatitude;
	private static double reportingLongitude;
	private static String reportingLocationType = GlobalConstants.LOCATION_HERE;;
	private Handler progressBarHandler = new Handler();
	private int progressBarStatus;
	private Context context;
	private ProgressDialog progressBar;

	public static double getReportingLatitude() {
		return reportingLatitude;
	}

	public static void setReportingLatitude(double reportingLatitude) {
		SuggestNew.reportingLatitude = reportingLatitude;
	}

	public static double getReportingLongitude() {
		return reportingLongitude;
	}

	public static void setReportingLongitude(double reportingLongitude) {
		SuggestNew.reportingLongitude = reportingLongitude;
	}

	public static String getReportingLocationType() {
		return reportingLocationType;
	}

	public static void setReportingLocationType(String reportingLocationType) {
		SuggestNew.reportingLocationType = reportingLocationType;
	}

	

	public static String getReportingType() {
		return reportingType;
	}

	public static void setReportingType(String reportingType) {
		SuggestNew.reportingType = reportingType;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggest_new);
		Bundle extras = getIntent().getExtras();
		context=this;
		if (extras != null) {
			reportingType = extras.getString(GlobalConstants.REPORT_TYPE);
			reportingLatitude = extras.getDouble(GlobalConstants.LAT);
			reportingLongitude = extras.getDouble(GlobalConstants.LONG);
			RadioButton button = (RadioButton) findViewById(R.id.radio_different_locaton);
			button.setChecked(true);
		}
		if (reportingType != null) {
			if (GlobalConstants.ONLY_TOILETS.equals(reportingType)) {
				ImageView imageView = (ImageView) findViewById(R.id.imageView2);
				imageView.setImageResource(R.drawable.icon_toliets_only);
			} else if (GlobalConstants.ONLY_TRASH.equals(reportingType)) {
				ImageView imageView = (ImageView) findViewById(R.id.imageView3);
				imageView.setImageResource(R.drawable.icon_dustbin);
			}

		}
		/*
		 * GPSTracker gps = new GPSTracker(AddNew.this); // check if GPS enabled
		 * if (gps.canGetLocation()) { latitude = gps.getLatitude(); longitude =
		 * gps.getLongitude(); CameraPosition cLocation = new
		 * CameraPosition.Builder() .target(new LatLng(latitude,
		 * longitude)).zoom(12.5f) .bearing(300).tilt(50).build();
		 * SupportMapFragment fragment=((SupportMapFragment)
		 * getSupportFragmentManager() .findFragmentById(R.id.map1)); mMap =
		 * fragment.getMap(); mMap.clear(); mMap.moveCamera(CameraUpdateFactory
		 * .newCameraPosition(cLocation));
		 * 
		 * 
		 * 
		 * }
		 */
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

	public void addNew(View view) {
	

		AlertDialog alertDialog = new AlertDialog.Builder(this).create();

		if (!GlobalConstants.ONLY_TOILETS.equals(reportingType)
				&& !GlobalConstants.ONLY_TRASH.equals(reportingType)) {
			alertDialog.setTitle("Error");
			alertDialog.setTitle("Please select toilet or trashcan");
			alertDialog.show();
			return;
		}

		if (GlobalConstants.LOCATION_HERE.equals(reportingLocationType)) {
			reportingLatitude = SearchTrashAndToilets.latitude;
			reportingLongitude = SearchTrashAndToilets.longitude;
		} else if (!GlobalConstants.LOCATION_ELSE_WHERE
				.equals(reportingLocationType)) {
			alertDialog.setTitle("Error");
			alertDialog.setMessage("Please select a location");
			alertDialog.show();
			return;


		}
       
		progressBar = new ProgressDialog(this);
		progressBar.setMessage("Saving Suggestion");
		progressBar.setCancelable(false);
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressBar.setProgress(0);
		progressBar.setMax(100);
		progressBar.show();
		URI [] uris = null;
	    new AddComponents().execute(uris);
	    new Thread(new Runnable() {
			  public void run() {
				while (progressBarStatus < 100) {

				  // process some tasks

				  // your computer is too fast, sleep 1 second
				  try {
					Thread.sleep(1000);
				  } catch (InterruptedException e) {
					e.printStackTrace();
				  }

				  // Update the progress bar
				  progressBarHandler.post(new Runnable() {
					public void run() {
					  progressBar.setProgress(progressBarStatus);
					}
				  });
				}

				// ok, file is downloaded,
				if (progressBarStatus >= 100) {

					// sleep 2 seconds, so that you can see the 100%
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					// close the progress bar dialog
					progressBar.dismiss();
				}
			  }
		       }).start();

	          
	    
	    
	    
		progressBarHandler.post(new Runnable() {
			public void run() {
				progressBar.setProgress(progressBarStatus);
			}
		});
	

	}

	public void onRadioButtonClicked(View view) {
		RadioButton button = (RadioButton) view;
		String content = (String) button.getContentDescription();
		if (content.equals(GlobalConstants.LOCATION_ELSE_WHERE)) {
			reportingLocationType = GlobalConstants.LOCATION_ELSE_WHERE;
			Intent intent = new Intent(this, SearchTrashAndToilets.class);
			intent.putExtra(GlobalConstants.FROM_VIEW, GlobalConstants.SUGGEST_NEW);
			intent.putExtra(GlobalConstants.REPORT_TYPE, reportingType);
			startActivity(intent);

		} else if (content.equals(GlobalConstants.LOCATION_HERE)) {
			reportingLocationType = GlobalConstants.LOCATION_HERE;

		}
	}

	private class AddComponents extends AsyncTask<URI, Integer, String> {
		public Result result;

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
			HttpPost httpPost = new HttpPost(getUrl());

			try {
				String json=getParams().toString();
				StringEntity se = new StringEntity(json);
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				httpPost.setEntity(se);
				HttpResponse response = httpclient.execute(httpPost);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));
				StringBuilder builder = new StringBuilder();
				for (String line = null; (line = reader.readLine()) != null;) {
					builder.append(line).append("\n");
				}
				finalResult = new JSONObject(builder.toString());

				 setResult(new TestingParsing()
						.parseResultJSONObject(finalResult));
				System.out.println(finalResult);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
			}
			return null;
		}

		private JSONObject getParams() {
			JSONObject location = new JSONObject();
			JSONObject requesetJson = new JSONObject();
			JSONArray typesArray = new JSONArray();
			typesArray.put("establishment");
			String name = null;
		   if (GlobalConstants.ONLY_TOILETS.equals(reportingType)) {
				name = GlobalConstants.SUGGEST_TOILETS;

			} else if (GlobalConstants.ONLY_TRASH.equals(reportingType)) {
				name = GlobalConstants.SUGGEST_TRASH;

			}
			try {
				location.put(TestingParsing.TAG_LATITUDE, reportingLatitude);
				location.put(TestingParsing.TAG_LONGITUDE, reportingLongitude);
				requesetJson.put(TestingParsing.TAG_LOCATION, location);
				requesetJson.put(TestingParsing.TAG_NAME, name);
				requesetJson.put(TestingParsing.ACCURACY,
						SearchTrashAndToilets.accuracy);
				requesetJson.put(TestingParsing.TAG_TYPES, typesArray);
				requesetJson.put(TestingParsing.LANGUAGE, "en");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return requesetJson;
		}

		@Override
		protected void onPostExecute(String str) {
			
			progressBar.dismiss();
			if("OK".equals(result.getStatus())){
				AlertDialog alertDialog = new AlertDialog.Builder(context).create();
				alertDialog.setTitle("Status");
				alertDialog.setMessage("Succesfully added !");
				alertDialog.show();
				GlobalConstants.ADD_OR_SUGGESTED=true;

			}else{
				AlertDialog alertDialog = new AlertDialog.Builder(context).create();
				alertDialog.setTitle("Status");
				alertDialog.setMessage(result.getStatus());
				alertDialog.show();
			}
	/*		SearchTrashAndToilets.toilets.clear();
			SearchTrashAndToilets.trashcans.clear();
*/
			
		}
		private URI getUrl() {
			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
			qparams.add(new BasicNameValuePair("key", GlobalConstants.APIKey));
			qparams.add(new BasicNameValuePair("sensor", "true"));

			URI uri = null;
			try {
				uri = URIUtils.createURI("https", "maps.googleapis.com", -1,
						"/maps/api/place/add/json",
						URLEncodedUtils.format(qparams, "UTF-8"), null);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return uri;

		}

		public Result getResult() {
			return result;
		}

		public void setResult(Result result) {
			this.result = result;
		}

	}

}
