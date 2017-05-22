package tmdb.walmart.interview.findmovies.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import tmdb.walmart.interview.findmovies.adapter.MoviesListAdapter;
import tmdb.walmart.interview.findmovies.R;
import tmdb.walmart.interview.findmovies.callback.MoviesDBServiceCallback;
import tmdb.walmart.interview.findmovies.model.Movie;
import tmdb.walmart.interview.findmovies.model.MoviesDBService;
import tmdb.walmart.interview.findmovies.model.MoviesFolder;

/**
 * Created by Yee on 5/20/17.
 */

public class MoviesSearchFragment extends Fragment {
    public static final String TAG = "MoviesSearchFragment";
    public static final String TAG_MOVIES = "MoviesFromMoviesDBService";

    EditText mEditMovies;
    Button mSubmitButton;
    RecyclerView mMoviesRecyclerView;
    MoviesDBService mMoviesDBService;
    ArrayList<Movie> mMovies;

    public static MoviesSearchFragment newInstance(MoviesFolder folder) {
        Bundle args = new Bundle();
        args.putParcelable(TAG_MOVIES,folder);
        MoviesSearchFragment fragment = new MoviesSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_search, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mMoviesRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_movies_list);
        mMoviesRecyclerView.setLayoutManager(linearLayoutManager);
        mMoviesRecyclerView.setHasFixedSize(true);
        setupAdapter();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupAdapter() {
        MoviesFolder folder = getArguments().getParcelable(TAG_MOVIES);
        assert folder != null;
        ArrayList<Movie> movies = folder.getMovies();
        MoviesListAdapter adapter = new MoviesListAdapter(movies);
        mMoviesRecyclerView.setAdapter(adapter);
    }
}
