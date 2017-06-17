package br.com.lwsantos.filmespopulares.View;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import br.com.lwsantos.filmespopulares.Adapter.ReviewAdapter;
import br.com.lwsantos.filmespopulares.Adapter.VideoAdapter;
import br.com.lwsantos.filmespopulares.AsyncTask.ReviewAsync;
import br.com.lwsantos.filmespopulares.AsyncTask.VideoAsync;
import br.com.lwsantos.filmespopulares.Components.PosterImageView;
import br.com.lwsantos.filmespopulares.Control.MovieControl;
import br.com.lwsantos.filmespopulares.Control.Util;
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

    private LinearLayout mPosterContainer;
    private PosterImageView mImgPoster;
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
        mPosterContainer = (LinearLayout) rootView.findViewById(R.id.poster_container);
        mImgPoster = (PosterImageView) rootView.findViewById(R.id.imgPoster);
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
        Bundle arguments = getArguments();
        if (arguments != null) {
            mFilme = arguments.getParcelable(Movie.PARCELABLE_KEY);

            preencherFormulario();
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

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
            mTxtTitulo.setText(mFilme.getTitulo());
            SimpleDateFormat dtFormat = new SimpleDateFormat(getString(R.string.formato_data));
            mTxtDataLancamento.setText(dtFormat.format(mFilme.getDataLancamento()));
            mTxtMediaVoto.setText(String.format("%.2f", mFilme.getMediaVoto()));
            mTxtSinopse.setText(mFilme.getResumo());

            //Verifica se o filme está na base local (idSQLite > 0)
            if(mFilme.getFavorito())
            {
                mBtnStar.setImageResource(R.drawable.ic_star_black_36dp);
            }
            else
            {
                mBtnStar.setImageResource(R.drawable.ic_star_border_black_36dp);
            }

            Glide.with(getContext())
                    .load(Movie.URL_IMAGEM + mFilme.getPosterPath())
                    .placeholder(new ColorDrawable(getContext().getResources().getColor(R.color.accent_material_light)))
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .centerCrop()
                    .crossFade()
                    .into(mImgPoster);

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
        mFilme.setFavorito(!mFilme.getFavorito());
        new MovieControl(getContext()).addFavorite(mFilme);
        preencherFormulario();
    }

}
