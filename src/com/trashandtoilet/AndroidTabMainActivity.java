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
	        search.setIndicator("Search", getResources().getDrawable(R.drawable.icon_search));
	        Intent searchToilet = new Intent(this, SearchToilet.class);
	        search.setContent(searchToilet);
	 
	        // Tab for Add
	        TabSpec addNew = tabHost.newTabSpec("AddNew");
	        addNew.setIndicator("AddNew", getResources().getDrawable(R.drawable.icon_add));
	        Intent addNewIntent = new Intent(this, AddNew.class);
	        addNew.setContent(addNewIntent);
	 
	        // Tab for Suggest
	        TabSpec suggest = tabHost.newTabSpec("Suggest");
	        suggest.setIndicator("Suggest", getResources().getDrawable(R.drawable.icon_suggest));
	        Intent suggestIntent = new Intent(this, SuggestNew.class);
	        suggest.setContent(suggestIntent);

	        // Tab for info
	        TabSpec info = tabHost.newTabSpec("Info");
	        info.setIndicator("info", getResources().getDrawable(R.drawable.icon_info));
	        Intent toiletOnlyIntent = new Intent(this, ViewToilets.class);
	        info.setContent(toiletOnlyIntent);
	 
	        // Adding all TabSpec to TabHost
	        tabHost.addTab(search); // Adding Search tab
	        tabHost.addTab(addNew); // Adding New tab
	        tabHost.addTab(suggest); // Adding Suggest tab
	        tabHost.addTab(info); // Adding Info tab

	 }
	}
