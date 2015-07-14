/***************************************************************
Copyright (c) 2015 swethavs

[This program is licensed under the "MIT License"]

Please see the file COPYING in the source

distribution of this software for license terms.
****************************************************************/


package com.swetha.easypark;

import java.util.LinkedHashMap;

public class TextSelected implements Comparable<TextSelected>{
    
	private LinkedHashMap<String, String> mText = new  LinkedHashMap<String, String>();
	private boolean mSelected;
	
	public TextSelected(LinkedHashMap<String, String> map, boolean selected) {
		mText = map;
		mSelected = selected;
	}
	
	public String getText() {
		return mText.get("vacantspotdisplay");
	}
	
	public String getId(){
	return mText.get("parkingspotid");
	}
	
	public void setText(LinkedHashMap<String, String> text) {
		mText = text;
	}
	
	public boolean getSelected() {
		return mSelected;
	}
	
	public void setSelected(boolean selected) {
		mSelected = selected;
	}

	//@Override
	public int compareTo(TextSelected other) {
		if(this.mText != null)
		
			return (this.mText.get("vacantspotdisplay")).compareTo(other.mText.get("vacantspotdisplay")); 
		
		else 
			throw new IllegalArgumentException();
		
	}
}
