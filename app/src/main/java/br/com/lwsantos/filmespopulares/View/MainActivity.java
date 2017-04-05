package br.com.lwsantos.filmespopulares.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.lwsantos.filmespopulares.Model.Movie;
import br.com.lwsantos.filmespopulares.R;

public class MainActivity extends AppCompatActivity implements MoviesFragment.Callback{

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Verifica se possui o framelayout de conteudo. Significa que o dispositivo é um tablet.
        // Está sendo utilizado o activity_main (w600dp)
        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            // No modo de dois paineis, os detalhes é exibido ou substituido no fragmento de detalhes,
            // utilizando a transação de fragmentos
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        MoviesFragment moviesFragment =  ((MoviesFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_movie_container));
        moviesFragment.mTwoPane = mTwoPane;
    }

    // Metodo a ser implementado da interface callback do MoviesFragment
    @Override
    public void onItemSelected(Movie filme) {
        Bundle args = new Bundle();
        args.putParcelable(Movie.PARCELABLE_KEY, filme);

        if (mTwoPane) {
            //Caso for em tablet (dois paineis) os detalhes será adicionado do lado direito através da transação de framento.
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent itDetail = new Intent(this, DetailActivity.class);
            itDetail.putExtra(Movie.PARCELABLE_KEY, args);
            startActivity(itDetail);
        }
    }
}
