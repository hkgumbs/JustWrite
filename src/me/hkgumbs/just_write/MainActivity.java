package me.hkgumbs.just_write;

import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.squareup.seismic.ShakeDetector;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.Bundle;

public class MainActivity extends FragmentActivity implements
	ShakeDetector.Listener {

    private SharedPreferences sp;
    private ShakeDetector sd;

    MyMenu menu;
    ActionHandler handler;
    MyFragmentAdapter pagerAdapter;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.activity_home);

	sp = getPreferences(Context.MODE_PRIVATE);
	pagerAdapter = new MyFragmentAdapter(getSupportFragmentManager());
	pager = (ViewPager) findViewById(R.id.pager);
	pager.setAdapter(pagerAdapter);

	handler = new ActionHandler(this, pager);
	menu = new MyMenu(this, handler);
    }

    @Override
    public void onResume() {
	super.onResume();

	// INIT SHAKEDETECTOR
	sd = new ShakeDetector(this);
	sd.start((SensorManager) getSystemService(SENSOR_SERVICE));
    }

    @Override
    public void onPause() {
	super.onPause();
	sd.stop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_MENU) { // HARDWARE MENU BUTTON
	    hearShake();
	    return true;
	} else
	    return super.onKeyDown(keyCode, event);
    }

    public void hearShake() {
	// shake detector implemented in square-seismic-1.0.0.jar
	int position = pager.getCurrentItem();
	View root = pager.getChildAt(position);
	if (menu.open(position, root)) {
	    // hide keyboard
	    InputMethodManager imm = (InputMethodManager) this
		    .getSystemService(Context.INPUT_METHOD_SERVICE);
	    imm.hideSoftInputFromWindow(pager.getWindowToken(), 0);
	}
    }

    class MyFragmentAdapter extends FragmentStatePagerAdapter {

	public MyFragmentAdapter(FragmentManager fm) {
	    super(fm);
	}

	@Override
	public ContentFragment getItem(int position) {
	    ContentFragment fragment = new ContentFragment();
	    Bundle arg = new Bundle();
	    String value;
	    if (position == 0)
		// first element gets empty argument for legacy reasons
		value = "";
	    else
		value = Integer.toString(position);
	    arg.putString("position", value);
	    fragment.setArguments(arg);
	    return fragment;
	}

	@Override
	public int getCount() {
	    return sp.getInt("pages", 1);
	}

    }

}