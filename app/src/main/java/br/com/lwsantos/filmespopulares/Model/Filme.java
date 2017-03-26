package br.com.lwsantos.filmespopulares.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by lwsantos on 15/11/16.
 */

public class Filme implements Parcelable {

    public static final String PARCELABLE_KEY = "filme";
    public static final String URL_IMAGEM = "http://image.tmdb.org/t/p/w185";

    private long id;
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
        id = in.readLong();
        tituloOriginal = in.readString();
        titulo = in.readString();
        posterPath = in.readString();
        resumo = in.readString();
        mediaVoto = in.readDouble();
        dataLancamento = (Date) in.readSerializable();
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

    public static final Parcelable.Creator<Filme> CREATOR = new Parcelable.Creator<Filme>() {
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

        dest.writeLong(id);
        dest.writeString(tituloOriginal);
        dest.writeString(titulo);
        dest.writeString(posterPath);
        dest.writeString(resumo);
        dest.writeDouble(mediaVoto);
        dest.writeSerializable(dataLancamento);
    }
}
