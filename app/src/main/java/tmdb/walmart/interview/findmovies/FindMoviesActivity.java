package tmdb.walmart.interview.findmovies;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import tmdb.walmart.interview.findmovies.callback.MoviesDBServiceCallback;
import tmdb.walmart.interview.findmovies.fragment.MoviesSearchFragment;
import tmdb.walmart.interview.findmovies.manager.ScreenAppearanceManager;
import tmdb.walmart.interview.findmovies.manager.SearchStateManager;
import tmdb.walmart.interview.findmovies.model.MoviesDBService;
import tmdb.walmart.interview.findmovies.model.MoviesFolder;
import tmdb.walmart.interview.findmovies.tool.ApplicationDataRefresher;

public class FindMoviesActivity extends AppCompatActivity implements MoviesDBServiceCallback {
    public static final String TAG = "FindMoviesActivity";

    Toolbar mToolbar;
    ActionBar mActionBar;
    EditText mEditMoviesSearch;
    MoviesDBService mMoviesDBService;
    Fragment mMoviesSearchFragment;
    FragmentManager mFragmentManager;
    String mCurrentMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationDataRefresher refresher = new ApplicationDataRefresher(this);
        refresher.clearApplicationData();
        mMoviesDBService = new MoviesDBService(this, this);
        getScreenAppearance();
        setContentView(R.layout.activity_find_movies);
        mFragmentManager = getSupportFragmentManager();
        mEditMoviesSearch = (EditText) findViewById(R.id.edit_movies_search);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.post(new Runnable() {
            @Override
            public void run() {
                mActionBar = getSupportActionBar();
                if (mActionBar != null) {
                    getSupportActionBar().setTitle(getString(R.string.action_bar_title));
                    getSupportActionBar().setSubtitle(getString(R.string.action_bar_comment));
                }
            }
        });
        setSupportActionBar(mToolbar);
        mEditMoviesSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideSoftKeyBoard();
                    mCurrentMovie = mEditMoviesSearch.getText().toString();
                    mMoviesDBService.getInfo(mCurrentMovie);
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SearchStateManager manager = new SearchStateManager(this);
        mCurrentMovie = manager.getMovieName();
        mEditMoviesSearch.setText(mCurrentMovie);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SearchStateManager manager = new SearchStateManager(this);
        manager.saveMovieName(mCurrentMovie);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_find_movies_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.app_bar_search) {
            mEditMoviesSearch.setVisibility(View.VISIBLE);
            String input = mEditMoviesSearch.getText().toString();
            if (!input.isEmpty()) {
                mMoviesDBService.getInfo(input);
                if (this.getCurrentFocus() != null && this.getCurrentFocus() instanceof EditText) {
                    hideSoftKeyBoard();
                }
            }
            mActionBar.setDisplayShowTitleEnabled(false);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMoviesDBServiceSuccess(ProgressDialog dialog, MoviesFolder folder) {
        mMoviesSearchFragment = MoviesSearchFragment.newInstance(folder);
        if (!mMoviesSearchFragment.isAdded()) {
            mFragmentManager.beginTransaction().add(R.id.fragment_movies_search_container,
                    mMoviesSearchFragment).commitAllowingStateLoss();
        }
        dialog.dismiss();
    }

    @Override
    public void onMoviesDBServiceFailed(ProgressDialog dialog) {
        Log.d(TAG, "onMoviesDBServiceFailed");
        dialog.dismiss();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getScreenAppearance() {
        ScreenAppearanceManager screenManager = new ScreenAppearanceManager(this);
        if ((screenManager.screenWidth = screenManager.getScreenWidth()) == -1) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenManager.saveScreenAppearance(display);
            screenManager.screenWidth = screenManager.getScreenWidth();
        }
        screenManager.screenLength = screenManager.getScreenLength();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    private void hideSoftKeyBoard() {
        InputMethodManager manager = (InputMethodManager) this.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
