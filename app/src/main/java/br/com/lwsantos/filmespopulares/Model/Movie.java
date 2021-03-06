package br.com.lwsantos.filmespopulares.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by lwsantos on 15/11/16.
 */

public class Movie implements Parcelable {

    public static final String PARCELABLE_KEY = "filme";
    public static final String URL_IMAGEM = "http://image.tmdb.org/t/p/w185";

    private long id;
    private long idSQLite;
    private String tituloOriginal;
    private String titulo;
    private String posterPath;
    private String posterLocalPath;
    private String resumo;
    private Double mediaVoto;
    private Date dataLancamento;
    private Boolean favorito;

    public Movie(){

    }

    protected Movie(Parcel in) {
        id = in.readLong();
        tituloOriginal = in.readString();
        titulo = in.readString();
        posterPath = in.readString();
        resumo = in.readString();
        mediaVoto = in.readDouble();
        dataLancamento = (Date) in.readSerializable();
        favorito = (in.readInt() > 0 ? true : false);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getPosterLocalPath() {
        return posterLocalPath;
    }

    public void setPosterLocalPath(String posterLocalPath) {
        this.posterLocalPath = posterLocalPath;
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

    public Boolean getFavorito() {
        return favorito;
    }

    public void setFavorito(Boolean favorito) {
        this.favorito = favorito;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(id);
        dest.writeString(tituloOriginal);
        dest.writeString(titulo);
        dest.writeString(posterPath);
        dest.writeString(resumo);
        dest.writeDouble(mediaVoto);
        dest.writeSerializable(dataLancamento);
        dest.writeInt(favorito ? 1 : 0);
    }
}
