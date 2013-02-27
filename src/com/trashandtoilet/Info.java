package com.trashandtoilet;

import java.util.Collections;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class Info extends Activity {
	private  boolean sanitationTipStatus=false;
	private   boolean whyUseStatus=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
	}

	public void getSaniTips(View view) {
    	ImageView imageView = (ImageView)view;

        if(isSanitationTipStatus()){
        	setSanitationTipStatus(false);
    		imageView.setImageResource(R.drawable.icon_go_right);
    		findViewById(R.id.layout4).setLayoutParams(
    				new LayoutParams(0,0));
    	
        }else{
        	setSanitationTipStatus(true);
    		imageView.setImageResource(R.drawable.icon_go_down);
    		TextView content = (TextView) findViewById(R.id.content);
    		Collections.shuffle(GlobalConstants.sanitationTips);
    		String s = GlobalConstants.sanitationTips.get(0);
    		content.setText(s);
    		findViewById(R.id.layout4).setLayoutParams(
    				new LayoutParams(LayoutParams.FILL_PARENT,
    						LayoutParams.WRAP_CONTENT));

        }
		
		


	}

	public void getInfo(View view) {
    	ImageView imageView = (ImageView)view;
        if(isWhyUseStatus()){
        	setSanitationTipStatus(false);
    		imageView.setImageResource(R.drawable.icon_go_right);
    		findViewById(R.id.layout2).setLayoutParams(
    				new LayoutParams(0,0));
    	
        }else{
        	setWhyUseStatus(true);
    		imageView.setImageResource(R.drawable.icon_list_view);
    		imageView.setImageResource(R.drawable.icon_go_down);
    		
    		findViewById(R.id.layout2).setLayoutParams(
    				new LayoutParams(LayoutParams.FILL_PARENT,
    						LayoutParams.WRAP_CONTENT));

        }
		
		

	}

	public boolean isSanitationTipStatus() {
		return sanitationTipStatus;
	}

	public void setSanitationTipStatus(boolean sanitationTipStatus) {
		this.sanitationTipStatus = sanitationTipStatus;
	}

	public boolean isWhyUseStatus() {
		return whyUseStatus;
	}

	public void setWhyUseStatus(boolean whyUseStatus) {
		this.whyUseStatus = whyUseStatus;
	}

}

