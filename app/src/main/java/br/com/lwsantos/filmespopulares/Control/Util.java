package br.com.lwsantos.filmespopulares.Control;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by lwsantos on 25/03/17.
 */

public class Util {

    public static boolean verificarConexao(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    //Redimensionar a altura do Height da ListView para a qtde de itens da lista.
    //Com isso não terá scroll
    public static void redimensionarAltura(Context context, ListView listView)
    {
        int height = 0;
        int qtdeItem = listView.getAdapter().getCount();

        for(int i=0; i < qtdeItem; i++) {
            View viewItem = listView.getAdapter().getView(i, null, listView);
            viewItem.measure(0,0);

            height += viewItem.getMeasuredHeight();
        }

        height += (listView.getDividerHeight() * qtdeItem);

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = height;

        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static String armazenarImagem(Bitmap imagem, String nomeArquivo) {

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Filmes Populares");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        // Create a media file name
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath(), nomeArquivo);

        try {

            if (mediaFile == null) {
                return "";
            }

            FileOutputStream fos = new FileOutputStream(mediaFile);
            imagem.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        }
        catch (Exception e)
        {
            Log.e("MOVIE", e.getMessage());
        }

        return mediaFile.getPath();
    }
}