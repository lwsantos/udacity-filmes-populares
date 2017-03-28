package br.com.lwsantos.filmespopulares.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by lwsantos on 21/03/17.
 */

public class MovieContract implements BaseColumns {

    /**** CONFIGURAÇÃO DA TABELA ****/
    // Nome da Tabela
    public static final String TABLE_NAME = "Movie";

    //Definição das colunas
    public static final String COLUMN_ID_API = "id_api";
    public static final String COLUMN_TITULO = "titulo";
    public static final String COLUMN_DATA_LANCAMENTO = "dataLancamento";
    public static final String COLUMN_SINOPSE = "sinopse";
    public static final String COLUMN_MEDIA_VOTO = "mediaVoto";
    public static final String COLUMN_POSTER = "poster";

    //Projeção das colunas a serem utilizadas nas consultas.
    //A projeção auxlia para não precisar repetir as colunas e indices nas consultas das telas
    public static final String[] MOVIE_PROJECTION = new String[] {
            _ID,
            COLUMN_ID_API,
            COLUMN_TITULO,
            COLUMN_DATA_LANCAMENTO,
            COLUMN_SINOPSE,
            COLUMN_MEDIA_VOTO,
            COLUMN_POSTER
    };

    //Indices da projeção
    public static final int INDEX_ID = 0;
    public static final int INDEX_ID_API = 1;
    public static final int INDEX_TITULO = 2;
    public static final int INDEX_DATA_LANCAMENTO = 3;
    public static final int INDEX_SINOPSE = 4;
    public static final int INDEX_MEDIA_VOTO = 5;
    public static final int INDEX_POSTER = 6;
    /*******************************/



    /**** CONFIGURAÇÃO DAS URIs ****/

    // O authority é o nome de entrada do content provider, semelhante a um dominio na internet.
    //  Geralmente a string utilizada é o nome do pacote do app.
    public static final String CONTENT_AUTHORITY = "br.com.lwsantos.filmespopulares";

    // Caminho da localização. Geralmente é o nome da tabela.
    public static final String PATH_MOVIE = "movie";

    // URI base da aplicação - content://br.com.lwsantos.filmespopulares
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Endereço URI para a tabela Movie - content://br.com.lwsantos.filmespopulares/movie
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

    //Dependendo do retorno do Cursor de um Content Provider, pode ser uma lista ou um único item. Para isso configuramos as seguintes variáveis.
    //Retorno de mais de um item (DIR)
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
    //Retorno de apenas um item (ITEM)
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

    // Metodo para montar a URI padrão da tabela, seguido de seu ID.
    // Geralmente usado após uma inserção de registro
    public static Uri buildMovieUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    // Metodo para criar a URI de pesquisa por titulo
    // content://br.com.lwsantos.filmespopulares/movie/[title]
    // scheme://authority/location/query
    public static Uri buildMovieTitle(String title) {
        // Usa o método buildUpon() para construir uma nova URI copiando os atributos da URI CONTENT_URI.
        // Após a construção da nova URI adiciona o titulo do filme a ser consultada, através do método appendPath().
        //Por ultimo constrói a URI, através do build().
        return CONTENT_URI.buildUpon().appendPath(title).build();
    }

    //Extrai e Retorna o valor do titulo a ser pesquisado
    public static String getTitleFromUri(Uri uri) {
        return uri.getPathSegments().get(1);
    }

    /*******************************/
}
