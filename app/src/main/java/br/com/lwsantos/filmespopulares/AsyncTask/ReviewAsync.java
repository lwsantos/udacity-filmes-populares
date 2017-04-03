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

import br.com.lwsantos.filmespopulares.BuildConfig;
import br.com.lwsantos.filmespopulares.Control.ReviewControl;
import br.com.lwsantos.filmespopulares.Delegate.AsyncTaskDelegate;
import br.com.lwsantos.filmespopulares.Model.Review;

/**
 * Created by lwsantos on 26/03/17.
 */

public class ReviewAsync extends AsyncTask<Object, Void, ArrayList<Review>> {
    private final static String KEY = BuildConfig.THE_MOVIE_DB_API_KEY;
    private AsyncTaskDelegate mDelegate = null;

    public ReviewAsync(AsyncTaskDelegate delegate){
        mDelegate = delegate;
    }

    @Override
    protected ArrayList<Review> doInBackground(Object... params) {
        ArrayList<Review> lista = new ArrayList<>();
        String idFilme = String.valueOf(params[0]);

        //Cria a  URL https://api.themoviedb.org/3/movie/[ID MOVIE]/reviews?api_key=[KEY]
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(idFilme)
                .appendPath("reviews")
                .appendQueryParameter("api_key", KEY);

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
                if (buffer.length() == 0) {
                    return null;
                } else {
                    jsonStr = buffer.toString();
                    lista = new ReviewControl().parseJSON(jsonStr);
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

        return lista;
    }

    @Override
    protected void onPostExecute(ArrayList<Review> reviews) {
        super.onPostExecute(reviews);
        if(mDelegate != null)
            mDelegate.processFinish(reviews);
    }
}
