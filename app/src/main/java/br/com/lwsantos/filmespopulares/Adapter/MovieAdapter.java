package br.com.lwsantos.filmespopulares.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.lwsantos.filmespopulares.Holder.MovieHolder;
import br.com.lwsantos.filmespopulares.Model.Filme;
import br.com.lwsantos.filmespopulares.R;

/**
 * Created by lwsantos on 15/11/16.
 */

public class MovieAdapter extends BaseAdapter {

    private ArrayList<Filme> mListaFilmes;
    private Context mContext;

    public MovieAdapter(Context context){
        mContext = context;
        mListaFilmes = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mListaFilmes.size();
    }

    @Override
    public Object getItem(int position) {
        return mListaFilmes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_movie, parent, false);
            viewHolder = new MovieHolder(convertView, parent.getWidth());

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (MovieHolder) convertView.getTag();
        }

        if(mListaFilmes.size() > 0) {

            Picasso.with(mContext).load(Filme.URL_IMAGEM + mListaFilmes.get(position).getPosterPath()).into(viewHolder.mImgPoster);
        }

        return convertView;

    }

    //Metodo para atualizar a lista de filmes do adaptador
    public void addAll(ArrayList<Filme> lista){
        mListaFilmes = lista;
        notifyDataSetChanged();
    }
}
