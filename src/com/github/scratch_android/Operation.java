package com.github.scratch_android;
import android.content.ClipData;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;

public class Operation extends ImageView implements View.OnDragListener{

	public Operation(Context context) {
		super(context);
		this.setImageResource(R.drawable.ic_launcher);
		setOnLongClickListener(lcListener);
		setOnDragListener(this);
	}

	public Operation(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setImageResource(R.drawable.ic_launcher);
		setOnLongClickListener(lcListener);
		setOnDragListener(this);
	}

	public Operation(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setImageResource(R.drawable.ic_launcher);
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
