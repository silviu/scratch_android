package com.github.scratch_android;
import android.content.ClipData;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;

public abstract class CodeBlock extends ImageView implements View.OnDragListener{

	public CodeBlock(Context context) {
		super(context);
		setOnLongClickListener(lcListener);
		this.setImageResource(R.drawable.motion_change_x_by);
		setOnDragListener(this);
	}

	public CodeBlock(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnLongClickListener(lcListener);
		setOnDragListener(this);
	}

	private static View.OnLongClickListener lcListener = new View.OnLongClickListener() {
		private boolean mDragInProgress;

		public boolean onLongClick(View v) {
			ClipData data = ClipData.newPlainText("DragData", (String) v.getTag());
			mDragInProgress = v.startDrag(data, new View.DragShadowBuilder(v), (Object) v, 0);
			Log.v((String) v.getTag(), "Started dragging:  " + mDragInProgress);
			return true;
		}
	};

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
