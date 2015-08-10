/***************************************************************
Copyright (c) 2015 swethavs

[This program is licensed under the "MIT License"]

Please see the file COPYING in the source

distribution of this software for license terms.
****************************************************************/


package com.swetha.easypark;

import java.util.LinkedHashMap;

public class RadioButtonTracker implements Comparable<RadioButtonTracker>{
    
	private LinkedHashMap<String, String> linkedHashMap = new  LinkedHashMap<String, String>();
	private boolean selected;
	
	public RadioButtonTracker(LinkedHashMap<String, String> map, boolean selected) {
		linkedHashMap = map;
		this.selected = selected;
	}
	
	public String getText() {
		return linkedHashMap.get("vacantspotdisplay");
	}
	
	public String getId(){
	return linkedHashMap.get("parkingspotid");
	}
	
	public void setText(LinkedHashMap<String, String> text) {
		linkedHashMap = text;
	}
	
	public boolean getSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	//@Override
	public int compareTo(RadioButtonTracker other) {
		if(this.linkedHashMap != null)
		
			return (this.linkedHashMap.get("vacantspotdisplay")).compareTo(other.linkedHashMap.get("vacantspotdisplay")); 
		
		else 
			throw new IllegalArgumentException();
		
	}
}
