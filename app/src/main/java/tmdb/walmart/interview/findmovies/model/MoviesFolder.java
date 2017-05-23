package tmdb.walmart.interview.findmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Yee on 5/21/17.
 */

public class MoviesFolder implements Parcelable, ParseData {
    private static final String TAG = "MoviesFolder";
    private ArrayList<Movie> mMovies;

    public MoviesFolder() {

    }

    protected MoviesFolder(Parcel in) {
        mMovies = in.createTypedArrayList(Movie.CREATOR);
    }

    public static final Creator<MoviesFolder> CREATOR = new Creator<MoviesFolder>() {
        @Override
        public MoviesFolder createFromParcel(Parcel in) {
            return new MoviesFolder(in);
        }

        @Override
        public MoviesFolder[] newArray(int size) {
            return new MoviesFolder[size];
        }
    };

    @Override
    public void parseData(JSONObject object) {
        JSONArray jsonArray = object.optJSONArray("results");
        int size = object.optInt("total_results");
        mMovies = new ArrayList<>();
        for (int i = 0; i < Math.min(20, size); i++) {
            Movie movie = new Movie();
            try {
                movie.parseData(jsonArray.getJSONObject(i));
                mMovies.add(movie);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    void setMovies(JSONObject object) {
        parseData(object);
    }

    public ArrayList<Movie> getMovies() {
        return mMovies;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mMovies);
    }
}
