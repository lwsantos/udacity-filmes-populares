package br.com.lwsantos.filmespopulares.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.lwsantos.filmespopulares.R;

/**
 * Created by lwsantos on 25/03/17.
 */

public class TrailerHolder  extends RecyclerView.ViewHolder{
    public final TextView mTxtNomeVideo;

    public TrailerHolder(View view) {
        super(view);
        mTxtNomeVideo = (TextView) view.findViewById(R.id.txtNomeVideo);
    }
}
