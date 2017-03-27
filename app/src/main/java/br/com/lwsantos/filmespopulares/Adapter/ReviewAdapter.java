package br.com.lwsantos.filmespopulares.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.com.lwsantos.filmespopulares.Holder.ReviewHolder;
import br.com.lwsantos.filmespopulares.Model.Review;
import br.com.lwsantos.filmespopulares.R;

/**
 * Created by lwsantos on 26/03/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewHolder> {

    private ArrayList<Review> mListaReview;
    private Context mContext;

    public ReviewAdapter(Context context){
        mContext = context;
        mListaReview = new ArrayList<>();
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_review, null);
        ReviewHolder reviewHolder = new ReviewHolder(view);
        return reviewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        Review review = mListaReview.get(position);

        holder.mTxtReviewConteudo.setText(review.getConteudo());
        holder.mTxtReviewAutor.setText(review.getAutor());
    }

    @Override
    public int getItemCount() {
        return mListaReview.size();
    }

    //Metodo para atualizar a lista de reviews do adaptador
    public void addAll(ArrayList<Review> lista){
        mListaReview = lista;
        notifyDataSetChanged();
    }
}
