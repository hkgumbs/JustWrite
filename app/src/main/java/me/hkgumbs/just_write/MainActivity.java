package me.hkgumbs.just_write;

import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

public class MainActivity extends ActionBarActivity {

    MyFragmentAdapter pagerAdapter;
    ViewPager pager;
    FloatingActionButton button;
    SoftKeyboard softKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        // initialize fields
        int size = getPreferences(Context.MODE_PRIVATE).getInt(C.PAGES, 1);
        pagerAdapter = new MyFragmentAdapter(getSupportFragmentManager(), size);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);

        // setup SoftKeyboard
        InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        RelativeLayout root = ((RelativeLayout) findViewById(R.id.root));
        button = ((FloatingActionButton) findViewById(R.id.button));
        softKeyboard = new SoftKeyboard(root, im);
        softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {

            @Override
            public void onSoftKeyboardHide() {
                button.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "goodbye world", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSoftKeyboardShow() {
                button.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "hello world", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        softKeyboard.unRegisterSoftKeyboardCallback();
    }

}
