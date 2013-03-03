package com.trashntoilet.service;

import java.util.ArrayList;

import com.trashntoilet.R;
import com.trashntoilet.dto.Component;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomizedAdapter extends ArrayAdapter<Component> {
	private Context context;
	private int layoutResourceId;
	ArrayList<Component> data = null;

	public CustomizedAdapter(Context context, int layoutResourceId,
			ArrayList<Component> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ComponentHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = (((Activity) context).getLayoutInflater());
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new ComponentHolder();
			holder.imgIcon1 = (ImageView) row.findViewById(R.id.imgIcon1);
			holder.txtTitle1 = (TextView) row.findViewById(R.id.txtTitle1);
			holder.txtTitle2 = (TextView) row.findViewById(R.id.txtTitle2);
			holder.imgIcon2 = (ImageView) row.findViewById(R.id.imgIcon2);

			row.setTag(holder);
		} else {
			holder = (ComponentHolder) row.getTag();
		}
		Component component = data.get(position);
        double distance=component.getDistance();
        distance = Math.round( distance * 100.0 ) / 100.0;
		holder.txtTitle1.setText(component.getName());
		holder.txtTitle2.setText(distance+" KM");
		if (component.isToilet())
			holder.imgIcon1.setImageResource(R.drawable.icon_toilet_list_view);
		else if (component.isTrash())
			holder.imgIcon1.setImageResource(R.drawable.icon_dustbin_list_view);
		
		holder.imgIcon2.setImageResource(R.drawable.direction);
		holder.imgIcon2.setContentDescription(component.getLatitude()+"$"+component.getLongitude());
       return row;
	}

	static class ComponentHolder {
		ImageView imgIcon1;
		TextView txtTitle1;
		TextView txtTitle2;
		ImageView imgIcon2;

	}
}
