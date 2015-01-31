package me.hkgumbs.just_write;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

    MyFragmentAdapter pagerAdapter;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hides actionbar on main activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home);

        // show actionbar when status bar is visible
        getWindow().getDecorView().getRootView().setOnSystemUiVisibilityChangeListener(
                new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            Toast.makeText(MainActivity.this, "visible", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "hidden", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | 0x4);

        // initialize fields
        int size = getPreferences(Context.MODE_PRIVATE).getInt("pages", 1);
        pagerAdapter = new MyFragmentAdapter(getSupportFragmentManager(), size);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);

        // TODO focus edittext
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