package br.com.lwsantos.filmespopulares.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import br.com.lwsantos.filmespopulares.Model.Filme;
import br.com.lwsantos.filmespopulares.R;

public class DetailActivity extends AppCompatActivity {

    private Filme filme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Recupera o filme selecionado na Activity Principal
        Intent itParent = this.getIntent();
        if(itParent != null && itParent.hasExtra("Filme")){
            filme = (Filme) itParent.getSerializableExtra("Filme");
            preencherFormulario();
        }
    }

    private void preencherFormulario()
    {
        if(filme != null)
        {
            //Define o tamanho da imagem
            int widthScreen = this.getResources().getDisplayMetrics().widthPixels;
            int widthImagem = (int) (widthScreen * 0.4); // A imagem tera 40% da largura da tela
            int heightImagem = (int)(widthImagem * 1.35); // A altura tera 1,35 vezes a largura da imagem

            ImageView imgPoster = (ImageView)findViewById(R.id.imgPoster);
            imgPoster.setMinimumWidth(widthImagem);
            imgPoster.setMinimumHeight(heightImagem);
            TextView txtTitulo = (TextView) findViewById(R.id.txtTitulo);
            TextView txtDataLancamento = (TextView) findViewById(R.id.txtDataLancamento);
            TextView txtSinopse = (TextView) findViewById(R.id.txtSinopse);
            TextView txtMediaVoto = (TextView) findViewById(R.id.txtMediaVoto);

            Picasso.with(this).load(Filme.URL_IMAGEM + filme.getPosterPath()).resize(widthImagem, heightImagem).into(imgPoster);
            txtTitulo.setText(filme.getTitulo());
            SimpleDateFormat dtFormat = new SimpleDateFormat("MMM/yyyy");
            txtDataLancamento.setText(dtFormat.format(filme.getDataLancamento()));
            txtMediaVoto.setText(String.format("%.2f", filme.getMediaVoto()));
            txtSinopse.setText(filme.getResumo());
        }
    }
}
