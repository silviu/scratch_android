package com.github.scratch_android;
import android.content.ClipData;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;

public abstract class CodeBlock extends ImageView implements View.OnDragListener , View.OnLongClickListener{
	private int margin_top;
	private String image_name;
	
	public CodeBlock(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnLongClickListener(this);
		setOnDragListener(this);
		init(context, attrs);
	}
	
	private void init(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CodeBlock);
		CharSequence s = a.getString(R.styleable.CodeBlock_image_name);
        if (s != null) {
            this.image_name = s.toString();
        }
        this.margin_top = a.getDimensionPixelOffset(R.styleable.CodeBlock_margin_top, -1);
        a.recycle();

		Log.v(String.valueOf(margin_top), this.image_name);
	}
	
	public String get_image_name() {
		return this.image_name;
	}
	
	public int get_margin_top() {
		return this.margin_top;
	}
	
	public CodeBlock(Context context, String image, int margin_top) {
		super(context);
		this.setImageResource(getResources().getIdentifier(image, "drawable", "com.github.scratch_android"));
		setOnLongClickListener(this);
		setOnDragListener(this);
		this.image_name = image;
		this.margin_top = margin_top;
	}

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
