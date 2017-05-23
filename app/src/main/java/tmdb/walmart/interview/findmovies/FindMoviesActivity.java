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
import android.widget.Toast;

import tmdb.walmart.interview.findmovies.callback.MoviesDBServiceCallback;
import tmdb.walmart.interview.findmovies.fragment.MoviesSearchFragment;
import tmdb.walmart.interview.findmovies.manager.ScreenAppearanceManager;
import tmdb.walmart.interview.findmovies.model.MoviesDBService;
import tmdb.walmart.interview.findmovies.model.MoviesFolder;

public class FindMoviesActivity extends AppCompatActivity implements MoviesDBServiceCallback {
    public static final String TAG = "FindMoviesActivity";
    public static final String TAG_MOVIES_FOLDER = "FindMoviesActivityMoviesFolder";
    public static final String TAG_EDIT_TEXT_VISIBILITY = "FindMoviesActivityEditTextMoviesFolder";

    Toolbar mToolbar;
    ActionBar mActionBar;
    EditText mEditMoviesSearch;
    MoviesDBService mMoviesDBService;
    Fragment mMoviesSearchFragment;
    FragmentManager mFragmentManager;
    MoviesFolder mMoviesFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMoviesDBService = new MoviesDBService(this, this);
        ScreenAppearanceManager manager = new ScreenAppearanceManager(this);
        if (manager.getScreenLength() == -1) {
            manager.saveScreenAppearance(getWindowManager().getDefaultDisplay());
        }
        setContentView(R.layout.activity_find_movies);
        mFragmentManager = getSupportFragmentManager();
        mEditMoviesSearch = (EditText) findViewById(R.id.edit_movies_search);
        mToolbar = (Toolbar) findViewById(R.id.search_toolbar);
        mToolbar.setTitle(R.string.action_bar_title);
        mToolbar.setSubtitle(R.string.action_bar_subtitle);
        setSupportActionBar(mToolbar);
        if (savedInstanceState != null) {
            mMoviesFolder = savedInstanceState.getParcelable(TAG_MOVIES_FOLDER);
            if (mMoviesFolder == null) {
                mMoviesFolder = new MoviesFolder();
            }
            boolean visible = savedInstanceState.getBoolean(TAG_EDIT_TEXT_VISIBILITY, false);
            if (visible) {
                mEditMoviesSearch.setVisibility(View.VISIBLE);
                hideActionBarTitle();
            }
        }
        mEditMoviesSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideSoftKeyBoard();
                    String movie = mEditMoviesSearch.getText().toString();
                    mMoviesDBService.getInfo(movie);
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
                hideSoftKeyBoard();
            }
            hideActionBarTitle();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMoviesDBServiceSuccess(ProgressDialog dialog, MoviesFolder folder) {
        createFragment(mMoviesFolder = folder);
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

    private void hideSoftKeyBoard() {
        InputMethodManager manager = (InputMethodManager) this.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void createFragment(MoviesFolder folder) {
        mMoviesSearchFragment = mFragmentManager.findFragmentById(R.id
                .fragment_movies_search_container);
        if (mMoviesSearchFragment != null) {
            mFragmentManager.beginTransaction().remove(mMoviesSearchFragment).commit();
        }
        mMoviesSearchFragment = MoviesSearchFragment.newInstance(folder);
        mFragmentManager.beginTransaction().add(R.id.fragment_movies_search_container,
                mMoviesSearchFragment).commit();
    }

    private void hideActionBarTitle() {
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TAG_MOVIES_FOLDER, mMoviesFolder);
        boolean visible = mEditMoviesSearch.getVisibility() == View.VISIBLE;
        outState.putBoolean(TAG_EDIT_TEXT_VISIBILITY, visible);
    }
}
