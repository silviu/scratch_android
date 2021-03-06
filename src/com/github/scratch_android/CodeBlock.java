package com.github.scratch_android;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("ViewConstructor")
public class CodeBlock extends ImageView {
	private int margin_top;
	private String image_name;
	
	public CodeBlock(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}
	
	public CodeBlock(Context context) {
		super(context);
	}
	
	public CodeBlock(Context context, String image, int margin_top) {
		super(context);
		this.setImageResource(getResources().getIdentifier(image, "drawable", "com.github.scratch_android"));
		this.image_name = image;
		this.margin_top = margin_top;
	}
	
	private void init(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CodeBlock);
		CharSequence s = a.getString(R.styleable.CodeBlock_image_name);
        this.image_name = s.toString();
        this.margin_top = a.getDimensionPixelOffset(R.styleable.CodeBlock_margin_top, -1);
        a.recycle();
	}
	
	public String get_image_name() {
		return this.image_name;
	}
	
	public int get_margin_top() {
		return this.margin_top;
	}
}

class BlankCodeBlock extends CodeBlock {
	public final static int TOP = 0;
	public final static int BOTTOM = 1;
	public final static int HEIGHT = 30;
	private int type;
	
	public BlankCodeBlock(Context context, int ptype) {
		super(context);
		this.type = ptype;
		setBackgroundColor(Color.RED);
		this.setScaleType(ScaleType.FIT_XY);
	}
	
	public int get_type() {
		return type;
	}
}

class SnapCodeBlock extends CodeBlock {

	public SnapCodeBlock(Context context) {
		super(context);
		this.setImageResource(getResources().getIdentifier("white_snap", "drawable", "com.github.scratch_android"));
		this.setScaleType(ScaleType.FIT_XY);
	}
}
