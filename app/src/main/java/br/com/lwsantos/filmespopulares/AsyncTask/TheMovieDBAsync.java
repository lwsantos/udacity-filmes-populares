package br.com.lwsantos.filmespopulares.AsyncTask;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.lwsantos.filmespopulares.BuildConfig;
import br.com.lwsantos.filmespopulares.Control.MovieControl;
import br.com.lwsantos.filmespopulares.Data.MovieContract;
import br.com.lwsantos.filmespopulares.Delegate.AsyncTaskDelegate;

/**
 * Created by lwsantos on 15/11/16.
 */

public class TheMovieDBAsync extends AsyncTask <Object, Void, Void> {

    private final static String KEY = BuildConfig.THE_MOVIE_DB_API_KEY;
    private final static String LANGUAGE = "pt-BR";

    private AsyncTaskDelegate mDelegate = null;
    private Context mContext;
    private String mClassificacao;

    public TheMovieDBAsync(AsyncTaskDelegate delegate, Context context, String classificacao){
        mDelegate = delegate;
        mContext = context;
        mClassificacao = classificacao;
    }


    @Override
    protected Void doInBackground(Object... params) {

        //Verifica se a classificação esta definido como favorito
        //Se sim, busca os dados na base local.
        //Senão, consulta pelo webservice
        if(!mClassificacao.equals(MovieContract.CLASSIFICACAO_FAVORITO))
        {
            //Cria a URL https://api.themoviedb.org/3/movie/CLASSIFICACAO?api_key=KEY&language=IDIOMA
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(mClassificacao)
                    .appendQueryParameter("api_key", KEY)
                    .appendQueryParameter("language", LANGUAGE);

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Variavel que armazena o valor retornado da API em formato JSON String.
            String jsonStr = null;

            try {
                URL url = new URL(builder.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                StringBuffer buffer = new StringBuffer();
                InputStream inputStream = urlConnection.getInputStream();

                if (inputStream == null) {
                    jsonStr = null;
                } else {

                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    //Realiza a leitura do retorno da API (em formato JSON) e armazena cada linha no buffer.
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }

                    //Se houve um retorno da API armazena na variavel String
                    if (buffer.length() > 0) {
                        jsonStr = buffer.toString();

                        ContentValues[] movieValues = new MovieControl(mContext).parseJSON(jsonStr, mClassificacao);
                        //Insere valores e atualiza a base de dados local
                        new MovieControl(mContext).builkInsert(movieValues);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        new MovieLocalAsync(mDelegate, mContext, mClassificacao).execute();
    }
}
