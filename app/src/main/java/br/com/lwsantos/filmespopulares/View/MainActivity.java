package br.com.lwsantos.filmespopulares.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import br.com.lwsantos.filmespopulares.Adapter.ImageAdapter;
import br.com.lwsantos.filmespopulares.AsyncTask.TheMovieDBAsync;
import br.com.lwsantos.filmespopulares.Model.Filme;
import br.com.lwsantos.filmespopulares.R;

public class MainActivity extends AppCompatActivity {

    private ImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new ImageAdapter(this);

        GridView grdFilme = (GridView) findViewById(R.id.grdFilmes);
        grdFilme.setAdapter(mAdapter);

        //Ao clicar em um dos posters, eh exibido os detalhes do filme
        grdFilme.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent itDetail = new Intent(view.getContext(), DetailActivity.class);
                Filme filme = (Filme) mAdapter.getItem(position);
                itDetail.putExtra("Filme", filme);
                startActivity(itDetail);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarFilmes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId())
        {
            case R.id.men_settings:
                Intent it = new Intent(this, SettingsActivity.class);
                startActivity(it);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void carregarFilmes(){

        //Captura as configuracao do aplicativo definido no SettingsActivity
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //Captura a ordem de classificacao configurada no aplicativo
        String classificacao = sharedPreferences.getString(getString(R.string.pref_classificacao_key), getString(R.string.pref_classificacao_default));

        //Executa a thread em segundo plano para capturar a lista de filmes
        new TheMovieDBAsync().execute(mAdapter, classificacao);
    }
}
