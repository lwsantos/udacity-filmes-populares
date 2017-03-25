package br.com.lwsantos.filmespopulares.View;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import br.com.lwsantos.filmespopulares.Data.MovieContract;
import br.com.lwsantos.filmespopulares.Model.Filme;
import br.com.lwsantos.filmespopulares.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private Filme mFilme;

    ImageView mImgPoster;
    TextView mTxtTitulo;
    TextView mTxtDataLancamento;
    TextView mTxtSinopse;
    TextView mTxtMediaVoto;
    ImageButton mBtnStar;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        //Localizando os componentes de tela.
        mImgPoster = (ImageView)rootView.findViewById(R.id.imgPoster);
        mTxtTitulo = (TextView) rootView.findViewById(R.id.txtTitulo);
        mTxtDataLancamento = (TextView) rootView.findViewById(R.id.txtDataLancamento);
        mTxtSinopse = (TextView) rootView.findViewById(R.id.txtSinopse);
        mTxtMediaVoto = (TextView) rootView.findViewById(R.id.txtMediaVoto);
        mBtnStar = (ImageButton) rootView.findViewById(R.id.btnStar);

        mBtnStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFavorite();
            }
        });

        //Recupera o filme selecionado na Activity Principal
        Intent itParent = getActivity().getIntent();
        if(itParent != null && itParent.hasExtra(Filme.PARCELABLE_KEY)){
            mFilme = (Filme) itParent.getParcelableExtra(Filme.PARCELABLE_KEY);

            // Antes de Preencher o formulario, verifica se esta salvo na base de dados.
            // No metodo de preenchimento será verificado se idSQLite é maior que zero.
            // Se for, deixa o botão de favorito pressionado
            long idSQLite = consultarBaseLocal();
            mFilme.setIdSQLite(idSQLite);

            preencherFormulario();
        }

        return rootView;
    }

    /**
     * Metodos de apoio
     */

    private void preencherFormulario()
    {
        if(mFilme != null)
        {
            //Define o tamanho da imagem
            int widthScreen = this.getResources().getDisplayMetrics().widthPixels;
            int widthImagem = (int) (widthScreen * 0.4); // A imagem tera 40% da largura da tela
            int heightImagem = (int)(widthImagem * 1.35); // A altura tera 1,35 vezes a largura da imagem

            mImgPoster.setMinimumWidth(widthImagem);
            mImgPoster.setMinimumHeight(heightImagem);


            Picasso.with(getContext()).load(Filme.URL_IMAGEM + mFilme.getPosterPath()).resize(widthImagem, heightImagem).into(mImgPoster);
            mTxtTitulo.setText(mFilme.getTitulo());
            SimpleDateFormat dtFormat = new SimpleDateFormat(getString(R.string.formato_data));
            mTxtDataLancamento.setText(dtFormat.format(mFilme.getDataLancamento()));
            mTxtMediaVoto.setText(String.format("%.2f", mFilme.getMediaVoto()));
            mTxtSinopse.setText(mFilme.getResumo());

            if(mFilme.getIdSQLite() > 0)
            {
                mBtnStar.setImageResource(R.drawable.ic_star_black_36dp);
            }
            else {
                mBtnStar.setImageResource(R.drawable.ic_star_border_black_36dp);
            }
        }
    }

    private void addFavorite() {

        //Se o idSQLite for maior que zero significa que está salvo na base de dados.
        //Nesse caso, deleta o registro da base.
        if(mFilme.getIdSQLite() > 0) {
            getContext().getContentResolver().delete(
                    MovieContract.CONTENT_URI,
                    MovieContract._ID + "= ?",
                    new String[]{String.valueOf(mFilme.getIdSQLite())}
            );

            mFilme.setIdSQLite(0);
        }
        else
        {
            ContentValues movieValues = new ContentValues();

            final SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");

            // Define os colunas e os respectivos valores a serem inseridos.
            // O proprio Content Provider sabe o tipo do valor a ser inserido.
            movieValues.put(MovieContract.COLUMN_TITULO, mFilme.getTitulo());
            movieValues.put(MovieContract.COLUMN_DATA_LANCAMENTO, parser.format(mFilme.getDataLancamento()));
            movieValues.put(MovieContract.COLUMN_POSTER, mFilme.getPosterPath());
            movieValues.put(MovieContract.COLUMN_MEDIA_VOTO, mFilme.getMediaVoto());
            movieValues.put(MovieContract.COLUMN_SINOPSE, mFilme.getResumo());

            // Aciona o provider para inserir os valores, passando como parametro a URI:
            // content://br.com.lwsantos.filmespopulares/movie
            Uri insertedUri = getContext().getContentResolver().insert(MovieContract.CONTENT_URI, movieValues);

            // O resultado da URI contem o ID da linha inserida.
            mFilme.setIdSQLite(ContentUris.parseId(insertedUri));
        }

        preencherFormulario();
    }

    // Esse metodo irá consultar, atraves do titulo do filme, a base local para verificar se o mesmo já está inserido.
    private long consultarBaseLocal()
    {
        long movieId;

        // Realiza uma consulta para verificar se o filme já foi adicionado.
        // A consulta é realizada através do titulo
        Cursor movieCursor = getContext().getContentResolver().query(
                MovieContract.buildMovieTitle(mFilme.getTitulo()),
                new String[]{MovieContract._ID},
                null,
                null,
                null);

        //Se o filme existe, significa que o que queremos é desfavoritar.
        //Para isso excluimos da base de dados.
        if (movieCursor.moveToFirst()) {
            int movieIdIndex = movieCursor.getColumnIndex(MovieContract._ID);
            movieId = movieCursor.getLong(movieIdIndex);
        }
        else {
            movieId = 0;
        }

        movieCursor.close();

        return movieId;
    }

}
