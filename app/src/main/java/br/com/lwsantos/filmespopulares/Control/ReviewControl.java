package br.com.lwsantos.filmespopulares.Control;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.lwsantos.filmespopulares.Model.Review;

/**
 * Created by lwsantos on 26/03/17.
 */

public class ReviewControl {

    //Metodo para mapear uma String JSON em uma lista de Reviews de um determinado Filme.
    public ArrayList<Review> parseJSON(String jsonStr)
            throws JSONException {

        ArrayList<Review> lista = new ArrayList<>();

        if(jsonStr != null) {

            JSONObject json = new JSONObject(jsonStr);
            JSONArray jsonResultado = json.getJSONArray("results");

            for (int i = 0; i < jsonResultado.length(); i++) {
                Review review = new Review();

                review.setAutor(jsonResultado.getJSONObject(i).optString("author"));
                review.setConteudo(jsonResultado.getJSONObject(i).optString("content"));

                lista.add(review);
            }
        }

        return lista;
    }
}
