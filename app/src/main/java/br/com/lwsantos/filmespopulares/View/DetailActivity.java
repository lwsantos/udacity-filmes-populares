package br.com.lwsantos.filmespopulares.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.lwsantos.filmespopulares.Model.Movie;
import br.com.lwsantos.filmespopulares.R;

public class DetailActivity extends AppCompatActivity {

    // Usando o ID de movie_detail_container, adicione um novo DetailFragment quando o DetailActivity iniciar.
    // Isso é unicamente para o caso de um painel, pois no caso de dois painéis não teremos um DetailActivity,
    // apenas uma MainActivity com um DetailFragment dentro dela.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {

            //Recupera os dados do filme para passar para o fragmento.
            Bundle arguments = new Bundle();
            arguments = getIntent().getBundleExtra(Movie.PARCELABLE_KEY);

            //Criar o DetailFragment e adicionar na atividade.
            //usando a transação do fragmento.
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }

    }
}
