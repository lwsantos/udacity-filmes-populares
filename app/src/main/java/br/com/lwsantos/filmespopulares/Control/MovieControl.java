package br.com.lwsantos.filmespopulares.Control;

import android.content.Context;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
                null,
                null,
                null
        );
        for(int i = 0; i < movieCursor.getCount(); i++){
            movieCursor.moveToPosition(i);
            Movie filme = new Movie();
            filme.setId(movieCursor.getLong(MovieContract.INDEX_ID_API));
            filme.setIdSQLite(movieCursor.getLong(MovieContract.INDEX_ID));
            filme.setTitulo(movieCursor.getString(MovieContract.INDEX_TITULO));
            filme.setResumo(movieCursor.getString(MovieContract.INDEX_SINOPSE));
            filme.setMediaVoto(movieCursor.getDouble(MovieContract.INDEX_MEDIA_VOTO));
            filme.setPosterLocalPath(movieCursor.getString(MovieContract.INDEX_POSTER));

            //Recupera a data da base local que esta armazenado como milesegundos.
            //Ao inserir no objeto, o valor em milisegundos (long) é convertido em data.
            long data_lancamento = movieCursor.getLong(MovieContract.INDEX_DATA_LANCAMENTO);
            filme.setDataLancamento(new Date(data_lancamento));

            lista.add(filme);

            movieCursor.moveToNext();
        }

        movieCursor.close();
        return lista;
    }

    //Metodo para mapear uma String JSON em uma lista de Filmes.
    public ArrayList<Movie> parseJSON(String jsonStr) throws JSONException {

        ArrayList<Movie> lista = new ArrayList<>();

        if(jsonStr != null) {

            JSONObject json = new JSONObject(jsonStr);
            JSONArray jsonResultado = json.getJSONArray("results");

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

                lista.add(filme);
            }
        }

        return lista;
    }
}
