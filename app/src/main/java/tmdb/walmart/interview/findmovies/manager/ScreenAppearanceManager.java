package tmdb.walmart.interview.findmovies.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.view.Display;

import java.util.Random;

/**
 * Created by Yee on 5/21/17.
 */

public class ScreenAppearanceManager {

    public static final String PREFERENCE = "screenPreference";
    public static final String SCREEN_WIDTH = "screenWidth";
    public static final String SCREEN_LENGTH = "screenLength";

    public int screenWidth;
    public int screenLength;

    private SharedPreferences mPrefs;
    Context mContext;

    public ScreenAppearanceManager(Context context) {
        mPrefs = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        mContext = context;
    }

    public void saveScreenAppearance(Display display) {
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenLength = size.y;
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(SCREEN_WIDTH, screenWidth);
        editor.putInt(SCREEN_LENGTH, screenLength);
        editor.apply();
    }

    public int getScreenWidth() {
        SharedPreferences prefs = mContext.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        return screenWidth = prefs.getInt(SCREEN_WIDTH, -1);
    }

    public int getScreenLength() {
        SharedPreferences prefs = mContext.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        return screenLength = prefs.getInt(SCREEN_LENGTH, -1);
    }
}

