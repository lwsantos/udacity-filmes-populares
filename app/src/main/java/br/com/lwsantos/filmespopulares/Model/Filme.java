package br.com.lwsantos.filmespopulares.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lwsantos on 15/11/16.
 */

public class Filme implements Parcelable {

    public static final String PARCELABLE_KEY = "filme";
    public static final String URL_IMAGEM = "http://image.tmdb.org/t/p/w185";

    private long idSQLite;
    private String tituloOriginal;
    private String titulo;
    private String posterPath;
    private String resumo;
    private Double mediaVoto;
    private Date dataLancamento;

    public Filme(){

    }

    protected Filme(Parcel in) {
        tituloOriginal = in.readString();
        titulo = in.readString();
        posterPath = in.readString();
        resumo = in.readString();
        mediaVoto = in.readDouble();
        dataLancamento = (Date) in.readSerializable();
    }

    //Metodo para mapear uma String JSON em uma lista de Filmes.
    public ArrayList<Filme> parseJSON(String jsonStr)
            throws JSONException {

        ArrayList<Filme> lista = new ArrayList<>();

        if(jsonStr != null) {

            JSONObject json = new JSONObject(jsonStr);
            JSONArray jsonResultado = json.getJSONArray("results");

            for (int i = 0; i < jsonResultado.length(); i++) {
                Filme filme = new Filme();

                /* Pode acontecer em alguma situação do campo overview não vir preenchido ou não existir no JSON?
                Se sim, o ideal neste caso é utilizar o método optString() ao invés do getString(),
                pois, utilizando o getString(), caso o JSON não contenha a propriedade que você está tentando recuperar o valor,
                um erro será lançado e o funcionamento da sua aplicação pode ser afetado. */

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

    public long getIdSQLite() {
        return idSQLite;
    }

    public void setIdSQLite(long idSQLite) {
        this.idSQLite = idSQLite;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTituloOriginal() {
        return tituloOriginal;
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

    public static final Parcelable.Creator<Filme> CREATOR
            = new Parcelable.Creator<Filme>() {
        public Filme createFromParcel(Parcel in) {
            return new Filme(in);
        }

        public Filme[] newArray(int size) {
            return new Filme[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(tituloOriginal);
        dest.writeString(titulo);
        dest.writeString(posterPath);
        dest.writeString(resumo);
        dest.writeDouble(mediaVoto);
        dest.writeSerializable(dataLancamento);
    }
}
