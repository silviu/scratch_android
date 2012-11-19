package com.github.scratch_android;

import com.github.scratch_android.DropArea.OnCodeBlockDroppedListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.Menu;

public class MainActivity extends Activity implements OnCodeBlockDroppedListener {
	FragmentManager fm;
	DragArea drag;
	
	private ActivityToDragAreaListener activity_to_drag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		drag = new MotionDragArea();
		activity_to_drag = (ActivityToDragAreaListener) drag;
		
		fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.drag_area_fragment, drag, "dynamic_drag_fragment");
		ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onCodeBlockDropped(CodeBlock cb) {
		activity_to_drag.onCodeBlockDropped(cb);
	}
	
	public interface ActivityToDragAreaListener {
		public void onCodeBlockDropped(CodeBlock cb);
	}
}
