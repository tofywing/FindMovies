package tmdb.walmart.interview.findmovies.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

import tmdb.walmart.interview.findmovies.FindMoviesActivity;
import tmdb.walmart.interview.findmovies.R;
import tmdb.walmart.interview.findmovies.callback.MoviesDBServiceCallback;

/**
 * Created by Yee on 5/20/17.
 */

public class MoviesDBService {
    public static final String MOVIES_DB_URL = "https://api.themoviedb" +
            ".org/3/search/movie?api_key=90e28f2839c7611f0641c8646f7353d3&language=en-US&query" +
            "=%s&page=1&include_adult=false";

    private Context mContext;
    private MoviesDBServiceCallback mDBServiceCallback;
    private AsyncTask mAsyncTask;
    private String mDataError;
    private String mConnectionError;
    private String mMovieName;
    private ProgressDialog mProgressDialog;

    public MoviesDBService(Context context, MoviesDBServiceCallback callback) {
        mContext = context;
        mDBServiceCallback = callback;
    }

    public void getInfo(final String movieName) {
        mMovieName = movieName.replaceAll("[\\s]", "%20").toLowerCase();
        showProgressDialog(mContext);
        mAsyncTask = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(String.format(Locale.US, MOVIES_DB_URL, params[0]));
                    URLConnection urlConnection = url.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader
                            (inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    return stringBuilder.length() == 0 ? (mDataError = mContext.getString(R.string
                            .server_data_error)) : stringBuilder.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return mConnectionError = mContext.getString(R.string.server_connection_error);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals(mDataError) || s.equals(mConnectionError)) {
                    mDBServiceCallback.onMoviesDBServiceFailed(mProgressDialog);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        MoviesFolder moviesFolder = new MoviesFolder();
                        moviesFolder.setMovies(jsonObject);
                        mDBServiceCallback.onMoviesDBServiceSuccess(mProgressDialog, moviesFolder);
                    } catch (JSONException e) {
                        mDBServiceCallback.onMoviesDBServiceFailed(mProgressDialog);
                        e.printStackTrace();
                    }
                }
            }
        }.execute(mMovieName);
    }

    private void showProgressDialog(Context context) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(mContext.getString(R.string.movies_loading));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext, "sdfdsf", Toast.LENGTH_SHORT).show();
                stopService();
            }
        });
        mProgressDialog.show();
    }

    private void stopService() {
        mAsyncTask.cancel(true);
        mDBServiceCallback.onMoviesDBServiceFailed(mProgressDialog);
    }
}
