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

import br.com.lwsantos.filmespopulares.Holder.TrailerHolder;
import br.com.lwsantos.filmespopulares.Model.Video;
import br.com.lwsantos.filmespopulares.R;

/**
 * Created by lwsantos on 25/03/17.
 */

public class VideoAdapter extends RecyclerView.Adapter<TrailerHolder>{

    private ArrayList<Video> mListaVideos;
    private Context mContext;
    private static final String YOUTUBE_THUMBNAIL = "https://img.youtube.com/vi/%s/mqdefault.jpg";

    public VideoAdapter(Context context){
        mContext = context;
        mListaVideos = new ArrayList<>();
    }

    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_trailer, null);
        TrailerHolder trailerHolder = new TrailerHolder(view);
        trailerHolder.mContext = mContext;
        trailerHolder.mListVideo = mListaVideos;
        return trailerHolder;
    }

    @Override
    public void onBindViewHolder(TrailerHolder holder, int position) {
        Video video = mListaVideos.get(position);

        Glide.with(mContext)
                .load(String.format(YOUTUBE_THUMBNAIL, video.getKey()))
                .placeholder(new ColorDrawable(mContext.getResources().getColor(R.color.accent_material_light)))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop()
                .crossFade()
                .into(holder.mImgTrailer);
    }

    @Override
    public int getItemCount() {
        return mListaVideos.size();
    }

    //Metodo para atualizar a lista de filmes do adaptador
    public void addAll(ArrayList<Video> lista){
        mListaVideos = lista;
        notifyDataSetChanged();
    }
}
