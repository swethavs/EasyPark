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

public class TextSelectedListAdapter extends BaseAdapter {
	
	private static final String TAG = "TextSelectedListAdapter";

	/** Remember  context so we can use it when constructing views. */
	private Context mContext;

	private List<TextSelected> mItems = new ArrayList<TextSelected>();

	public TextSelectedListAdapter(Context context) {
		Log.i(TAG,"Inside textSelected List Adapter constructor");
		mContext = context;
		Log.i(TAG,"Inside textSelected List Adapter constructor after setting context");
	}

	public void addItem(TextSelected it) { mItems.add(it); }

	public void setListItems(List<TextSelected> lit) 
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
		TextSelectedView btv;
		btv = new TextSelectedView(mContext, mItems.get(position));
		return btv;
	}
}