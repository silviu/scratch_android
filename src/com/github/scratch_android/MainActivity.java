package com.github.scratch_android;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.view.Menu;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new MainActivityManager(getFragmentManager());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
class MainActivityManager {
	FragmentManager fm;
	DragAreaManager drag_area_manager;
	DropAreaManager drop_area_manager;
	FragmentSwitcherManager fragment_switcher_manager;
	
	public MainActivityManager(FragmentManager fmanager) {
		fm = fmanager;
		drag_area_manager = get_drag_manager();
		drop_area_manager = get_drop_manager();
		fragment_switcher_manager = get_switcher_manager();
		
		drag_area_manager.set_parent_manager(this);
		drop_area_manager.set_parent_manager(this);
	}
	
	public void code_block_dropped(CodeBlock cb) {
		drag_area_manager.code_block_dropped(cb);
	}
	
	private DragAreaManager get_drag_manager() {
		DragArea fg = (DragArea) fm.findFragmentById(R.id.drag_area_fragment);
		return fg.get_manager();
	}
	
	private DropAreaManager get_drop_manager() {
		DropArea fg = (DropArea) fm.findFragmentById(R.id.drop_area_fragment);
		return fg.get_manager();
	}
	
	private FragmentSwitcherManager get_switcher_manager() {
		FragmentSwitcher fg = (FragmentSwitcher) fm.findFragmentById(R.id.switcher_fragment);
		return fg.get_manager();
	}
}