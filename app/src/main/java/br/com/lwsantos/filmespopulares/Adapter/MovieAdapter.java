package br.com.lwsantos.filmespopulares.Adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import br.com.lwsantos.filmespopulares.Holder.MovieHolder;
import br.com.lwsantos.filmespopulares.Interface.ItemClickListener;
import br.com.lwsantos.filmespopulares.Model.Movie;
import br.com.lwsantos.filmespopulares.R;

/**
 * Created by lwsantos on 15/11/16.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {

    private ArrayList<Movie> mListaFilmes;
    private Context mContext;

    //Variavel que responsavel pelo envendo de click
    private final ItemClickListener mClickListener;

    public MovieAdapter(Context context, ItemClickListener listener){
        mContext = context;
        mListaFilmes = new ArrayList<>();
        mClickListener = listener;
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, null);
        MovieHolder holder = new MovieHolder(view);
        //holder.mContext = mContext;
        //holder.mListaFilmes = mListaFilmes;
        return holder;
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {

        holder.bind(mClickListener);

        Glide.with(mContext)
                .load(Movie.URL_IMAGEM + mListaFilmes.get(position).getPosterPath())
                .placeholder(new ColorDrawable(mContext.getResources().getColor(R.color.accent_material_light)))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop()
                .crossFade()
                .into(holder.mImgPoster);
    }

    @Override
    public int getItemCount() {
        return mListaFilmes.size();
    }

    public Movie getItem(int position){
        return mListaFilmes.get(position);
    }

    //Metodo para atualizar a lista de filmes do adaptador
    public void addAll(ArrayList<Movie> lista){
        mListaFilmes = lista;
        notifyDataSetChanged();
    }
}
