package com.trashandtoilet.service;

import java.util.ArrayList;

import com.trashandtoilet.R;
import com.trashandtoilet.dto.Component;

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
			holder.imgIcon = (ImageView) row.findViewById(R.id.imgIcon);
			holder.txtTitle = (TextView) row.findViewById(R.id.txtTitle);

			row.setTag(holder);
		} else {
			holder = (ComponentHolder) row.getTag();
		}

		Component component = data.get(position);
		holder.txtTitle.setText(component.getName());
		if (component.isToilet())
			holder.imgIcon.setImageResource(R.drawable.icon_toliets_only);
		else if (component.isTrash())
			holder.imgIcon.setImageResource(R.drawable.icon_dustbin);

		return row;
	}

	static class ComponentHolder {
		ImageView imgIcon;
		TextView txtTitle;
	}
}
