package com.trashandtoilet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class LoadingPage extends Activity {
	protected boolean _active = true;
	protected int _splashTime = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_loading_page);
		 Thread splashTread = new Thread() {
	            @Override
	            public void run() {
	                try {
	                    int waited = 0;
	                    while (_active && (waited < _splashTime)) {
	                        sleep(100);
	                        if (_active) {
	                            waited += 100;
	                        }
	                    }
	                } catch (Exception e) {

	                } finally {

	                    startActivity(new Intent(LoadingPage.this,
	                            SearchToilet.class));
LoadingPage.this.finish();
	                }
	}
		 };splashTread.start();
		 
	}

	

}
