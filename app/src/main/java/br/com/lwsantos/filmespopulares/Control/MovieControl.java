package br.com.lwsantos.filmespopulares.Control;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;

import br.com.lwsantos.filmespopulares.Data.MovieContract;
import br.com.lwsantos.filmespopulares.Model.Filme;

/**
 * Created by lwsantos on 25/03/17.
 */

public class MovieControl {
    Context mContext;

    public MovieControl(Context context)
    {
        mContext = context;
    }

    public ArrayList<Filme> listarFavoritos()
    {
        ArrayList<Filme> lista = new ArrayList<Filme>();

        // Realiza uma consulta de todos os filmes inseridos na base local.

        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.CONTENT_URI,
                MovieContract.MOVIE_PROJECTION,
                null,
                null,
                null
        );

        //if(movieCursor.moveToFirst()) {
        for(int i = 0; i < movieCursor.getCount(); i++){
            movieCursor.moveToPosition(i);
        //    do {
                Filme filme = new Filme();
                filme.setIdSQLite(movieCursor.getLong(MovieContract.INDEX_ID));
                filme.setTitulo(movieCursor.getString(MovieContract.INDEX_TITULO));
                filme.setResumo(movieCursor.getString(MovieContract.INDEX_SINOPSE));
                filme.setMediaVoto(movieCursor.getDouble(MovieContract.INDEX_MEDIA_VOTO));
                filme.setPosterPath(movieCursor.getString(MovieContract.INDEX_POSTER));

                //Recupera a data da base local que esta armazenado como milesegundos.
                //Ao inserir no objeto, o valor em milisegundos (long) é convertido em data.
                long data_lancamento = movieCursor.getLong(MovieContract.INDEX_DATA_LANCAMENTO);
                filme.setDataLancamento(new Date(data_lancamento));

                lista.add(filme);

                movieCursor.moveToNext();
        //    } while (movieCursor.moveToNext());
        }

        movieCursor.close();
        return lista;
    }
}
