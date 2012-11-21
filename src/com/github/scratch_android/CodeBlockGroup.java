package com.github.scratch_android;

import java.util.ArrayList;

import com.github.scratch_android.DropArea.OnCodeBlockDroppedListener;

import android.content.ClipData;
import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.widget.RelativeLayout;

public class CodeBlockGroup extends RelativeLayout {
	private BlankCodeBlock top_blank;
	private BlankCodeBlock bottom_blank;
	private RelativeLayout.LayoutParams params_blank_top;
	private RelativeLayout.LayoutParams params_blank_bottom;
	private RelativeLayout.LayoutParams params_code_block;
	private ArrayList<CodeBlock> code_structure;
	private ArrayList<Integer> total_height;

	public CodeBlockGroup(Context context, CodeBlock parent, OnCodeBlockDroppedListener pcb_listener) {
		super(context);
		top_blank = new BlankCodeBlock(this.getContext(), BlankCodeBlock.TOP);
		bottom_blank = new BlankCodeBlock(this.getContext(), BlankCodeBlock.BOTTOM);
		params_blank_top = new RelativeLayout.LayoutParams(parent.getWidth(), 30);
		params_blank_bottom = new RelativeLayout.LayoutParams(parent.getWidth(), 30);
		params_code_block = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		code_structure = new ArrayList<CodeBlock>();
		total_height = new ArrayList<Integer>();
		init(parent);
		CodeBlockGroupManager manager = new CodeBlockGroupManager(this, pcb_listener, top_blank, bottom_blank, params_blank_top, params_blank_bottom);
	}

	private void init(CodeBlock parent) {
		params_blank_top.topMargin = 0;
		params_blank_top.leftMargin = 0;
		this.addView(top_blank, params_blank_top);
		code_structure.add(top_blank);
		total_height.add(BlankCodeBlock.HEIGHT);

		params_code_block.topMargin = params_blank_top.topMargin + BlankCodeBlock.HEIGHT;
		params_code_block.leftMargin = 0;
		this.addView(parent, params_code_block);
		code_structure.add(parent);
		total_height.add(params_code_block.topMargin + parent.getHeight());

		params_blank_bottom.topMargin = params_code_block.topMargin + parent.getHeight();
		params_blank_bottom.leftMargin = 0;
		this.addView(bottom_blank, params_blank_bottom);
		code_structure.add(bottom_blank);
		total_height.add(params_blank_bottom.topMargin + BlankCodeBlock.HEIGHT);
	}

	public void insertAt(CodeBlock new_code, CodeBlock old_code) {
		int index = code_structure.indexOf(old_code);
		CodeBlock old = code_structure.get(index);
		RelativeLayout.LayoutParams save_position;
		if (old instanceof BlankCodeBlock) {
			if (((BlankCodeBlock) old).get_type() == BlankCodeBlock.BOTTOM) {
				save_position = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams tmp = (RelativeLayout.LayoutParams) old.getLayoutParams();
				save_position.topMargin = tmp.topMargin - 6;
				save_position.leftMargin = tmp.leftMargin;
				for (CodeBlock cdb : code_structure.subList(index, code_structure.size())) {
					((RelativeLayout.LayoutParams) cdb.getLayoutParams()).topMargin += new_code.getHeight() - 6;
				}
			}
			else {
				if (index == 0)
					index++;
				save_position = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				((RelativeLayout.LayoutParams)this.getLayoutParams()).topMargin -= new_code.getHeight();				
				
				RelativeLayout.LayoutParams tmp = (RelativeLayout.LayoutParams) code_structure.get(1).getLayoutParams();
				save_position.topMargin = tmp.topMargin + 6;
				save_position.leftMargin = tmp.leftMargin;
				((RelativeLayout.LayoutParams)top_blank.getLayoutParams()).topMargin = save_position.topMargin - new_code.getHeight() - 6;
				for (CodeBlock cdb : code_structure.subList(index, code_structure.size())) {
					((RelativeLayout.LayoutParams) cdb.getLayoutParams()).topMargin += new_code.getHeight();
				}
			}
		}
		else {
			save_position = (RelativeLayout.LayoutParams) old.getLayoutParams();
		}
		code_structure.add(index, new_code);
		this.addView(new_code, save_position);
	}
}

class CodeBlockGroupManager implements View.OnDragListener, View.OnLongClickListener {
	private CodeBlockGroup rel;
	private ArrayList<CodeBlock> listeners;
	private OnCodeBlockDroppedListener cb_listener;
	private BlankCodeBlock top_blank;
	private BlankCodeBlock bottom_blank;
	private RelativeLayout.LayoutParams params_top_blank;
	private RelativeLayout.LayoutParams params_bottom_blank;


	public CodeBlockGroupManager(CodeBlockGroup prel, OnCodeBlockDroppedListener pcb_listener, 
			BlankCodeBlock ptop_blank, BlankCodeBlock pbottom_blank,
			RelativeLayout.LayoutParams pparams_top_blank, RelativeLayout.LayoutParams pparams_bottom_blank) {
		this.rel = prel;
		this.top_blank = ptop_blank;
		this.bottom_blank = pbottom_blank;
		this.params_top_blank = pparams_top_blank;
		this.params_bottom_blank = pparams_bottom_blank;
		this.listeners = new ArrayList<CodeBlock>();
		this.cb_listener = pcb_listener;
		construct_listeners();
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

	private boolean contains_snap() {
		int chid_no = rel.getChildCount();
		for (int i = 0; i < chid_no; i++)
			if (rel.getChildAt(i) instanceof SnapCodeBlock)
				return true;
		return false;
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

		switch (action) {
		case DragEvent.ACTION_DRAG_STARTED:
			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			if (area == rel) {
				Log.v("GROOOOOOOOOUUUUP", "YEEES");
			}
			if (area instanceof BlankCodeBlock && !contains_snap()) {
				RelativeLayout.LayoutParams area_params =  (RelativeLayout.LayoutParams) area.getLayoutParams();
				snap = new SnapCodeBlock(rel.getContext());
				RelativeLayout.LayoutParams snap_params = new RelativeLayout.LayoutParams(area.getWidth(), 5);
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
				rel.addView(snap, snap_params);
			}
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			if (area instanceof BlankCodeBlock) {
				Log.v("EXITED", "BLANK SNAP AREA");
				remove_snap();
			}
			break;
		case DragEvent.ACTION_DRAG_LOCATION:
			break;
		case DragEvent.ACTION_DROP:
			CodeBlock cb = (CodeBlock) event.getLocalState();
			ViewGroup owner = (ViewGroup) cb.getParent();


			if (owner != rel) {
				cb_listener.onCodeBlockDropped(cb);
				cb.setOnLongClickListener(this);
				cb.setOnDragListener(this);
				listeners.add(cb);
			}

			if (owner == rel) {
				owner.removeView(cb);
			}

			rel.insertAt(cb, (CodeBlock) area);
			remove_snap();


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
