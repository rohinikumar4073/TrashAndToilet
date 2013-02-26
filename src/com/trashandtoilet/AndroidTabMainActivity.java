package com.trashandtoilet;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class AndroidTabMainActivity extends TabActivity {
	 @SuppressWarnings("deprecation")
	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_tab_layout);
	 
	        TabHost tabHost = getTabHost();
	 
	        // Tab for Search
	        TabSpec search = tabHost.newTabSpec("Search");
	        search.setIndicator("", getResources().getDrawable(R.drawable.icon_search));
	        Intent searchToilet = new Intent(this, SearchTrashAndToilets.class);
	        search.setContent(searchToilet);
	 
	        // Tab for Add
	        TabSpec addNew = tabHost.newTabSpec("AddNew");
	        addNew.setIndicator("", getResources().getDrawable(R.drawable.icon_add));
	        Intent addNewIntent = new Intent(this, AddNew.class);
	        addNew.setContent(addNewIntent);
	       
	 
	        // Tab for Suggest
	        TabSpec suggest = tabHost.newTabSpec("Suggest");
	        suggest.setIndicator("", getResources().getDrawable(R.drawable.icon_suggest));
	        Intent suggestIntent = new Intent(this, SuggestNew.class);
	        suggest.setContent(suggestIntent);

	        // Tab for info
	        TabSpec info = tabHost.newTabSpec("Info");
	        info.setIndicator("", getResources().getDrawable(R.drawable.icon_info));
	        Intent toiletOnlyIntent = new Intent(this, Info.class);
	        info.setContent(toiletOnlyIntent);
	 
	        // Adding all TabSpec to TabHost
	        tabHost.addTab(search); // Adding Search tab
	        tabHost.addTab(addNew); // Adding New tab
	        tabHost.addTab(suggest); // Adding Suggest tab
	        tabHost.addTab(info); // Adding Info tab
	        
	        Bundle extras = getIntent().getExtras();
	        String fromView=null;
			if (extras != null) {
				
				fromView = extras.getString(GlobalConstants.FROM_VIEW);
				if(GlobalConstants.ADD_NEW.equals(fromView)){
				addNewIntent.putExtra(GlobalConstants.REPORT_TYPE,extras.getString(GlobalConstants.REPORT_TYPE));
				addNewIntent.putExtra(GlobalConstants.LAT,extras.getDouble(GlobalConstants.LAT));
				addNewIntent.putExtra(GlobalConstants.LONG,extras.getDouble(GlobalConstants.LONG));
				addNewIntent.putExtra(GlobalConstants.FROM_VIEW, GlobalConstants.ADD_NEW);
				tabHost.setCurrentTab(1);
				}
				else if(GlobalConstants.SUGGEST_NEW.equals(fromView)){
					suggestIntent.putExtra(GlobalConstants.REPORT_TYPE,extras.getString(GlobalConstants.REPORT_TYPE));
					suggestIntent.putExtra(GlobalConstants.LAT,extras.getDouble(GlobalConstants.LAT));
					suggestIntent.putExtra(GlobalConstants.LONG,extras.getDouble(GlobalConstants.LONG));
					suggestIntent.putExtra(GlobalConstants.FROM_VIEW, GlobalConstants.SUGGEST_NEW);

					tabHost.setCurrentTab(2);
					}

			}

	 }
	}
