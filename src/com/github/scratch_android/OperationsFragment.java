package com.github.scratch_android;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class OperationsFragment extends Fragment {
	RelativeLayout lay;
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.motion_blocks, container, false);
		return v;
	}
	
	private void open_images() {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
		params.leftMargin = 25;
		params.topMargin = 100;
		MotionCodeBlock op = new MotionCodeBlock(lay.getContext());
		lay.addView(op, params);
	}
}
