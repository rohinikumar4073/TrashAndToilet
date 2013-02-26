package com.trashandtoilet;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
	protected boolean _active = true;
	protected int _splashTime = 3000;
	protected static  boolean firstTime=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (firstTime) {
			
		
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

	                    startActivity(new Intent(MainActivity.this,
	                            AndroidTabMainActivity.class));
	                    MainActivity.this.finish();
	                }
	}
		 };splashTread.start();
		 
		}else{
			firstTime=false;
			startActivity(new Intent(this,AndroidTabMainActivity.class));
		}
		 
	}
	}

