package com.github.scratch_android;

import java.util.ArrayList;

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
	private DropAreaManager manager;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.code_layout, container, false);
		manager = new DropAreaManager(v);
		return v;
	}

	public DropAreaManager get_manager() {
		return this.manager;
	}
}

class DropAreaManager implements View.OnDragListener, View.OnLongClickListener {
	private View v;
	private ArrayList<CodeBlock> listeners;
	private MainActivityManager parent_manager;

	public DropAreaManager(View view) {
		this.v = view;
		this.listeners = new ArrayList<CodeBlock>();
		construct_listeners();
	}

	public void set_parent_manager(MainActivityManager pmanager) {
		this.parent_manager = pmanager;
	}
	
	private void code_block_dropped(CodeBlock cb) {
		parent_manager.code_block_dropped(cb);
	}

	private void construct_listeners() {
		ViewGroup container = (ViewGroup) this.v.findViewById(R.id.drop_area_container);
		int child_count =  container.getChildCount();
		for (int i = 0; i < child_count; i++) {
			CodeBlock cb = (CodeBlock) container.getChildAt(i);
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
		boolean mDragInProgress = v.startDrag(data, dsb, (Object) v, 1);
		Log.v((String) v.getTag(), "Started dragging:  " + mDragInProgress);
		return true;
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
			CodeBlock cb = (CodeBlock) event.getLocalState();
			ViewGroup owner = (ViewGroup) cb.getParent();

			if (owner != v && !listeners.contains(cb)) {
				code_block_dropped(cb);
				cb.setOnLongClickListener(this);
				cb.setOnDragListener(this);
				listeners.add(cb);
				
			}
			else if (owner == v) {
				owner.removeView(cb);
			}
			
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.leftMargin = (int) event.getX() - cb.getWidth()/2;
			params.topMargin = (int) event.getY() - cb.getHeight()/2;
			((ViewGroup) v).addView(cb, params);
			
			Log.v(DROPTAG, "Droped Operation on target");
			System.gc();
			break;
		case DragEvent.ACTION_DRAG_ENDED:
			break;
		default:
			result = false;
		}
		return result;
	}
}
