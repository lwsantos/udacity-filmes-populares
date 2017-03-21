package br.com.lwsantos.filmespopulares.View;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import br.com.lwsantos.filmespopulares.Model.Filme;
import br.com.lwsantos.filmespopulares.R;

import static br.com.lwsantos.filmespopulares.R.id.txtDataLancamento;
import static br.com.lwsantos.filmespopulares.R.id.txtMediaVoto;
import static br.com.lwsantos.filmespopulares.R.id.txtSinopse;
import static br.com.lwsantos.filmespopulares.R.id.txtTitulo;

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
        mTxtTitulo = (TextView) rootView.findViewById(txtTitulo);
        mTxtDataLancamento = (TextView) rootView.findViewById(txtDataLancamento);
        mTxtSinopse = (TextView) rootView.findViewById(txtSinopse);
        mTxtMediaVoto = (TextView) rootView.findViewById(txtMediaVoto);

        //Recupera o filme selecionado na Activity Principal
        Intent itParent = getActivity().getIntent();
        if(itParent != null && itParent.hasExtra(Filme.PARCELABLE_KEY)){
            mFilme = (Filme) itParent.getParcelableExtra(Filme.PARCELABLE_KEY);
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
        }
    }

}
