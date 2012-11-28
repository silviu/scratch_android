package com.github.scratch_android;

import java.util.ArrayList;

import com.github.scratch_android.DropArea.OnCodeBlockDroppedListener;

import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.widget.RelativeLayout;

public class DropArea extends Fragment {
	private OnCodeBlockDroppedListener cb_listener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		cb_listener = (OnCodeBlockDroppedListener) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.code_layout, container, false);
		new DropAreaManager(v, cb_listener);
		return v;
	}

	public interface OnCodeBlockDroppedListener {
		public void onCodeBlockDropped(CodeBlock cb);
	}

}



class DropAreaManager implements View.OnDragListener, View.OnLongClickListener {
	private View v;
	private ArrayList<CodeBlockGroup> listeners;
	private OnCodeBlockDroppedListener cb_listener;
	

	public DropAreaManager(View view, OnCodeBlockDroppedListener cb_listener) {
		this.v = view;
		this.listeners = new ArrayList<CodeBlockGroup>();
		this.cb_listener = cb_listener;
		construct_listeners();
	}

	private void construct_listeners() {
		ViewGroup container = (ViewGroup) this.v.findViewById(R.id.drop_area_container);
		int child_count =  container.getChildCount();
		for (int i = 0; i < child_count; i++) {
			CodeBlockGroup cb = (CodeBlockGroup) container.getChildAt(i);
			cb.setOnLongClickListener(this);
			cb.setOnDragListener(this);
			listeners.add(cb);
		}
		v.setOnDragListener(this);
	}

	@Override
	public boolean onLongClick(View v) {
		ClipData data = ClipData.newPlainText("DragData", (String) v.getTag());
		DragShadowBuilder dsb = new View.DragShadowBuilder(v);
		v.startDrag(data, dsb, (Object) v, 1);
		return true;
	}



	@Override
	public boolean onDrag(View area, DragEvent event) {
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
			Object cb = event.getLocalState();
			if (cb instanceof CodeBlock ) {
				CodeBlock cb1 = (CodeBlock)cb;
				ViewGroup owner = (ViewGroup) cb1.getParent();
				// if owner is one of the codeblockgroups
				if (listeners.contains(owner)) {
					Log.v("OWNER IS", "CODE_BLOCK_GROUP");
					CodeBlockGroup cbg = (CodeBlockGroup) owner;
					cbg.removeCodeBlock(cb1);
					// if code_block_group is empty after remove
					// delete the code_block_group
					if (cbg.getChildCount() == 2) {
						listeners.remove(cbg);
						((ViewGroup) v).removeView(cbg);
					}
					//owner.removeView(cb1);
				// else parent is Drag Area
				} else if (owner != v) {
					Log.v("OWNER IS", "DRAG AREA");
					cb_listener.onCodeBlockDropped(cb1);
				}

				if (area == v) {
					Log.v("DROPPED", " in V");
					CodeBlockGroup group = new CodeBlockGroup(v.getContext(), cb1, cb_listener);
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
					params.leftMargin = (int) event.getX() - cb1.getWidth()/2;
					params.topMargin = (int) event.getY() - cb1.getHeight()/2 - BlankCodeBlock.HEIGHT;
					((ViewGroup) v).addView(group, params);
					listeners.add(group);
				}

				Log.v("DROP_AREA CHILD_COUNT", String.valueOf(((ViewGroup) v).getChildCount()));
				System.gc();
			}
			break;
		case DragEvent.ACTION_DRAG_ENDED:
			break;
		default:
			result = false;
		}
		return result;
	}
}
