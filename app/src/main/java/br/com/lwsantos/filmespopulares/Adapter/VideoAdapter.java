package br.com.lwsantos.filmespopulares.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import br.com.lwsantos.filmespopulares.Holder.TrailerHolder;
import br.com.lwsantos.filmespopulares.Model.Video;
import br.com.lwsantos.filmespopulares.R;

/**
 * Created by lwsantos on 25/03/17.
 */

public class VideoAdapter extends BaseAdapter {

    private ArrayList<Video> mListaVideos;
    private Context mContext;

    public VideoAdapter(Context context){
        mContext = context;
        mListaVideos = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mListaVideos.size();
    }

    @Override
    public Object getItem(int position) {
        return mListaVideos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TrailerHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_trailer, parent, false);
            viewHolder = new TrailerHolder(convertView);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (TrailerHolder) convertView.getTag();
        }

        if(mListaVideos.size() > 0) {
            viewHolder.mTxtNomeVideo.setText(mListaVideos.get(position).getName());
        }

        return convertView;
    }

    //Metodo para atualizar a lista de filmes do adaptador
    public void addAll(ArrayList<Video> lista){
        mListaVideos = lista;
        notifyDataSetChanged();
    }
}
