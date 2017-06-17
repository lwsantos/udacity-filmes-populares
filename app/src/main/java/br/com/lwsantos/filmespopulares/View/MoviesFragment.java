package br.com.lwsantos.filmespopulares.View;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.lwsantos.filmespopulares.Adapter.MovieAdapter;
import br.com.lwsantos.filmespopulares.AsyncTask.MovieLocalAsync;
import br.com.lwsantos.filmespopulares.AsyncTask.TheMovieDBAsync;
import br.com.lwsantos.filmespopulares.Data.MovieContract;
import br.com.lwsantos.filmespopulares.Delegate.AsyncTaskDelegate;
import br.com.lwsantos.filmespopulares.Interface.ItemClickListener;
import br.com.lwsantos.filmespopulares.Model.Movie;
import br.com.lwsantos.filmespopulares.R;

public class MoviesFragment extends Fragment implements AsyncTaskDelegate {

    private MovieAdapter mAdapter;
    private RecyclerView mGrdFilme;

    private static final String KEY_POSICAO_CLIQUE = "POSITION";
    private static final String KEY_CLASSIFICACAO = "CLASSIFICACAO";

    // Variavel que a activity informa se há dois paineis.
    // Se existir dois paineis significa que os detalhes estão sendo exibidos a direita.
    public Boolean mTwoPane = false;

    // Declarar a variavel como static permite que o valor consiga ser recuperado após voltar da tela de Detalhes
    private static int mPosicaoClique = -1;
    private static String mClassificacaoSaved = "";

    /**
     * Na interface de Callback, toda atividade que contem este fragmento deverá implementar.
     * Nesse app, a ActicityMain deverá implementar para notificar o item selecionado.
     * O item selecionado é capturado no evento mGrdFilme.setOnItemClickListener desta classe.
     */
    public interface Callback {
        public void onItemSelected(Movie filme);
    }

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Sinaliza que o fragment possui um menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        mAdapter = new MovieAdapter(getContext(), new ItemClickListener() {
            @Override
            public void onClick(int position) {
                //Passa para o metodo implementado da activity o filme selecioando
                Movie filme = (Movie) mAdapter.getItem(position);
                ((Callback) getActivity()).onItemSelected(filme);

                //Grava a posição do clique para ser utilizado quando retornar
                mPosicaoClique = position;
            }
        });

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        mGrdFilme = (RecyclerView) rootView.findViewById(R.id.grdFilmes);
        mGrdFilme.setLayoutManager(layoutManager);
        mGrdFilme.setAdapter(mAdapter);

        //Recupera a posição quando retornar na tela, após clicar em voltar da tela de detalhes.
        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(KEY_POSICAO_CLIQUE))
            {
                mPosicaoClique = savedInstanceState.getInt(KEY_POSICAO_CLIQUE);
            }

            if(savedInstanceState.containsKey(KEY_CLASSIFICACAO))
            {
                mClassificacaoSaved = savedInstanceState.getString(KEY_CLASSIFICACAO);
            }
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(mClassificacaoSaved == "")
            mClassificacaoSaved = MovieContract.CLASSIFICACAO_POPULARIDADE;

        carregarFilmes();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId())
        {
            case R.id.men_atualizar:
                carregarFilmes();
                Toast.makeText(getContext(), getString(R.string.msg_atualizado), Toast.LENGTH_LONG).show();
                return true;
            case R.id.men_por_popularidade:
                mClassificacaoSaved = MovieContract.CLASSIFICACAO_POPULARIDADE;
                carregarFilmes();
                return true;
            case R.id.men_por_avaliacao:
                mClassificacaoSaved = MovieContract.CLASSIFICACAO_AVALIACAO;
                carregarFilmes();
                return true;
            case R.id.men_favoritos:
                mClassificacaoSaved = MovieContract.CLASSIFICACAO_FAVORITO;
                carregarFilmes();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(mPosicaoClique != -1) {
            outState.putInt(KEY_POSICAO_CLIQUE, mPosicaoClique);
        }

        outState.putString(KEY_CLASSIFICACAO, mClassificacaoSaved);
    }

    /**
     * Metodos da classe AsyncTaskDelegate
     */

    //Metodo executado quando o processamento da thread em segundo plano é concluido.
    @Override
    public void processFinish(ArrayList output) {
        if(output != null){
            //Recupero a lista retornada pelo asynctask
            ArrayList<Movie> lista = (ArrayList<Movie>) output;

            mAdapter.addAll(lista);

            //Verifica se há itens a serem exibidos.
            if(lista.size() > 0){

                if (mPosicaoClique != -1) {
                    // Após carregar as informações, a tela realiza um scrool até a posição do clique.
                    mGrdFilme.smoothScrollToPosition(mPosicaoClique);
                }
                else
                {
                    mGrdFilme.smoothScrollToPosition(0);
                }

                if (mTwoPane && mPosicaoClique == -1)
                {
                    //Se a Activity tiver dois paineis (tablet) e nenhum item estiver selecionado,
                    //seleciona o primeiro.
                    Movie filme = (Movie) mAdapter.getItem(0);
                    ((Callback) getActivity()).onItemSelected(filme);
                }
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

        new MovieLocalAsync(this, getContext(), mClassificacaoSaved).execute();

        if(verificarConexao() && mClassificacaoSaved != MovieContract.CLASSIFICACAO_FAVORITO) {
            new TheMovieDBAsync(this, getContext(), mClassificacaoSaved).execute();
        }
    }

    private boolean verificarConexao() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
