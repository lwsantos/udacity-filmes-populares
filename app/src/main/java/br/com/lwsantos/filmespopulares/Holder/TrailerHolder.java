package br.com.lwsantos.filmespopulares.Holder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import br.com.lwsantos.filmespopulares.Model.Video;
import br.com.lwsantos.filmespopulares.R;

/**
 * Created by lwsantos on 25/03/17.
 */

public class TrailerHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{
    public final ImageView mImgTrailer;
    public ArrayList<Video> mListVideo;
    public Context mContext;

    public TrailerHolder(View view) {
        super(view);
        mImgTrailer = (ImageView) view.findViewById(R.id.imgTrailer);
        view.setOnClickListener(this);
    }

    //Metodo que abre o video ao clicar em um item.
    //O array de videos e o context são definidos no onCreateViewHolder VideoAdapter
    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();
        Video video = mListVideo.get(position);
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + video.getKey()));
        mContext.startActivity(intent);
    }
}
