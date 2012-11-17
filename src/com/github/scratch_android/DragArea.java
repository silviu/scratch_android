package com.github.scratch_android;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class DragArea extends Fragment implements View.OnDragListener{
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.motion_blocks, container, false);
		return v;
	}

	@Override
	public boolean onDrag(View v, DragEvent event) {
		View view = (View) event.getLocalState();
		int action = event.getAction();
		
		switch (action) {
		case DragEvent.ACTION_DRAG_STARTED:
			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			break;
		case DragEvent.ACTION_DRAG_LOCATION:
			break;
		case DragEvent.ACTION_DROP:
			break;
		case DragEvent.ACTION_DRAG_ENDED:
			break;
		}
		return false;
	}

}
