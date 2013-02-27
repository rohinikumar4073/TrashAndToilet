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
	private   boolean whySuggest=false;

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
    		findViewById(R.id.layout6).setLayoutParams(
    				new LayoutParams(0,0));
    	
        }else{
        	setSanitationTipStatus(true);
        	setWhySuggest(false);
        	setWhyUseStatus(false);
    		imageView.setImageResource(R.drawable.icon_go_down);
        	ImageView firstView = (ImageView)findViewById(R.id.imgIcon1);
        	firstView.setImageResource(R.drawable.icon_go_right);
        	ImageView secondView = (ImageView)findViewById(R.id.imgIcon2);
        	secondView.setImageResource(R.drawable.icon_go_right);
    		TextView content = (TextView) findViewById(R.id.content4);
    		Collections.shuffle(GlobalConstants.sanitationTips);
    		String s = GlobalConstants.sanitationTips.get(0);
    		content.setText(s);
    		findViewById(R.id.layout2).setLayoutParams(
    				new LayoutParams(0,0));
    		findViewById(R.id.layout4).setLayoutParams(
    				new LayoutParams(0,0));
    		findViewById(R.id.layout6).setLayoutParams(
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
        	setSanitationTipStatus(false);
        	setWhySuggest(false);
    		imageView.setImageResource(R.drawable.icon_go_down);
    		ImageView secondView = (ImageView)findViewById(R.id.imgIcon2);
    		secondView.setImageResource(R.drawable.icon_go_right);
        	ImageView thirdView = (ImageView)findViewById(R.id.imgIcon3);
        	thirdView.setImageResource(R.drawable.icon_go_right);
    		findViewById(R.id.layout2).setLayoutParams(
    				new LayoutParams(LayoutParams.FILL_PARENT,
    						LayoutParams.WRAP_CONTENT));
    		findViewById(R.id.layout4).setLayoutParams(
    				new LayoutParams(0,0));
    		findViewById(R.id.layout6).setLayoutParams(
    				new LayoutParams(0,0));

        }
		
		

	}

	public void whySuggest(View view){
		ImageView imageView = (ImageView)view;
        if(isWhySuggest()){
        	setWhySuggest(false);
    		imageView.setImageResource(R.drawable.icon_go_right);
    		findViewById(R.id.layout4).setLayoutParams(
    				new LayoutParams(0,0));
    	
        }else{
        	setWhyUseStatus(false);
        	setSanitationTipStatus(false);
        	setWhySuggest(true);
    		imageView.setImageResource(R.drawable.icon_go_down);
    		ImageView firstView = (ImageView)findViewById(R.id.imgIcon1);
    		firstView.setImageResource(R.drawable.icon_go_right);
        	ImageView thirdView = (ImageView)findViewById(R.id.imgIcon3);
        	thirdView.setImageResource(R.drawable.icon_go_right);
    		findViewById(R.id.layout4).setLayoutParams(
    				new LayoutParams(LayoutParams.FILL_PARENT,
    						LayoutParams.WRAP_CONTENT));
    		findViewById(R.id.layout2).setLayoutParams(
    				new LayoutParams(0,0));
    		findViewById(R.id.layout6).setLayoutParams(
    				new LayoutParams(0,0));

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

	public boolean isWhySuggest() {
		return whySuggest;
	}

	public void setWhySuggest(boolean whySuggest) {
		this.whySuggest = whySuggest;
	}

}

