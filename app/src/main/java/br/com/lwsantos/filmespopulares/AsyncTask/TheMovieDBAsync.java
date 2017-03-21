package br.com.lwsantos.filmespopulares.AsyncTask;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import br.com.lwsantos.filmespopulares.Delegate.AsyncTaskDelegate;
import br.com.lwsantos.filmespopulares.Model.Filme;

/**
 * Created by lwsantos on 15/11/16.
 */

public class TheMovieDBAsync extends AsyncTask <Object, Void, ArrayList<Filme>> {

    private final static String KEY = "6b54a4341820c5980eabe7488c4c636d";
    private final static String LANGUAGE = "pt-BR";

    private AsyncTaskDelegate mDelegate = null;

    public TheMovieDBAsync(AsyncTaskDelegate delegate){
        mDelegate = delegate;
    }


    @Override
    protected ArrayList<Filme> doInBackground(Object... params) {

        String classificacao = (String) params[0];

        //Cria a URL https://api.themoviedb.org/3/movie/CLASSIFICACAO?api_key=KEY&language=IDIOMA
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(classificacao)
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
            }
            else {

                reader = new BufferedReader(new InputStreamReader(inputStream));

                //Realiza a leitura do retorno da API (em formato JSON) e armazena cada linha no buffer.
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                //Se houve um retorno da API armazena na variavel String
                if (buffer.length() == 0) {
                    return null;
                } else {
                    jsonStr = buffer.toString();
                    return new Filme().parseJSON(jsonStr);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
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

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Filme> filmes) {
        super.onPostExecute(filmes);
        if(mDelegate != null)
            mDelegate.processFinish(filmes);
    }
}
