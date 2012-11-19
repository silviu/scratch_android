package com.github.scratch_android;

import java.util.ArrayList;

import com.github.scratch_android.ButtonArea.OnToggleListener;
import com.github.scratch_android.DropArea.OnCodeBlockDroppedListener;
import com.github.scratch_android.MainActivity.ActivityToDragAreaListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity implements OnCodeBlockDroppedListener, OnToggleListener {

	FragmentManager fm;
	DragArea drag;
	ActivityManager manager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
				
		fm = getFragmentManager();
		manager = new ActivityManager(fm);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onCodeBlockDropped(CodeBlock cb) {
		manager.onCodeBlockDropped(cb);
	}
	
	public interface ActivityToDragAreaListener {
		public void onCodeBlockDropped(CodeBlock cb);
	}

	@Override
	public void onToggle(int fragment) {
		manager.toggle_fragment(fragment);
	}
}

class ActivityManager {
	private final DragArea[] drag_list =  { new MotionDragArea(),   new LooksDragArea(),
											 new SoundDragArea(),     new PenDragArea(),
											 new ControlDragArea(),   new SensingDragArea(),
											 new OperatorsDragArea(), new VariablesDragArea() };
	private ArrayList<ActivityToDragAreaListener> activity_to_drag_list;
	private FragmentManager fm;
	private int selected_drag_fragment;
	
	
	public ActivityManager(FragmentManager fm) {
		this.fm = fm;
		this.activity_to_drag_list = new ArrayList<MainActivity.ActivityToDragAreaListener>();
		selected_drag_fragment = 0;
		add_fragnment(selected_drag_fragment);
		add_listeners();
	}
	
	private void add_listeners() {
		for (int i = 0 ; i < drag_list.length; i++)
			activity_to_drag_list.add((ActivityToDragAreaListener) drag_list[i]);
	}
	
	private void add_fragnment(int fragment) {
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.drag_area_fragment, drag_list[fragment], "dynamic_drag_fragment");
		ft.commit();
	}
	
	public void toggle_fragment(int fragment) {
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.drag_area_fragment, drag_list[fragment], "dynamic_drag_fragment");
		ft.commit();
		selected_drag_fragment = fragment;
	}
	
	public void onCodeBlockDropped(CodeBlock cb) { 
		activity_to_drag_list.get(selected_drag_fragment).onCodeBlockDropped(cb);
	}
}
