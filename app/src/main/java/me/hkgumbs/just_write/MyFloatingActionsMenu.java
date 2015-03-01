package me.hkgumbs.just_write;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class MyFloatingActionsMenu {
    final public static int CUSTOMIZE = 0;
    final public static int NEW_PAGE = 1;
    final public static int CAPTURE = 2;
    final public static int NUM_ACTIONS = 3;

    private Activity activity;
    private FloatingActionsMenu whiteMenu;
    private FloatingActionsMenu blackMenu;

    public MyFloatingActionsMenu(Activity activity) {
        this.activity = activity;
        whiteMenu = ((FloatingActionsMenu) activity.findViewById(R.id.white_menu));
        blackMenu = ((FloatingActionsMenu) activity.findViewById(R.id.black_menu));
        attach(whiteMenu, false);
        attach(blackMenu, true);
    }

    public void attach(FloatingActionsMenu menu, boolean dark) {
        int color = dark ? android.R.color.black : android.R.color.white;
        int pressed = dark ? R.color.black_pressed : R.color.white_pressed;

        for (int i = 0; i < NUM_ACTIONS; i++) {
            FloatingActionButton button = new FloatingActionButton(activity);
            button.setColorNormalResId(color);
            button.setColorPressedResId(pressed);
            button.setSize(FloatingActionButton.SIZE_MINI);

            // TODO set icons and listeners
            switch (i) {
                case CUSTOMIZE:
                    button.setTitle(C.CUSTOMIZE);
                    break;
                case NEW_PAGE:
                    button.setTitle(C.NEW_PAGE);
                    break;
                case CAPTURE:
                    button.setTitle(C.CAPTURE);
            }

            menu.addButton(button);
        }

    }

    public void update(int page) {
        blackMenu.collapse();
        whiteMenu.collapse();

        SharedPreferences sp = activity.getPreferences(Context.MODE_PRIVATE);
        boolean dark = sp.getBoolean(C.DARK_THEME + C.spKey(page), false);

        if (dark) {
            if (blackMenu.getVisibility() == View.VISIBLE)
                return;
            Runnable hideWhite = visibility(whiteMenu, View.GONE);
            Runnable showBlack = visibility(blackMenu, View.VISIBLE);
            whiteMenu.animate().rotationBy(1f).alpha(0f).withEndAction(hideWhite).start();
            blackMenu.animate().rotationBy(1f).alpha(1f).withEndAction(showBlack).start();
        } else {
            if (whiteMenu.getVisibility() == View.VISIBLE)
                return;
            Runnable hideBlack = visibility(blackMenu, View.GONE);
            Runnable showWhite = visibility(whiteMenu, View.VISIBLE);
            blackMenu.animate().alpha(0f).withEndAction(hideBlack).start();
            whiteMenu.animate().alpha(1f).withEndAction(showWhite).start();
        }
    }

    private static Runnable visibility(final View v, final int visibility) {
        return new Runnable() {
            @Override
            public void run() {
                v.setVisibility(visibility);
            }
        };
    }

}
