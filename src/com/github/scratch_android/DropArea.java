package com.github.scratch_android;

import java.util.ArrayList;

import com.github.scratch_android.DropArea.OnCodeBlockDroppedListener;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


class DropAreaManager implements View.OnDragListener {
	private View v;
	private ArrayList<CodeBlockGroup> listeners;
	private OnCodeBlockDroppedListener cb_listener;

	public DropAreaManager(View view, OnCodeBlockDroppedListener cb_listener) {
		this.v = view;
		this.listeners = new ArrayList<CodeBlockGroup>();
		this.cb_listener = cb_listener;
		v.setOnDragListener(this);
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

			if (owner instanceof CodeBlockGroup) {
				((CodeBlockGroup) owner).removeCodeBlock(cb);
				// if code_block_group is empty after remove
				// delete the code_block_group
				if (owner.getChildCount() == 2) {
					listeners.remove(owner);
					((ViewGroup) v).removeView(owner);
				}
				// else parent is Drag Area
			} else if (owner != v) {
				cb_listener.onCodeBlockDropped(cb);
			}

			CodeBlockGroup group = new CodeBlockGroup(v.getContext(), cb, cb_listener);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.leftMargin = (int) event.getX() - cb.getWidth()/2;
			params.topMargin = (int) event.getY() - cb.getHeight()/2 - BlankCodeBlock.HEIGHT;
			((ViewGroup) v).addView(group, params);
			listeners.add(group);
			
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
