package br.com.lwsantos.filmespopulares.Holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import br.com.lwsantos.filmespopulares.R;

/**
 * Created by lwsantos on 02/04/17.
 */

public class MovieHolder {
    public final ImageView mImgPoster;

    public MovieHolder(View view, int widthScreen){
        mImgPoster = (ImageView) view.findViewById(R.id.imgPoster);

        // Configura o componente de imagem
        int widthImagem = widthScreen/2; // A lagura da imagem sera a metade da largura do GridView
        int heightImagem = (int)(widthImagem * 1.35); // A altura da imagem sera 1,35 vezes a largura

        //Define os parametros de layout da imagem.
        ViewGroup.LayoutParams params = mImgPoster.getLayoutParams();
        params.width = widthImagem;
        params.height = heightImagem;

        mImgPoster.setLayoutParams(params);
    }
}
