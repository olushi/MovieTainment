package com.soundwebcraft.movietainment.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.soundwebcraft.movietainment.MovieDetailActivity;
import com.soundwebcraft.movietainment.R;
import com.soundwebcraft.movietainment.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    public static final String TAG = MoviesAdapter.class.getSimpleName(),
            HIGH_RES_POSTER = "HIGH_RES_POSTER",
            MOVIE_TITLE = "MOVIE_TITLE",
            MOVIE_ID = "MOVIE_ID";

    private List<Movie> mMovies;
    private Context mContext;
    private Toast mToast;
    public MoviesAdapter(Context context, List<Movie> movies) {
        mContext = context;
        mMovies = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);

        return new MovieViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        TextView textView = holder.titleTextView;
        textView.setText(movie.getOriginalTitle());

        ImageView imageView = holder.posterImage;
        // set the movie poster content description
        String movieContentDescription = "Movie: " + movie.getOriginalTitle();
        imageView.setContentDescription(movieContentDescription);
        // load the movie poster into the imageview
        Picasso.with(mContext)
                .load(movie.getPoster())
                .placeholder(R.drawable.loading)
                .error(R.drawable.no_preview)
                .into(imageView);

        TextView movieIDTextView = holder.movieIDTextView;
        movieIDTextView.setText(String.valueOf(movie.getID()));

        // add animations to item {row}
        Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        // hold reference to each views in row for caching
        @BindView(R.id.tv_item_movie) TextView titleTextView;
        @BindView(R.id.movie_poster) ImageView posterImage;
        @BindView(R.id.movie_id) TextView movieIDTextView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            // simplify viewholder findViewById using butterknife
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    int movieId = mMovies.get(position).getID();
                    String highResPoster = mMovies.get(position).getPoster(true);
                    String movieTitle = mMovies.get(position).getOriginalTitle();

                    Intent intent = new Intent(mContext, MovieDetailActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putInt(MOVIE_ID, movieId);
                    // send poster (high res) url to detail activity for fast loading (doesn't wait for getting movie clicked)
                    bundle.putString(HIGH_RES_POSTER, highResPoster);
                    bundle.putString(MOVIE_TITLE, movieTitle);
                    intent.putExtras(bundle);

                    mContext.startActivity(intent);
                }
            });
        }
    }
}
