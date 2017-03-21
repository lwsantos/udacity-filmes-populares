package br.com.lwsantos.filmespopulares.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.lwsantos.filmespopulares.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            //Criar o DetailFragment e adicionar na atividade.
            //usando a transação do fragmento.
            DetailFragment fragment = new DetailFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }

    }
}
