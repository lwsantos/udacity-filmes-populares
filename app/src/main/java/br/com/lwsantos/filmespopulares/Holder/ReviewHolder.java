package br.com.lwsantos.filmespopulares.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.lwsantos.filmespopulares.R;

/**
 * Created by lwsantos on 26/03/17.
 */

public class ReviewHolder extends RecyclerView.ViewHolder{
    public final TextView mTxtReviewAutor;
    public final TextView mTxtReviewConteudo;

    public ReviewHolder(View itemView) {
        super(itemView);
        mTxtReviewConteudo = (TextView) itemView.findViewById(R.id.txtReviewConteudo);
        mTxtReviewAutor = (TextView) itemView.findViewById(R.id.txtReviewAutor);
    }
}
