package br.com.lwsantos.filmespopulares.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import br.com.lwsantos.filmespopulares.Control.MovieControl;
import br.com.lwsantos.filmespopulares.Data.MovieContract;
import br.com.lwsantos.filmespopulares.Delegate.AsyncTaskDelegate;
import br.com.lwsantos.filmespopulares.Model.Movie;

/**
 * Created by lwsantos on 11/06/17.
 */

public class MovieLocalAsync extends AsyncTask <Object, Void, ArrayList<Movie>>{

    private AsyncTaskDelegate mDelegate = null;
    private Context mContext;
    private String mClassificacao;

    public MovieLocalAsync(AsyncTaskDelegate delegate, Context context, String classificacao){
        mDelegate = delegate;
        mContext = context;
        mClassificacao = classificacao;
    }

    @Override
    protected ArrayList<Movie> doInBackground(Object... params) {
        ArrayList<Movie> lista = new ArrayList<>();

        if(mClassificacao == MovieContract.CLASSIFICACAO_POPULARIDADE) {
            lista = new MovieControl(mContext).listarPopulares();
        }
        else if(mClassificacao == MovieContract.CLASSIFICACAO_AVALIACAO) {
            lista = new MovieControl(mContext).listarAvaliacao();
        }
        else if(mClassificacao == MovieContract.CLASSIFICACAO_FAVORITO)
        {
            lista = new MovieControl(mContext).listarFavoritos();
        }

        return lista;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> filmes) {
        super.onPostExecute(filmes);

        Log.i("MOVIE Local", "Consultar dados");

        if(mDelegate != null)
            mDelegate.processFinish(filmes);
    }
}
