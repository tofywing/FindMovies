package tmdb.walmart.interview.findmovies.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Yee on 5/22/17.
 */

public class SearchStateManager {
    public static final String PREFERENCE = "moviesPreference";
    public static final String MOVIE_NAME = "movieName";

    private SharedPreferences mPrefs;
    private Context mContext;
    private String movieName;

    public SearchStateManager(Context context) {
        mPrefs = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        mContext = context;
    }

    public void saveMovieName(String movieName) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(MOVIE_NAME, movieName);
        editor.apply();
    }

    public String getMovieName() {
        return movieName = mPrefs.getString(MOVIE_NAME, "");
    }
}
