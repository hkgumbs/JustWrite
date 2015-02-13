package me.hkgumbs.just_write;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    MyFragmentAdapter pagerAdapter;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Hide status bar
        getSupportActionBar().hide();

        // initialize fields
        int size = getPreferences(Context.MODE_PRIVATE).getInt("pages", 1);
        pagerAdapter = new MyFragmentAdapter(getSupportFragmentManager(), size);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            // hardware menu button
            openMenu();
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

    private void openMenu() {
        int position = pager.getCurrentItem();
        View root = pager.getChildAt(position);
        // TODO start menu activity for result
    }

}