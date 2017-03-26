package br.com.lwsantos.filmespopulares.Control;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.lwsantos.filmespopulares.Model.Video;

/**
 * Created by lwsantos on 25/03/17.
 */

public class VideoControl {

    //Metodo para mapear uma String JSON em uma lista de Videos de um determinado Filme.
    public ArrayList<Video> parseJSON(String jsonStr)
            throws JSONException {

        ArrayList<Video> lista = new ArrayList<>();

        if(jsonStr != null) {

            JSONObject json = new JSONObject(jsonStr);
            JSONArray jsonResultado = json.getJSONArray("results");

            for (int i = 0; i < jsonResultado.length(); i++) {
                Video video = new Video();

                video.setKey(jsonResultado.getJSONObject(i).optString("key"));
                video.setName(jsonResultado.getJSONObject(i).optString("name"));
                video.setSite(jsonResultado.getJSONObject(i).optString("site"));
                video.setType(jsonResultado.getJSONObject(i).optString("type"));

                lista.add(video);
            }
        }

        return lista;
    }
}
