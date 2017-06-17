package br.com.lwsantos.filmespopulares.Control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import br.com.lwsantos.filmespopulares.Data.MovieContract;
import br.com.lwsantos.filmespopulares.Model.Movie;

/**
 * Created by lwsantos on 25/03/17.
 */

public class MovieControl {
    Context mContext;

    public MovieControl(Context context)
    {
        mContext = context;
    }

    public ArrayList<Movie> listarFavoritos() {
        ArrayList<Movie> lista = new ArrayList<Movie>();

        // Realiza uma consulta de todos os filmes inseridos na base local.

        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.CONTENT_URI,
                MovieContract.MOVIE_PROJECTION,
                MovieContract.COLUMN_FAVORITO + "=?",
                new String[]{String.valueOf("1")},
                null
        );
        for(int i = 0; i < movieCursor.getCount(); i++){
            movieCursor.moveToPosition(i);

            Movie filme = mapearDados(movieCursor);
            lista.add(filme);

            movieCursor.moveToNext();
        }

        movieCursor.close();
        return lista;
    }

    public ArrayList<Movie> listarPopulares() {
        ArrayList<Movie> lista = new ArrayList<Movie>();

        // Realiza uma consulta de todos os filmes inseridos na base local.

        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.CONTENT_URI,
                MovieContract.MOVIE_PROJECTION,
                MovieContract.COLUMN_POPULAR + "= ?",
                new String[]{String.valueOf("1")},
                null
        );
        for(int i = 0; i < movieCursor.getCount(); i++){
            movieCursor.moveToPosition(i);

            Movie filme = mapearDados(movieCursor);
            lista.add(filme);

            movieCursor.moveToNext();
        }

        movieCursor.close();
        return lista;
    }

    public ArrayList<Movie> listarAvaliacao() {
        ArrayList<Movie> lista = new ArrayList<Movie>();

        // Realiza uma consulta de todos os filmes inseridos na base local.

        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.CONTENT_URI,
                MovieContract.MOVIE_PROJECTION,
                MovieContract.COLUMN_TOP_RATED + "= ?",
                new String[]{String.valueOf("1")},
                null
        );
        for(int i = 0; i < movieCursor.getCount(); i++){
            movieCursor.moveToPosition(i);

            Movie filme = mapearDados(movieCursor);
            lista.add(filme);

            movieCursor.moveToNext();
        }

        movieCursor.close();
        return lista;
    }

    public void builkInsert(ContentValues[] movieValues)
    {
        String selection = MovieContract.COLUMN_ID_API + "> ?";
        String[] selectionArgs = new String[]{String.valueOf("0")};

        ContentValues movieUpdate = new ContentValues();
        movieUpdate.put(MovieContract.COLUMN_POPULAR, false);
        movieUpdate.put(MovieContract.COLUMN_TOP_RATED, false);

        // Limpa os campos que identificam o tipo de classificacao, passando como parametro a URI:
        // content://br.com.lwsantos.filmespopulares/movie
        mContext.getContentResolver().update(MovieContract.CONTENT_URI, movieUpdate, selection, selectionArgs);

        // Aciona o provider para inserir os valores, passando como parametro a URI:
        // content://br.com.lwsantos.filmespopulares/movie
        mContext.getContentResolver().bulkInsert(MovieContract.CONTENT_URI, movieValues);
    }

    public void addFavorite(Movie filme)
    {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.COLUMN_FAVORITO, filme.getFavorito());

        String selection = MovieContract.COLUMN_ID_API + "=?";
        String[] selectionArgs = new String[]{String.valueOf(filme.getId())};

        // Aciona o provider para inserir os valores, passando como parametro a URI:
        // content://br.com.lwsantos.filmespopulares/movie
        mContext.getContentResolver().update(MovieContract.CONTENT_URI, movieValues, selection, selectionArgs);
    }

    //Metodo para mapear uma String JSON em uma lista de Filmes.
    public ContentValues[] parseJSON(String jsonStr, String classificacao) throws JSONException {

        if(jsonStr != null) {

            JSONObject json = new JSONObject(jsonStr);
            JSONArray jsonResultado = json.getJSONArray("results");

            // Vetor para inserir filmes na base de dados
            Vector<ContentValues> vFilmesValores = new Vector<ContentValues>(jsonResultado.length());

            for (int i = 0; i < jsonResultado.length(); i++) {
                Movie filme = new Movie();

                /* Pode acontecer em alguma situação do campo overview não vir preenchido ou não existir no JSON?
                Se sim, o ideal neste caso é utilizar o método optString() ao invés do getString(),
                pois, utilizando o getString(), caso o JSON não contenha a propriedade que você está tentando recuperar o valor,
                um erro será lançado e o funcionamento da sua aplicação pode ser afetado. */

                filme.setId(jsonResultado.getJSONObject(i).getLong("id"));
                filme.setTituloOriginal(jsonResultado.getJSONObject(i).optString("original_title"));
                filme.setTitulo(jsonResultado.getJSONObject(i).optString("title"));
                filme.setPosterPath(jsonResultado.getJSONObject(i).optString("poster_path"));
                filme.setResumo(jsonResultado.getJSONObject(i).optString("overview"));
                filme.setMediaVoto(jsonResultado.getJSONObject(i).optDouble("vote_average"));

                String dateStr = jsonResultado.getJSONObject(i).optString("release_date");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    filme.setDataLancamento(sdf.parse(dateStr));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                ContentValues movieValue = new ContentValues();

                movieValue.put(MovieContract.COLUMN_ID_API, filme.getId());
                movieValue.put(MovieContract.COLUMN_TITULO, filme.getTitulo());
                movieValue.put(MovieContract.COLUMN_POSTER, filme.getPosterPath());
                movieValue.put(MovieContract.COLUMN_SINOPSE, filme.getResumo());
                movieValue.put(MovieContract.COLUMN_MEDIA_VOTO, filme.getMediaVoto());
                movieValue.put(MovieContract.COLUMN_DATA_LANCAMENTO, filme.getDataLancamento().getTime());

                if(classificacao == MovieContract.CLASSIFICACAO_POPULARIDADE)
                {
                    movieValue.put(MovieContract.COLUMN_POPULAR, true);
                }
                else if(classificacao == MovieContract.CLASSIFICACAO_AVALIACAO)
                {
                    movieValue.put(MovieContract.COLUMN_TOP_RATED, true);
                }

                vFilmesValores.add(movieValue);
            }

            // Converte o vetor em um array de Content Values
            if ( vFilmesValores.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[vFilmesValores.size()];
                vFilmesValores.toArray(cvArray);
                return cvArray;
            }
        }

        return null;
    }

    private Movie mapearDados(Cursor movieCursor)
    {
        Movie filme = new Movie();

        filme.setId(movieCursor.getLong(MovieContract.INDEX_ID_API));
        filme.setIdSQLite(movieCursor.getLong(MovieContract.INDEX_ID));
        filme.setTitulo(movieCursor.getString(MovieContract.INDEX_TITULO));
        filme.setResumo(movieCursor.getString(MovieContract.INDEX_SINOPSE));
        filme.setMediaVoto(movieCursor.getDouble(MovieContract.INDEX_MEDIA_VOTO));
        filme.setPosterPath(movieCursor.getString(MovieContract.INDEX_POSTER));

        //Recupera a data da base local que esta armazenado como milesegundos.
        //Ao inserir no objeto, o valor em milisegundos (long) é convertido em data.
        long data_lancamento = movieCursor.getLong(MovieContract.INDEX_DATA_LANCAMENTO);
        filme.setDataLancamento(new Date(data_lancamento));

        int favorito = movieCursor.getInt(MovieContract.INDEX_FAVORITO);
        if(favorito > 0)
            filme.setFavorito(true);
        else
            filme.setFavorito(false);

        return filme;
    }
}
