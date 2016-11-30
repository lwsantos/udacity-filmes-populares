package br.com.lwsantos.filmespopulares.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.lwsantos.filmespopulares.Adapter.ImageAdapter;
import br.com.lwsantos.filmespopulares.AsyncTask.TheMovieDBAsync;
import br.com.lwsantos.filmespopulares.Delegate.AsyncTaskDelegate;
import br.com.lwsantos.filmespopulares.Model.Filme;
import br.com.lwsantos.filmespopulares.R;

public class MainActivity extends AppCompatActivity implements AsyncTaskDelegate {

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
                itDetail.putExtra(Filme.PARCELABLE_KEY, filme);
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
            case R.id.men_atualizar:
                carregarFilmes();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void processFinish(Object output) {
        if(output != null){
            //Recupero a lista retornada pelo asynctask
            ArrayList<Filme> lista = (ArrayList<Filme>) output;

            mAdapter.addAll(lista);
        }
        else{
            Toast.makeText(this, getString(R.string.msg_erro_conexao), Toast.LENGTH_LONG).show();
        }
    }

    private void carregarFilmes(){

        if(verificarConexao()) {
            //Captura as configuracao do aplicativo definido no SettingsActivity
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            //Captura a ordem de classificacao configurada no aplicativo
            String classificacao = sharedPreferences.getString(getString(R.string.pref_classificacao_key), getString(R.string.pref_classificacao_default));

            //Executa a thread em segundo plano para capturar a lista de filmes
            new TheMovieDBAsync(this).execute(classificacao);
        }
        else{
            Toast.makeText(this, getString(R.string.msg_erro_conexao), Toast.LENGTH_LONG).show();
        }
    }

    private boolean verificarConexao() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
