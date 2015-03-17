package me.hkgumbs.just_write;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;


public class MainActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    ActionBar ab;
    FloatingActionButton fab;
    MyFragmentAdapter pagerAdapter;
    ViewPager pager;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        pager = (ViewPager) findViewById(R.id.pager);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        ab = getSupportActionBar();
        ab.hide();
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(R.layout.actionbar_main);

        sp = getPreferences(Context.MODE_PRIVATE);
        int size = sp.getInt(C.PAGES, 1);
        pagerAdapter = new MyFragmentAdapter(getSupportFragmentManager(), size);
        pager.setAdapter(pagerAdapter);
        pager.setOnPageChangeListener(this);

        fab.setOnClickListener(this);
        onPageSelected(0);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        boolean dark = sp.getBoolean(C.DARK_THEME, false);
        if (dark) {
            fab.setColorNormalResId(android.R.color.black);
            fab.setColorPressedResId(R.color.black_pressed);
        } else {
            fab.setColorNormalResId(android.R.color.white);
            fab.setColorPressedResId(R.color.white_pressed);
        }
    }

    @Override
    public void onClick(View v) {
        if (ab.isShowing())
            ab.hide();
        else
            ab.show();
    }

}
