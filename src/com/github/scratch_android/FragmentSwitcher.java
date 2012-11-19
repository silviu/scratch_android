package com.github.scratch_android;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Fragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class FragmentSwitcher extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_switcher_layout, container, false);
		new FragmentSwitcherManager(v);
		return v;
	}
}

class FragmentSwitcherManager implements View.OnClickListener {
	private View v;
	private ArrayList<ToggleFragmentButton> listeners = new ArrayList<ToggleFragmentButton>();
	
	public FragmentSwitcherManager(View v) {
		this.v = v;
		construct_listeners();
		listeners.get(0).toggle();
	}
	
	public void construct_listeners() {
		ViewGroup container = (ViewGroup) this.v.findViewById(R.id.button_container);
		int child_count =  container.getChildCount();
		for (int i = 0; i < child_count; i++) {
			ToggleFragmentButton tfb = (ToggleFragmentButton) container.getChildAt(i);
			tfb.setOnClickListener(this);
			listeners.add(tfb);
		}
	}
	@Override
	public void onClick(View button) {
		for (ToggleFragmentButton bt : listeners) {
			if (bt == button) {
				bt.toggle();
				continue;
			}
			if (bt.is_toggled())
				bt.untoggle();
		}	
	}
}

class ToggleFragmentButton extends ImageView{
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
