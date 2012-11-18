package com.github.scratch_android;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ToggleFragmentButton extends ImageView{
	private String selected_image_name;
	private String unselected_image_name;
	private boolean is_selected = false;
	
	public ToggleFragmentButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}
	
	public boolean is_toggled() {
		return this.is_selected;
	}
	
	public void untoggle() {
		this.setImageResource(getResources().getIdentifier(this.unselected_image_name, "drawable", "com.github.scratch_android"));	
		is_selected = false;
	}
	
	public void toggle() {
		this.setImageResource(getResources().getIdentifier(this.selected_image_name, "drawable", "com.github.scratch_android"));	
		is_selected = true;
	}
	
	private void init(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ToggleFragmentButton);
		CharSequence s = a.getString(R.styleable.ToggleFragmentButton_selected_image_name);
        this.selected_image_name = s.toString();

        s = a.getString(R.styleable.ToggleFragmentButton_unselected_image_name);
        this.unselected_image_name = s.toString();        
        a.recycle();
	}
}
