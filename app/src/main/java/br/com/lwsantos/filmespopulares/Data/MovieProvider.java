package br.com.lwsantos.filmespopulares.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by lwsantos on 21/03/17.
 */

public class MovieProvider extends ContentProvider {
    private MovieDbHelper mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Para facilitar a implementação, os provedores de conteúdo
    // geralmente vinculam cada tipo de URI a uma constante de tipo inteiro.
    // Com o UriMatcher podemos corresponder as URIs de entrada as constantes de inteiro do provedor de conteúdo.
    // Isso é importante porque precisamos ter uma maneira de saber qual o tipo de URI é passada ao provedor de conteúdo
    // para que possamos executar a operação solicitada.
    static final int MOVIE = 100;
    static final int MOVIE_WITH_TITLE = 101;


    //Consulta por Title
    //Movie.Titulo = ?
    private static final String sTitleSelection = MovieContract.TABLE_NAME + "." + MovieContract.COLUMN_TITULO + " = ? ";

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;

        // Primeiro utilizamos o URIMatcher para verificar qual é o tipo de URI recebido como parâmetro.
        // Com isso conseguimos saber qual o tipo de consulta devemos utilizar.
        switch (sUriMatcher.match(uri)) {
            // "movie/*"
            case MOVIE_WITH_TITLE:
            {
                String title = MovieContract.getTitleFromUri(uri);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TABLE_NAME,
                        projection,
                        sTitleSelection,
                        new String[]{title},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "movie"
            case MOVIE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Realizamos a configuração de notificação da URI.
        // Isso faz o cursor registrar um observador de conteúdo para ver mudanças que ocorram nesse URI e nos descendentes dele.
        // Com isso o provedor de conteúdo informa a IU quando o cursor muda, como por exemplo a atualização no banco de dados.
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    // O método getType definirá qual será o tipo de retorno do banco de dados. Se será um DIR (várias linhas) ou um ITEM (único registro).
    // Lembrando que dependendo da URI, um tipo é esperado, por isso a importância desse método.
    // As strings de retorno desse método estão definidos no contrato através das variáveis CONTENT_ITEM_TYPE (apenas um registro) e CONTENT_TYPE (mais de um registro).
    @Nullable
    @Override
    public String getType(Uri uri) {
        // Com a configuração do UriMatcher é possível comprar a URI passada no método
        // com as URIs parametrizadas no UriMatcher.
        // O retorno é uma constante inteira.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE_WITH_TITLE:
                // A URI de MOVIE_WITH_TITLE poderá retornar apenas um item
                return MovieContract.CONTENT_ITEM_TYPE;
            case MOVIE:
                // a URI MOVIE poderá retornar mais de um item.
                return MovieContract.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;

        long _id = db.insert(MovieContract.TABLE_NAME, null, values);
        if ( _id > 0 )
            returnUri = MovieContract.buildMovieUri(_id);
        else
            throw new android.database.SQLException("Failed to insert row into " + uri);

        // Quando inserimos no banco de dados queremos notificar a todos os observadores de conteúdo que pode haver dados modificados por nossa inserção.
        // Isso faz com que os próprios cursores se registrem como notificadores de descendentes.
        // Quer dizer, notificar a rota de URI também notificará todos os descendentes da URI,
        // aqueles contendo informações adicionais sobre o caminho.
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsDeleted;

        rowsDeleted = db.delete(MovieContract.TABLE_NAME, selection, selectionArgs);

        // Se ocorreu a exclusão, notificamos todos os obsrvadores de conteudo.
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsUpdated;

        rowsUpdated = db.update(MovieContract.TABLE_NAME, values, selection, selectionArgs);

        // Se ocorreu a atualização, notificamos todos os obsrvadores de conteudo.
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        Log.i("MOVIE", "Inicio do bulk insert");

        db.beginTransaction();

        try{
            for(ContentValues value : values){

                String selection = MovieContract.COLUMN_ID_API + "=?";
                String[] selectionArgs = new String[]{String.valueOf(value.getAsString(MovieContract.COLUMN_ID_API))};

                int rowsUpdated = db.update(MovieContract.TABLE_NAME, value, selection, selectionArgs);

                if(rowsUpdated == 0) {
                    long _id = db.insert(MovieContract.TABLE_NAME, null, value);
                }
            }

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }

        getContext().getContentResolver().notifyChange(uri, null);

        Log.i("MOVIE", "Fim do bulk insert");

        return 0;
    }

    // O objetivo da UriMatcher é associar as URIs a constantes do tipo inteiro
    // para que possa ser utilizada mais facilmente para identificar a
    // operação correta a ser realizada dependendo da URI.
    // O método deverá associar as URIs as seguintes constantes: MOVIE, MOVIE_WITH_TITLE
    static UriMatcher buildUriMatcher() {

        // O código passado do construtor UriMatcher é o retorno do nó raiz da URI.
        // É comum utilizar o NO_MATCH como código, quando o nó não corresponde a nada.
        // NO_MATCH é uma constante com valor de -1.
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // Associando a URI br.com.lwsantos.filmespopulares/movie
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE, MOVIE);

        // Associando a URI br.com.lwsantos.filmespopulares/movie/*
        // * receberá uma string que corresponderá a localização
        // Se o parametro a ser recebido fosse um número, a representação seria #
        //br.com.lwsantos.filmespopulares/movie/#
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE + "/*", MOVIE_WITH_TITLE);

        return uriMatcher;
    }
}
