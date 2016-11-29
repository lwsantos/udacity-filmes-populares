package br.com.lwsantos.filmespopulares.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lwsantos on 15/11/16.
 */

public class Filme implements Serializable {

    public final static String URL_IMAGEM = "http://image.tmdb.org/t/p/w185";
    private String tituloOriginal;
    private String titulo;
    private String posterPath;
    private String resumo;
    private Double mediaVoto;
    private Date dataLancamento;

    //Metodo para mapear uma String JSON em uma lista de Filmes.
    public ArrayList<Filme> parseJSON(String jsonStr)
            throws JSONException {

        ArrayList<Filme> lista = new ArrayList<>();

        JSONObject json= new JSONObject(jsonStr);
        JSONArray jsonResultado = json.getJSONArray("results");

        for(int i=0; i < jsonResultado.length(); i++)
        {
            Filme filme = new Filme();
            filme.setTituloOriginal(jsonResultado.getJSONObject(i).getString("original_title"));
            filme.setTitulo(jsonResultado.getJSONObject(i).getString("title"));
            filme.setPosterPath(jsonResultado.getJSONObject(i).getString("poster_path"));
            filme.setResumo(jsonResultado.getJSONObject(i).getString("overview"));
            filme.setMediaVoto(jsonResultado.getJSONObject(i).getDouble("vote_average"));

            String dateStr = jsonResultado.getJSONObject(i).getString("release_date");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                filme.setDataLancamento(sdf.parse(dateStr));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            lista.add(filme);
        }

        return lista;
    }

    public String getTituloOriginal() {
        return tituloOriginal;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setTituloOriginal(String tituloOriginal) {
        this.tituloOriginal = tituloOriginal;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public Double getMediaVoto() {
        return mediaVoto;
    }

    public void setMediaVoto(Double mediaVoto) {
        this.mediaVoto = mediaVoto;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }
}
