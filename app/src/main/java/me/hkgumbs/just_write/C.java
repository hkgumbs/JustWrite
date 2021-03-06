package me.hkgumbs.just_write;

public abstract class C {

    public final static int MIN_FONT_SIZE = 12; // max = 100

    public final static String FONT_SLAB = "fonts/RobotoSlab-Thin.ttf";

    public final static String POSITION = "key";

    // SharedPreferences keys
    public final static String CONTENT = "CONTENT";
    public final static String FONT = "FONT";
    public final static String FONT_SIZE = "FONT_SIZE";
    public final static String DARK_THEME = "DARK_THEME";
    public final static String PAGES = "pages";

    public static String spKey(int num) {
        return num == 0 ? "" : Integer.toString(num);
    }

}
