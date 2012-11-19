package com.github.scratch_android;

import java.util.ArrayList;

import com.github.scratch_android.MainActivity.ActivityToDragAreaListener;

import android.app.Fragment;
import android.content.ClipData;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.widget.RelativeLayout;

public class DragArea extends Fragment implements ActivityToDragAreaListener{
	protected DragAreaManager manager;
	@Override
	public void onCodeBlockDropped(CodeBlock cb) {
		manager.code_block_dropped(cb);
	}
}

class DragAreaManager implements View.OnDragListener, View.OnLongClickListener {
	private View v;
	private ArrayList<CodeBlock> listeners;
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
		CodeBlock child = new CodeBlock(this.v.getContext(), image_name, top_margin);
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
		v.startDrag(data, dsb, (Object) v, 1);
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

class MotionDragArea extends DragArea {
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.motion_blocks, container, false);
		manager = new DragAreaManager(v);
		return v;
	}
}

class LooksDragArea extends DragArea {
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.looks_blocks, container, false);
		manager = new DragAreaManager(v);
		return v;
	}
}

class SoundDragArea extends DragArea {
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.sound_blocks, container, false);
		manager = new DragAreaManager(v);
		return v;
	}
}

class PenDragArea extends DragArea {
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.pen_blocks, container, false);
		manager = new DragAreaManager(v);
		return v;
	}
}

class ControlDragArea extends DragArea {
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.control_blocks, container, false);
		manager = new DragAreaManager(v);
		return v;
	}
}

class SensingDragArea extends DragArea {
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.sensing_blocks, container, false);
		manager = new DragAreaManager(v);
		return v;
	}
}

class OperatorsDragArea extends DragArea {
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.operators_blocks, container, false);
		manager = new DragAreaManager(v);
		return v;
	}
}

class VariablesDragArea extends DragArea {
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.variables_blocks, container, false);
		manager = new DragAreaManager(v);
		return v;
	}
}