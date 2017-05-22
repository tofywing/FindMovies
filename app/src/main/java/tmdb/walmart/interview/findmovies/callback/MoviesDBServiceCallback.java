package tmdb.walmart.interview.findmovies.callback;

import android.app.ProgressDialog;

import java.util.ArrayList;

import tmdb.walmart.interview.findmovies.model.Movie;
import tmdb.walmart.interview.findmovies.model.MoviesFolder;

/**
 * Created by Yee on 5/20/17.
 */

public interface MoviesDBServiceCallback {

    void onMoviesDBServiceSuccess(ProgressDialog dialog, MoviesFolder folder);

    void onMoviesDBServiceFailed(ProgressDialog dialog);
}
