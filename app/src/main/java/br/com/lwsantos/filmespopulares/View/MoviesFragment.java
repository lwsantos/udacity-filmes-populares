package br.com.lwsantos.filmespopulares.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.lwsantos.filmespopulares.Adapter.ImageAdapter;
import br.com.lwsantos.filmespopulares.AsyncTask.TheMovieDBAsync;
import br.com.lwsantos.filmespopulares.Delegate.AsyncTaskDelegate;
import br.com.lwsantos.filmespopulares.Model.Filme;
import br.com.lwsantos.filmespopulares.R;

public class MoviesFragment extends Fragment implements AsyncTaskDelegate {

    private ImageAdapter mAdapter;

    private GridView mGrdFilme;
    private static final String KEY_POSICAO_CLIQUE = "POSITION";

    // Declarar a variavel como static permite que o valor consiga ser recuperado após voltar da tela de Detalhes
    private static int mPosicaoClique = -1;

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        mAdapter = new ImageAdapter(getContext());

        mGrdFilme = (GridView) rootView.findViewById(R.id.grdFilmes);
        mGrdFilme.setAdapter(mAdapter);

        //Ao clicar em um dos posters, eh exibido os detalhes do filme
        mGrdFilme.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent itDetail = new Intent(getActivity(), DetailActivity.class);
                Filme filme = (Filme) mAdapter.getItem(position);
                itDetail.putExtra(Filme.PARCELABLE_KEY, filme);
                startActivity(itDetail);

                //Grava a posição do clique para ser utilizado quando retornar
                mPosicaoClique = position;
            }
        });

        //Recupera a posição quando retornar na tela, após clicar em voltar da tela de detalhes.
        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(KEY_POSICAO_CLIQUE))
            {
                mPosicaoClique = savedInstanceState.getInt(KEY_POSICAO_CLIQUE);
            }
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        carregarFilmes();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(mPosicaoClique != mGrdFilme.INVALID_POSITION) {
            outState.putInt(KEY_POSICAO_CLIQUE, mPosicaoClique);
        }
    }

    /**
     * Metodos da classe AsyncTaskDelegate
     */

    @Override
    public void processFinish(Object output) {
        if(output != null){
            //Recupero a lista retornada pelo asynctask
            ArrayList<Filme> lista = (ArrayList<Filme>) output;

            mAdapter.addAll(lista);

            if(mPosicaoClique != mGrdFilme.INVALID_POSITION) {
                // Após carregar as informações, a tela realiza um scrool até a posição do clique.
                mGrdFilme.smoothScrollToPosition(mPosicaoClique);
            }
        }
        else{
            Toast.makeText(getContext(), getString(R.string.msg_erro_conexao), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Metodos de apoio
     */

    private void carregarFilmes(){

        if(verificarConexao()) {
            //Captura as configuracao do aplicativo definido no SettingsActivity
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            //Captura a ordem de classificacao configurada no aplicativo
            String classificacao = sharedPreferences.getString(getString(R.string.pref_classificacao_key), getString(R.string.pref_classificacao_default));

            //Executa a thread em segundo plano para capturar a lista de filmes
            new TheMovieDBAsync(this).execute(classificacao);
        }
        else{
            //Se não há	conexão disponível, exibe a mensagem
            View view = getView();

            Snackbar snackbar = Snackbar.make(view, getString(R.string.msg_erro_conexao), Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(getString(R.string.acao_atualizar), new View.OnClickListener() {
                //Ao clicar na snackbar, uma nova tentativa de atualizar a lista é efetuada :-)
                @Override
                public void onClick(View view) {
                    carregarFilmes();
                }
            });
            snackbar.show();
        }
    }

    private boolean verificarConexao() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
