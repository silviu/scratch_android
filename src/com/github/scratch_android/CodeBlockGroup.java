package com.github.scratch_android;

import java.util.ArrayList;

import com.github.scratch_android.DropArea.OnCodeBlockDroppedListener;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.widget.RelativeLayout;

@SuppressLint("ViewConstructor")
public class CodeBlockGroup extends RelativeLayout {
	private BlankCodeBlock top_blank;
	private BlankCodeBlock bottom_blank;
	private RelativeLayout.LayoutParams params_blank_top;
	private RelativeLayout.LayoutParams params_blank_bottom;
	private RelativeLayout.LayoutParams params_code_block;
	private ArrayList<CodeBlock> code_structure;
	private CodeBlockGroupManager manager;

	public CodeBlockGroup(Context context, CodeBlock parent, OnCodeBlockDroppedListener pcb_listener) {
		super(context);
		top_blank = new BlankCodeBlock(this.getContext(), BlankCodeBlock.TOP);
		bottom_blank = new BlankCodeBlock(this.getContext(), BlankCodeBlock.BOTTOM);
		params_blank_top = new RelativeLayout.LayoutParams(parent.getWidth(), 30);
		params_blank_bottom = new RelativeLayout.LayoutParams(parent.getWidth(), 30);
		params_code_block = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		code_structure = new ArrayList<CodeBlock>();
		init(parent);
		manager = new CodeBlockGroupManager(this, pcb_listener);		
	}

	private void init(CodeBlock parent) {
		params_blank_top.topMargin = 0;
		params_blank_top.leftMargin = 0;
		this.addView(top_blank, params_blank_top);
		code_structure.add(top_blank);

		params_code_block.topMargin = params_blank_top.topMargin + BlankCodeBlock.HEIGHT;
		params_code_block.leftMargin = 0;
		this.addView(parent, params_code_block);
		code_structure.add(parent);

		params_blank_bottom.topMargin = params_code_block.topMargin + parent.getHeight();
		params_blank_bottom.leftMargin = 0;
		this.addView(bottom_blank, params_blank_bottom);
		code_structure.add(bottom_blank);
	}

	public void insertCodeBlock(CodeBlock new_code, CodeBlock old_code) {
		int index = code_structure.indexOf(old_code);
		
		RelativeLayout.LayoutParams save_position = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams tmp = (RelativeLayout.LayoutParams) old_code.getLayoutParams();
		
		if (old_code instanceof BlankCodeBlock) {
			if (((BlankCodeBlock) old_code).get_type() == BlankCodeBlock.BOTTOM) {
				save_position.topMargin = tmp.topMargin - 6;
				save_position.leftMargin = tmp.leftMargin;
				for (int i = index; i < code_structure.size(); i++) 
					((RelativeLayout.LayoutParams) code_structure.get(i).getLayoutParams()).topMargin += new_code.getHeight() - 6;
				bottom_blank.getLayoutParams().width = new_code.getWidth();
			}
			else {
				if (index == 0)
					index++;
				((RelativeLayout.LayoutParams)this.getLayoutParams()).topMargin -= new_code.getHeight();
				tmp = (RelativeLayout.LayoutParams) code_structure.get(1).getLayoutParams();
				save_position.topMargin = tmp.topMargin + 6;
				save_position.leftMargin = tmp.leftMargin;
				((RelativeLayout.LayoutParams)top_blank.getLayoutParams()).topMargin = save_position.topMargin - new_code.getHeight() -2;
				for (int i = index; i < code_structure.size(); i++)
					((RelativeLayout.LayoutParams) code_structure.get(i).getLayoutParams()).topMargin += new_code.getHeight();
				
				top_blank.getLayoutParams().width = new_code.getWidth();
			}
		}
		else {
			save_position.topMargin = tmp.topMargin;
			save_position.leftMargin = tmp.leftMargin;
			for (int i = index; i < code_structure.size(); i++)
				((RelativeLayout.LayoutParams) code_structure.get(i).getLayoutParams()).topMargin += new_code.getHeight()-6;
			
		}
		code_structure.add(index, new_code);
		this.addView(new_code, save_position);
	}

	public void removeCodeBlock(CodeBlock cb) {
		int index = code_structure.indexOf(cb);
		for (int i = index+1; i < code_structure.size(); i++) {
			((RelativeLayout.LayoutParams) code_structure.get(i).getLayoutParams()).topMargin -= cb.getHeight()-6;
		}
		code_structure.remove(cb);
		manager.remove_listener(cb);
		this.removeView(cb);

		if (this.getChildCount() == 2) {
			RelativeLayout rl = (RelativeLayout) this.getParent();
			rl.removeView(this);
		}
	}
}


class CodeBlockGroupManager implements View.OnDragListener, View.OnLongClickListener, OnCodeBlockDroppedListener {
	private CodeBlockGroup rel;
	private ArrayList<CodeBlock> listeners;
	private OnCodeBlockDroppedListener cb_listener;

	public CodeBlockGroupManager(CodeBlockGroup prel, OnCodeBlockDroppedListener pcb_listener) {
		this.rel = prel;
		this.listeners = new ArrayList<CodeBlock>();
		this.cb_listener = pcb_listener;
		construct_listeners();
	}

	public void remove_listener(CodeBlock cb) {
		listeners.remove(cb);
	}

	private void construct_listeners() {
		int child_count =  rel.getChildCount();
		for (int i = 0; i < child_count; i++) {
			CodeBlock cb = (CodeBlock) rel.getChildAt(i);
			cb.setOnLongClickListener(this);
			cb.setOnDragListener(this);
			listeners.add(cb);
		}
		rel.setOnDragListener(this);
	}

	@Override
	public boolean onLongClick(View v) {
		ClipData data = ClipData.newPlainText("DRAGGING IN", "CODE_BLOCK_GROUP");
		DragShadowBuilder dsb = new View.DragShadowBuilder(v);
		v.startDrag(data, dsb, (Object) v, 1);
		return true;
	}

	private void remove_snap() {
		int chid_no = rel.getChildCount();
		for (int i = 0; i < chid_no; i++)
			if (rel.getChildAt(i) instanceof SnapCodeBlock)
				rel.removeViewAt(i);
	}

	@Override
	public boolean onDrag(View area, DragEvent event) {
		int action = event.getAction();
		boolean result = true;
		SnapCodeBlock snap = null;
		CodeBlock cb;
		ViewGroup owner;

		switch (action) {
		case DragEvent.ACTION_DRAG_STARTED:
			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			remove_snap();
			RelativeLayout.LayoutParams area_params =  (RelativeLayout.LayoutParams) area.getLayoutParams();
			snap = new SnapCodeBlock(rel.getContext());
			RelativeLayout.LayoutParams snap_params = new RelativeLayout.LayoutParams(area.getWidth(), 5);
			if (area instanceof BlankCodeBlock) {
				if (((BlankCodeBlock) area).get_type() == BlankCodeBlock.BOTTOM) {
					Log.v("ENTERED BLANK", "BOTTOM");
					snap_params.topMargin = area_params.topMargin;
					snap_params.leftMargin = area_params.leftMargin;
				}
				else {
					Log.v("ENTERED BLANK", "TOP");
					snap_params.topMargin = area_params.topMargin + area.getHeight() - 6;
					snap_params.leftMargin = area_params.leftMargin;
				}
			}
			else if (area instanceof CodeBlock) {
				snap_params.topMargin = area_params.topMargin;
				snap_params.leftMargin = area_params.leftMargin;
			}
			rel.addView(snap, snap_params);
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			Log.v("EXITED", "BLANK SNAP AREA");
			remove_snap();
			break;
		case DragEvent.ACTION_DRAG_LOCATION:
			break;
		case DragEvent.ACTION_DROP:
			if (area instanceof BlankCodeBlock || area instanceof CodeBlock) {
				Log.v("DROPPED IN REL", "REL");
				cb = (CodeBlock) event.getLocalState();
				owner = (ViewGroup) cb.getParent();

				if (owner instanceof CodeBlockGroup) {
					((CodeBlockGroup) owner).removeCodeBlock(cb);

				} else if (owner == rel) {
					owner.removeView(cb);
				} else {				
					//owner is DragArea{
					cb_listener.onCodeBlockDropped(cb);
				}

				rel.insertCodeBlock(cb, (CodeBlock) area);
				cb.setOnLongClickListener(this);
				cb.setOnDragListener(this);
				listeners.add(cb);
				remove_snap();
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

	@Override
	public void onCodeBlockDropped(CodeBlock cb) {
		// remove codeblock from this viewgroup
		Log.v("REMOVING CODE_BLOCK", "FROM CODE_BLOCK_GROUP");
		rel.removeCodeBlock(cb);
	}

}
