package com.github.scratch_android;

import java.util.ArrayList;

import com.github.scratch_android.DropArea.OnCodeBlockDroppedListener;

import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

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
	private ArrayList<CodeBlock> listeners;
	private OnCodeBlockDroppedListener cb_listener;

	public DropAreaManager(View view, OnCodeBlockDroppedListener cb_listener) {
		this.v = view;
		this.listeners = new ArrayList<CodeBlock>();
		this.cb_listener = cb_listener;
		construct_listeners();
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
		v.startDrag(data, dsb, (Object) v, 1);
		return true;
	}

	private boolean contains_snap() {
		int chid_no = ((ViewGroup) v).getChildCount();
		for (int i = 0; i < chid_no; i++)
			if (((ViewGroup) v).getChildAt(i) instanceof SnapCodeBlock)
				return true;
		return false;
	}

	private void remove_snap() {
		int chid_no = ((ViewGroup) v).getChildCount();
		for (int i = 0; i < chid_no; i++)
			if (((ViewGroup) v).getChildAt(i) instanceof SnapCodeBlock)
				((ViewGroup) v).removeViewAt(i);
	}

	private BlankCodeBlock get_new_blank(CodeBlock cb, int type) {
		BlankCodeBlock blank = new BlankCodeBlock(v.getContext(), type);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(cb.getWidth(), 30);
		RelativeLayout.LayoutParams cb_params = (RelativeLayout.LayoutParams) cb.getLayoutParams();
		if (type == BlankCodeBlock.TOP) {
			params.topMargin = cb_params.topMargin - cb.getHeight();
			params.leftMargin = cb_params.leftMargin;
		}
		else {
			params.topMargin = cb_params.topMargin + cb.getHeight();
			params.leftMargin = cb_params.leftMargin;
		}
		blank.setOnDragListener(this);
		blank.setLayoutParams(params);
		listeners.add(blank);

		return blank;
	}

	private BlankCodeBlock get_new_blank(BlankCodeBlock area, CodeBlock cb, int type) {
		RelativeLayout.LayoutParams tmp = (RelativeLayout.LayoutParams) area.getLayoutParams();
		BlankCodeBlock blank = new BlankCodeBlock(v.getContext(), type);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(cb.getWidth(), 30);
		if (type == BlankCodeBlock.BOTTOM) {
			params.topMargin = tmp.topMargin + cb.getHeight() - 6;
			params.leftMargin = tmp.leftMargin;
		}
		else {
			params.topMargin = tmp.topMargin - cb.getHeight() + 6;
			params.leftMargin = tmp.leftMargin;
		}
		blank.setOnDragListener(this);
		blank.setLayoutParams(params);
		listeners.add(blank);
		return blank;
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
			CodeBlock cb = (CodeBlock) event.getLocalState();
			ViewGroup owner = (ViewGroup) cb.getParent();


			if (owner != v) {
				cb_listener.onCodeBlockDropped(cb);
				cb.setOnLongClickListener(this);
				cb.setOnDragListener(this);
				listeners.add(cb);
			}

			if (owner == v) {
				owner.removeView(cb);
			}

			if (area == v) {
				Log.v("DROPPED", " in V");
				CodeBlockGroup group = new CodeBlockGroup(v.getContext(), cb, cb_listener);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				params.leftMargin = (int) event.getX() - cb.getWidth()/2;
				params.topMargin = (int) event.getY() - cb.getHeight()/2 - BlankCodeBlock.HEIGHT;
				((ViewGroup) v).addView(group, params);
			}

			Log.v("DROP_AREA CHILD_COUNT", String.valueOf(((ViewGroup) v).getChildCount()));
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
