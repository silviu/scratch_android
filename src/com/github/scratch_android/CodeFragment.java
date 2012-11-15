package com.github.scratch_android;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class CodeFragment extends Fragment {
	private View dropTarget;
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.code_layout, container, false);
		dropTarget = (View) v.findViewById(R.id.relative_layout);
		dropTarget.setOnDragListener(new View.OnDragListener() {
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
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(60, 60);
					params.leftMargin = (int) event.getX();;
					params.topMargin = (int) event.getY();
					Operation op = new Operation(v.getContext());
					((ViewGroup) v).addView(op, params);

					Log.v(DROPTAG, "drag drop in dropTarget");
					break;
				case DragEvent.ACTION_DRAG_ENDED:
					break;
				default:
					Log.v(DROPTAG, "other action in dropzone: " + action);
					result = false;
				}
				return result;
			}
		});

		return v;
	}
}
