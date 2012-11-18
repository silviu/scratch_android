package com.github.scratch_android;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentSwitcher extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_switcher_layout, container, false);
		new Manager(v);
		return v;
	}
}

class Manager implements View.OnClickListener {
	private View v;
	private ArrayList<ToggleFragmentButton> listeners = new ArrayList<ToggleFragmentButton>();

	private final int N = 8;
	private final int[] buttons = { R.id.motion_button_id,    R.id.looks_button_id, 
									  R.id.sound_button_id,     R.id.pen_button_id, 
									  R.id.control_button_id,   R.id.sensing_button_id, 
									  R.id.operators_button_id, R.id.variables_button_id};
	public Manager(View v) {
		this.v = v;
		construct_listeners();
	}
	
	public void construct_listeners() {
		for (int i = 0; i < N; i++) {
			ToggleFragmentButton tfb = (ToggleFragmentButton) this.v.findViewById(buttons[i]);
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