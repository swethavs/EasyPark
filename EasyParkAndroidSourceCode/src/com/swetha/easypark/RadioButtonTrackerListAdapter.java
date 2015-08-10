/***************************************************************
Copyright (c) 2015 swethavs

[This program is licensed under the "MIT License"]

Please see the file COPYING in the source

distribution of this software for license terms.
****************************************************************/


package com.swetha.easypark;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class RadioButtonTrackerListAdapter extends BaseAdapter {
	
	private static final String TAG = "RadioButtonTrackerListAdapter";

	
	private Context thiscontext;

	private List<RadioButtonTracker> mItems = new ArrayList<RadioButtonTracker>();

	public RadioButtonTrackerListAdapter(Context context) {
		Log.i(TAG,"Inside RadioButtonTracker List Adapter constructor");
		thiscontext = context;
		Log.i(TAG,"Inside RadioButtonTracker List Adapter constructor after setting context");
	}

	public void addItem(RadioButtonTracker it) { mItems.add(it); }

	public void setListItems(List<RadioButtonTracker> lit) 
	{ mItems = lit; }

	/** @return The number of items in the list */
	public int getCount() { return mItems.size(); }

	public Object getItem(int position) 
	{ return mItems.get(position); }
	
	/** Use the array index as a unique id. */
	public long getItemId(int position) {
		return position;
	}

	
	public View getView(int position, View convertView, ViewGroup parent) {
		IndividualItemInListView btv;
		btv = new IndividualItemInListView(thiscontext, mItems.get(position));
		return btv;
	}
}