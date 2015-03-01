package me.hkgumbs.just_write;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;


public class MainActivity extends ActionBarActivity {

    MyFragmentAdapter pagerAdapter;
    MyFloatingActionsMenu fam;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        final int size = getPreferences(Context.MODE_PRIVATE).getInt(C.PAGES, 1);
        pagerAdapter = new MyFragmentAdapter(getSupportFragmentManager(), size);
        pager = (ViewPager) findViewById(R.id.pager);
        fam = new MyFloatingActionsMenu(this);

        pager.setAdapter(pagerAdapter);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                fam.update(position);
            }
        });
        fam.update(0);
    }

}
