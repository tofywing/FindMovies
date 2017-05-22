package tmdb.walmart.interview.findmovies.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import tmdb.walmart.interview.findmovies.R;
import tmdb.walmart.interview.findmovies.manager.ScreenAppearanceManager;
import tmdb.walmart.interview.findmovies.model.Movie;

/**
 * Created by Yee on 5/20/17.
 */

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MovieHolder> {
    public static final String URL_PREFIX = "https://image.tmdb.org/t/p/original";

    private ArrayList<Movie> mMovies;
    private ScreenAppearanceManager mScreenManager;
    private Context mContext;

    public MoviesListAdapter(ArrayList<Movie> movies) {
        mMovies = movies;
    }

    class MovieHolder extends RecyclerView.ViewHolder {
        ImageView mMovieImage;
        TextView mMovieTitle;
        TextView mMovieVoteScore;
        TextView mMovieReleaseDate;

        MovieHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mScreenManager = new ScreenAppearanceManager(mContext);
            mMovieImage = (ImageView) itemView.findViewById(R.id.search_movie_poster);
            mMovieTitle = (TextView) itemView.findViewById(R.id.search_movie_title);
            mMovieVoteScore = (TextView) itemView.findViewById(R.id.search_vote_score);
            mMovieReleaseDate = (TextView) itemView.findViewById(R.id.search_release_date);
        }
    }

    @Override
    public MoviesListAdapter.MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_search_item,
                parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(final MoviesListAdapter.MovieHolder holder, int position) {
        Movie movie = mMovies.get(position);
        holder.mMovieTitle.setText(movie.getTitle());
        holder.mMovieVoteScore.setText(movie.getVoteAverage());
        holder.mMovieReleaseDate.setText(movie.getReleaseDate());
        int width = mScreenManager.getScreenWidth();
        int length = mScreenManager.getScreenLength();
        String url = URL_PREFIX + movie.getPoster_path();
        Picasso.with(mContext).load(url).centerCrop().resize(360, 540).into(holder
                .mMovieImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(mContext).load(R.drawable.no_image_available).centerCrop().resize
                        (360, 540).into(holder.mMovieImage);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }
}
