package br.com.lwsantos.filmespopulares.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.lwsantos.filmespopulares.Model.Filme;

/**
 * Created by lwsantos on 15/11/16.
 */

public class ImageAdapter extends BaseAdapter {

    private ArrayList<Filme> mListaFilmes;
    private Context mContext;

    public ImageAdapter(Context context){
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

        ImageView imagemFilme;

        if(convertView == null){
            // Configura o componente de imagem
            int widthScreen = parent.getWidth(); //Pega a largura do GridView
            int widthImagem = widthScreen/2; // A lagura da imagem sera a metade da largura do GridView
            int heightImagem = (int)(widthImagem * 1.35); // A altura da imagem sera 1,35 vezes a largura

            imagemFilme = new ImageView(mContext);
            imagemFilme.setLayoutParams(new GridView.LayoutParams(widthImagem,heightImagem));
            imagemFilme.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imagemFilme.setPadding(8, 8, 8, 8);
        }
        else{
            imagemFilme = (ImageView) convertView;
        }

        Picasso.with(mContext).load(Filme.URL_IMAGEM + mListaFilmes.get(position).getPosterPath()).into(imagemFilme);

        return imagemFilme;

    }

    //Metodo para atualizar a lista de filmes do adaptador
    public void addAll(ArrayList<Filme> lista){
        mListaFilmes = lista;
        notifyDataSetChanged();
    }
}
