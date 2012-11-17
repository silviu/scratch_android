package com.github.scratch_android;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class DropArea extends Fragment implements View.OnDragListener{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.code_layout, container, false);
		v.setOnDragListener(this);
		return v;
	}
	@Override
	public boolean onDrag(View v, DragEvent event) {
		final String DROPTAG = "DropTarget";

		int action = event.getAction();
		boolean result = true;
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
			View view = (View) event.getLocalState();
			ViewGroup owner = (ViewGroup) view.getParent();
			owner.removeView(view);

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.leftMargin = (int) event.getX() - 25;
			params.topMargin = (int) event.getY() - 25;


			((ViewGroup) v).addView(view, params);
			Log.v(DROPTAG, "Droped Operation on target");
			break;
		case DragEvent.ACTION_DRAG_ENDED:
			break;
		default:
			result = false;
		}
		return result;
	}
}
