package br.com.lwsantos.filmespopulares.View;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.lwsantos.filmespopulares.Adapter.MovieAdapter;
import br.com.lwsantos.filmespopulares.AsyncTask.TheMovieDBAsync;
import br.com.lwsantos.filmespopulares.Delegate.AsyncTaskDelegate;
import br.com.lwsantos.filmespopulares.Model.Filme;
import br.com.lwsantos.filmespopulares.R;

public class MoviesFragment extends Fragment implements AsyncTaskDelegate {

    private MovieAdapter mAdapter;
    private GridView mGrdFilme;
    private Spinner mSpnClassificacao;

    private static final String KEY_POSICAO_CLIQUE = "POSITION";
    private static final String KEY_CLASSIFICACAO = "CLASSIFICACAO";

    // Declarar a variavel como static permite que o valor consiga ser recuperado após voltar da tela de Detalhes
    private static int mPosicaoClique = -1;
    private static String mClassificacaoSaved = "";

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

        mAdapter = new MovieAdapter(getContext());

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
                mClassificacaoSaved = mSpnClassificacao.getSelectedItem().toString();
            }
        });

        mSpnClassificacao = (Spinner) rootView.findViewById(R.id.spnClassificacao);
        mSpnClassificacao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Não é necessário pegar o item selecionado, pois ao carregar filmes ele captura essa informação
                carregarFilmes();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Recupera a posição quando retornar na tela, após clicar em voltar da tela de detalhes.
        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(KEY_POSICAO_CLIQUE))
            {
                mPosicaoClique = savedInstanceState.getInt(KEY_POSICAO_CLIQUE);
            }

            if(savedInstanceState.containsKey(KEY_CLASSIFICACAO))
            {
                mClassificacaoSaved = savedInstanceState.getString(KEY_CLASSIFICACAO);
                // Define variavel que contém os array de valores do Spinner
                TypedArray spinnerValues = getResources().obtainTypedArray(R.array.pref_classificacao_values);
                for (int i=0; i < spinnerValues.length(); i++) {
                    if(spinnerValues.getString(i).toString().equals(mClassificacaoSaved))
                    {
                        mSpnClassificacao.setSelection(1);
                        break;
                    }
                }
            }
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Define variavel que contém os array de valores do Spinner
        TypedArray spinnerEntries = getResources().obtainTypedArray(R.array.pref_classificacao_entries);
        for (int i=0; i < spinnerEntries.length(); i++) {
            if(spinnerEntries.getString(i).toString().equals(mClassificacaoSaved))
            {
                mSpnClassificacao.setSelection(i);
                break;
            }
        }

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(mPosicaoClique != mGrdFilme.INVALID_POSITION) {
            outState.putInt(KEY_POSICAO_CLIQUE, mPosicaoClique);
        }

        outState.putString(KEY_CLASSIFICACAO, mSpnClassificacao.getSelectedItem().toString());
    }

    /**
     * Metodos da classe AsyncTaskDelegate
     */

    //Metodo executado quando o processamento da thread em segundo plano é concluido.
    @Override
    public void processFinish(ArrayList output) {
        if(output != null){
            //Recupero a lista retornada pelo asynctask
            ArrayList<Filme> lista = (ArrayList<Filme>) output;

            mAdapter.addAll(lista);

            //Verifica se há itens a serem exibidos.
            //Caso não exista, exibe mensagem ao usuário.
            if(lista.size() == 0){
                Toast.makeText(getContext(), getString(R.string.msg_nenhum_item), Toast.LENGTH_LONG).show();
            }
            else {
                if (mPosicaoClique != mGrdFilme.INVALID_POSITION) {
                    // Após carregar as informações, a tela realiza um scrool até a posição do clique.
                    mGrdFilme.smoothScrollToPosition(mPosicaoClique);
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

        if(verificarConexao()) {
            // Define variavel que contém os array de valores do Spinner
            TypedArray spinnerValues = getResources().obtainTypedArray(R.array.pref_classificacao_values);
            // De acordo com o posição do item selecionado, caputara o valor do array.
            String classificacao = spinnerValues.getString(mSpnClassificacao.getSelectedItemPosition());
            //Executa a thread em segundo plano para capturar a lista de filmes
            new TheMovieDBAsync(this, getContext()).execute(classificacao);
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
