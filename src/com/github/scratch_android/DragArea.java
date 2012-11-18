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

public class DragArea extends Fragment{
	private DragAreaManager manager;
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.motion_blocks, container, false);
		manager = new DragAreaManager(v);
		return v;
	}
	
	public DragAreaManager get_manager() {
		return this.manager;
	}
	
	
}

class DragAreaManager implements View.OnDragListener, View.OnLongClickListener {
	private View v;
	private ArrayList<CodeBlock> listeners;
	private MainActivityManager parent_manager;
	ViewGroup container;
	
	public DragAreaManager(View v) {
		this.v = v;
		this.listeners = new ArrayList<CodeBlock>();
		this.container = (ViewGroup) this.v.findViewById(R.id.myrellayout);
		construct_listeners();
	}
	
	private void add_new_code_block(CodeBlock cb) {
		String image_name  = ((CodeBlock) cb).get_image_name();
		int top_margin = ((CodeBlock) cb).get_margin_top();
		MotionCodeBlock child = new MotionCodeBlock(this.v.getContext(), image_name, top_margin);
		RelativeLayout.LayoutParams paramsw = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsw.leftMargin = 0;
		paramsw.topMargin = ((CodeBlock) cb).get_margin_top();
		container.addView(child, paramsw);
		child.setOnLongClickListener(this);
		child.setOnDragListener(this);
		listeners.add(child);
	}
	
	public void code_block_dropped(CodeBlock cb) {
		this.listeners.remove(cb);
		container.removeView(cb);
		add_new_code_block(cb);
	}
	
	public void set_parent_manager(MainActivityManager pmanager) {
		this.parent_manager = pmanager;
	}
	
	public void construct_listeners() {
		int child_count =  container.getChildCount();
		for (int i = 0; i < child_count; i++) {
			CodeBlock cb = (CodeBlock) container.getChildAt(i);
			cb.setOnLongClickListener(this);
			cb.setOnDragListener(this);
			listeners.add(cb);
		}
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
		if (event.getLocalState() != this) {
			return false;
		}
		boolean result = true;
		int action = event.getAction();
		switch (action) {
		case DragEvent.ACTION_DRAG_STARTED:
			break;
		case DragEvent.ACTION_DRAG_LOCATION:
			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			break;
		case DragEvent.ACTION_DROP:
			result = false;
			break;
		case DragEvent.ACTION_DRAG_ENDED:
			break;
		default:
			result = false;
			break;
		}
		return result;
	}
	
}