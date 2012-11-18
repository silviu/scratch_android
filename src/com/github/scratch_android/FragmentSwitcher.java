package com.github.scratch_android;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentSwitcher extends Fragment implements View.OnClickListener{
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_switcher_layout, container, false);
		return v;
	}

	@Override
	public void onClick(View v) {
	}

}
