package com.github.scratch_android;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class ToggleFragmentButton extends ImageView implements View.OnClickListener{
	private String selected_image_name;
	private String unselected_image_name;
	private boolean is_selected = false;
	public ToggleFragmentButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(this);
		init(context, attrs);
	}
	
	private void init(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ToggleFragmentButton);
		CharSequence s = a.getString(R.styleable.ToggleFragmentButton_selected_image_name);
        if (s != null) {
            this.selected_image_name = s.toString();
        }
        
        s = a.getString(R.styleable.ToggleFragmentButton_unselected_image_name);
        if (s != null) {
            this.unselected_image_name = s.toString();
        }
        if (this.unselected_image_name.equals("button_motion"))
            	this.is_selected = true;
        a.recycle();
	}

	@Override
	public void onClick(View view) {
		
		Log.v("Button", "Clicked");
		if (is_selected) {
			this.setImageResource(getResources().getIdentifier(this.unselected_image_name, "drawable", "com.github.scratch_android"));
			is_selected = false;
		}
		else {
			this.setImageResource(getResources().getIdentifier(this.selected_image_name, "drawable", "com.github.scratch_android"));
			is_selected = true;
		}
		
	}

}
