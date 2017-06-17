package br.com.lwsantos.filmespopulares.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import br.com.lwsantos.filmespopulares.Interface.ItemClickListener;
import br.com.lwsantos.filmespopulares.R;

/**
 * Created by lwsantos on 02/04/17.
 */

public class MovieHolder extends RecyclerView.ViewHolder {

    public final ImageView mImgPoster;

    public MovieHolder(View view){
        super(view);

        mImgPoster = (ImageView) view.findViewById(R.id.imgPoster);
    }

    public void bind(final ItemClickListener listener)
    {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(getAdapterPosition());
            }
        });
    }
}
