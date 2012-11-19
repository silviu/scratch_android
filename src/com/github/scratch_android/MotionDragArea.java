package com.github.scratch_android;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MotionDragArea extends DragArea {
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.motion_blocks, container, false);
		Log.v("SETTING", "MANAGER");
		manager = new DragAreaManager(v);
		return v;
	}
}
