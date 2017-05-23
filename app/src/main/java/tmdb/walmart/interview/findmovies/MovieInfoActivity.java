package tmdb.walmart.interview.findmovies;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import tmdb.walmart.interview.findmovies.manager.ScreenAppearanceManager;
import tmdb.walmart.interview.findmovies.model.Movie;

import static tmdb.walmart.interview.findmovies.adapter.MoviesListAdapter.TAG_MOVIE;
import static tmdb.walmart.interview.findmovies.adapter.MoviesListAdapter.URL_PREFIX;

/**
 * Created by yijin on 5/22/17.
 */

public class MovieInfoActivity extends AppCompatActivity {
    public static final String TAG = "MovieInfoActivity";

    ActionBar mActionBar;
    TextView mMovieTitle;
    TextView mMovieDescription;
    TextView mVoteCount;
    TextView mMovieScore;
    ImageView mMoviePoster;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_info);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable
                .abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.pureBlack), PorterDuff.Mode
                .SRC_ATOP);
        Movie movie = getIntent().getParcelableExtra(TAG_MOVIE);
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setHomeAsUpIndicator(upArrow);
        }
        mMovieTitle = (TextView) findViewById(R.id.info_movie_title);
        mMovieTitle.setText(movie.getTitle());
        mMovieDescription = (TextView) findViewById(R.id.info_movie_overview);
        mMovieDescription.setText(movie.getOverview());
        mVoteCount = (TextView) findViewById(R.id.info_vote_count);
        mVoteCount.setText(movie.getVoteCount());
        mMovieScore = (TextView) findViewById(R.id.info_movie_score);
        mMovieScore.setText(movie.getVoteAverage());
        mMoviePoster = (ImageView) findViewById(R.id.info_movie_poster);
        String url = URL_PREFIX + movie.getPoster_path();
        ScreenAppearanceManager manager = new ScreenAppearanceManager(this);
        int length = manager.getScreenLength();
        int width = manager.getScreenWidth();
        final int refs = (int) (width * 0.50);
        Picasso.with(this).load(url).centerCrop().resize(refs, (int) (refs *
                1.5)).into(mMoviePoster, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError() {
                Picasso.with(getApplicationContext()).load(R.drawable.no_image_available)
                        .centerCrop().resize
                        (refs, (int) (refs * 1.5)).into(mMoviePoster);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

