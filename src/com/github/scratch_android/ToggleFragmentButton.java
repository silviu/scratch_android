package com.github.scratch_android;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class ToggleFragmentButton extends ImageView implements OnToggleListener{
	private String selected_image_name;
	private String unselected_image_name;
	private int id;
	private boolean is_selected = false;
	
	private ArrayList<ToggleFragmentButton> listeners = new ArrayList<ToggleFragmentButton>();
	private final int N = 8;
	private final int[] buttons = { R.id.motion_button_id, R.id.looks_button_id, 
									  R.id.sound_button_id, R.id.pen_button_id, 
									  R.id.control_button_id, R.id.sensing_button_id, 
									  R.id.operations_fragment, R.id.variables_button_id};
	public ToggleFragmentButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(this);
		init(context, attrs);
		construct_listeners();
	}
	
	private void construct_listeners() {
		for (int i = 0; i < N; i++) {
			if (buttons[i] == this.id)
				continue;
			listeners.add((ToggleFragmentButton) findViewById(buttons[i]));
		}
		Log.v("??????????????", String.valueOf(listeners.size()));
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
        
        this.id = this.getId();//a.getInteger(R.styleable.ToggleFragmentButton_button_id, -1);
        Log.v(String.valueOf(R.id.motion_button_id), String.valueOf(this.id));
        
        if (this.unselected_image_name.equals("button_motion"))
            	this.is_selected = true;
        a.recycle();
	}

	@Override
	public void onClick(View view) {
		
		Log.v("Button", "Clicked");
		if (is_selected) {
			return;
		}
		else {
			for (ToggleFragmentButton l : listeners)
	            l.onToggle();
	    }

			this.setImageResource(getResources().getIdentifier(this.selected_image_name, "drawable", "com.github.scratch_android"));	
			is_selected = true;
		}

	@Override
	public void onToggle() {
		if (is_selected) {
			this.setImageResource(getResources().getIdentifier(this.unselected_image_name, "drawable", "com.github.scratch_android"));	
			is_selected = false;
		}
	}
}

interface OnToggleListener extends View.OnClickListener{
    public void onToggle();
}

