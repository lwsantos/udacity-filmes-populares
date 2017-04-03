package br.com.lwsantos.filmespopulares.View;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import br.com.lwsantos.filmespopulares.Adapter.ReviewAdapter;
import br.com.lwsantos.filmespopulares.Adapter.VideoAdapter;
import br.com.lwsantos.filmespopulares.AsyncTask.ReviewAsync;
import br.com.lwsantos.filmespopulares.AsyncTask.VideoAsync;
import br.com.lwsantos.filmespopulares.Control.Util;
import br.com.lwsantos.filmespopulares.Data.MovieContract;
import br.com.lwsantos.filmespopulares.Delegate.AsyncTaskDelegate;
import br.com.lwsantos.filmespopulares.Model.Movie;
import br.com.lwsantos.filmespopulares.Model.Review;
import br.com.lwsantos.filmespopulares.Model.Video;
import br.com.lwsantos.filmespopulares.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements AsyncTaskDelegate {

    private Movie mFilme;
    private VideoAdapter mVideoAdapter;
    private ReviewAdapter mReviewAdapter;

    private ImageView mImgPoster;
    private TextView mTxtTitulo;
    private TextView mTxtDataLancamento;
    private TextView mTxtSinopse;
    private TextView mTxtMediaVoto;
    private ImageButton mBtnStar;
    private RecyclerView mListTrailer;
    private RecyclerView mListReview;

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

        mVideoAdapter = new VideoAdapter(getContext());
        mListTrailer = (RecyclerView) rootView.findViewById(R.id.lstTrailer);
        //Define a rolagem do RecyclerView como Horizontal
        mListTrailer.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mListTrailer.setAdapter(mVideoAdapter);

        mReviewAdapter = new ReviewAdapter(getContext());
        mListReview = (RecyclerView) rootView.findViewById(R.id.lstReview);
        mListReview.setLayoutManager(new LinearLayoutManager(getContext()));
        mListReview.setAdapter(mReviewAdapter);

        //Recupera o filme selecionado na Activity Principal
        Intent itParent = getActivity().getIntent();
        if(itParent != null && itParent.hasExtra(Movie.PARCELABLE_KEY)){
            mFilme = (Movie) itParent.getParcelableExtra(Movie.PARCELABLE_KEY);

            // Antes de Preencher o formulario, verifica se esta salvo na base de dados.
            // No metodo de preenchimento será verificado se idSQLite é maior que zero.
            // Se for, deixa o botão de favorito pressionado
            consultarBaseLocal();

            preencherFormulario();
        }

        return rootView;
    }

    /**
     * Metodos da classe AsyncTaskDelegate
     */

    //Metodo executado quando o processamento da thread asynctask em segundo plano é concluido.
    @Override
    public void processFinish(ArrayList output) {
        if(output != null){

            //Verifica se o array é do tipo Video
            if(output.size() > 0 && output.get(0).getClass() == Video.class)
            {
                ArrayList<Video> listaVideo = (ArrayList<Video>) output;
                mVideoAdapter.addAll(listaVideo);
            }
            //Verifica se o array é do tipo review
            else if(output.size() > 0 && output.get(0).getClass() == Review.class)
            {
                ArrayList<Review> listaReview = (ArrayList<Review>) output;
                mReviewAdapter.addAll(listaReview);
            }
        }
        else{
            Toast.makeText(getContext(), getString(R.string.msg_erro_conexao), Toast.LENGTH_LONG).show();
        }
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

            mTxtTitulo.setText(mFilme.getTitulo());
            SimpleDateFormat dtFormat = new SimpleDateFormat(getString(R.string.formato_data));
            mTxtDataLancamento.setText(dtFormat.format(mFilme.getDataLancamento()));
            mTxtMediaVoto.setText(String.format("%.2f", mFilme.getMediaVoto()));
            mTxtSinopse.setText(mFilme.getResumo());

            //Verifica se o filme está na base local (idSQLite > 0)
            if(mFilme.getIdSQLite() > 0)
            {
                mBtnStar.setImageResource(R.drawable.ic_star_black_36dp);
                Picasso.with(getContext()).load(new File(mFilme.getPosterLocalPath())).resize(widthImagem, heightImagem).into(mImgPoster);
            }
            else {
                mBtnStar.setImageResource(R.drawable.ic_star_border_black_36dp);
                Picasso.with(getContext()).load(Movie.URL_IMAGEM + mFilme.getPosterPath()).resize(widthImagem, heightImagem).into(mImgPoster);
            }

            //Verifica se há conexão com a internet.
            //Se sim, monta a lista de trailers
            if(Util.verificarConexao(getContext()))
            {
                //Executa a thread em segundo plano para capturar a lista de trailers
                new VideoAsync(this).execute(mFilme.getId());

                //Executa a thread em segundo plano para capturar a lista de reviews
                new ReviewAsync(this).execute(mFilme.getId());
            }
            else{
                Toast.makeText(getContext(), getString(R.string.msg_erro_conexao), Toast.LENGTH_LONG).show();
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
            //Capturar Bitmap do ImageView do poster do filme
            Bitmap bitmap = ((BitmapDrawable)mImgPoster.getDrawable()).getBitmap();
            //Define o nome do arquivo com ID.png
            String nomeArquivo = mFilme.getId() + ".png";
            //Armazena a imagem no dispositivo
            String posterPathLocal = Util.armazenarImagem(bitmap, nomeArquivo);
            mFilme.setPosterLocalPath(posterPathLocal);

            // Define os colunas e os respectivos valores a serem inseridos.
            // O proprio Content Provider sabe o tipo do valor a ser inserido.
            movieValues.put(MovieContract.COLUMN_ID_API, mFilme.getId());
            movieValues.put(MovieContract.COLUMN_TITULO, mFilme.getTitulo());
            movieValues.put(MovieContract.COLUMN_DATA_LANCAMENTO, mFilme.getDataLancamento().getTime());
            movieValues.put(MovieContract.COLUMN_POSTER, mFilme.getPosterLocalPath());
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
    private void consultarBaseLocal()
    {
        // Realiza uma consulta para verificar se o filme já foi adicionado.
        // A consulta é realizada através do titulo
        Cursor movieCursor = getContext().getContentResolver().query(
                MovieContract.buildMovieTitle(mFilme.getTitulo()),
                MovieContract.MOVIE_PROJECTION,
                null,
                null,
                null);

        //Atualiza os dados do filme com o ID do SQLite e o local do poster
        if (movieCursor.moveToFirst()) {
            mFilme.setIdSQLite(movieCursor.getLong(MovieContract.INDEX_ID));
            mFilme.setPosterLocalPath(movieCursor.getString(MovieContract.INDEX_POSTER));
        }

        movieCursor.close();
    }

}
