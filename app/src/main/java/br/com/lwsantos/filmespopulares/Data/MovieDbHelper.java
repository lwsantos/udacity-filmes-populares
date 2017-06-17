package br.com.lwsantos.filmespopulares.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lwsantos on 21/03/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;

    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.TABLE_NAME + " (" +
                MovieContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.COLUMN_ID_API + " INT, " +
                MovieContract.COLUMN_TITULO + " TEXT, " +
                MovieContract.COLUMN_SINOPSE + " TEXT, " +
                MovieContract.COLUMN_POSTER + " TEXT, " +
                MovieContract.COLUMN_DATA_LANCAMENTO + " DATE, " +
                MovieContract.COLUMN_MEDIA_VOTO + " DOUBLE, " +
                MovieContract.COLUMN_FAVORITO + " BOOLEAN, " +
                MovieContract.COLUMN_TOP_RATED + " BOOLEAN, " +
                MovieContract.COLUMN_POPULAR + " BOOLEAN " +
                " );";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.TABLE_NAME);
        onCreate(db);
    }
}
