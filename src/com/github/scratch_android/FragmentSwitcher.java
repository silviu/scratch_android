package com.github.scratch_android;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentSwitcher extends Fragment {
	private FragmentSwitcherManager manager;
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_switcher_layout, container, false);
		manager = new FragmentSwitcherManager(v);
		
		return v;
	}
	
	public FragmentSwitcherManager get_manager() {
		return this.manager;
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