/***************************************************************
Copyright (c) 2015 swethavs

[This program is licensed under the "MIT License"]

Please see the file COPYING in the source

distribution of this software for license terms.
****************************************************************/


package com.swetha.easypark;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class TextSelectedView extends LinearLayout {
	private TextView mText;
	private TextView mTextId;
	private RadioButton mRadioBtn;
	private TextSelected mTextSelected;
	public TextSelectedView(Context context, final TextSelected textSelected) {
		super(context);
		Log.i("TextSelectedView","Inside textSelected view constructor: textselected");
		
		/* First Text to the right (horizontal),
		 * not above and below (vertical) */
		this.setOrientation(HORIZONTAL);
		mTextSelected = textSelected;
		
		mText = new TextView(context);
		mText.setPadding(15, 0, 0, 0);
		mText.setTypeface(Typeface.SERIF, 0);
		Log.i("TextSelectedView","Inside textSelected view constructor after Typeface");
		mText.setText(textSelected.getText());
		LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.weight = 1;
		params.gravity = Gravity.CENTER_VERTICAL;
		addView(mText, params);
		
		
		mTextId = new TextView(context);
		mTextId.setPadding(15, 0, 0, 0);
		mTextId.setVisibility(View.GONE);
		mTextId.setText(textSelected.getId());
		params.weight = 2;
		addView(mTextId, params);
		//add radio button
		mRadioBtn = new RadioButton(context);
		mRadioBtn.setChecked(textSelected.getSelected());
		mRadioBtn.setFocusable(false);
		mRadioBtn.setBackgroundColor(Color.GRAY);
		mRadioBtn.setClickable(false);
		addView(mRadioBtn, new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}

	public void setText(String words) {
		mText.setText(words);
	}
	
	public void setSelected(boolean selected) {
		mRadioBtn.setChecked(selected);
		mTextSelected.setSelected(selected);
	}
	
	public boolean getSelected() {
		return mTextSelected.getSelected();
	}
}