package br.com.lwsantos.filmespopulares.Holder;

import android.view.View;
import android.widget.TextView;

import br.com.lwsantos.filmespopulares.R;

/**
 * Created by lwsantos on 25/03/17.
 */

public class TrailerHolder {
    public final TextView mTxtNomeVideo;

    public TrailerHolder(View view) {
        mTxtNomeVideo = (TextView) view.findViewById(R.id.txtNomeVideo);
    }
}
