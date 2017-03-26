package br.com.lwsantos.filmespopulares.Control;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
    public static void redimensionarAltura(ListView listView)
    {
        int height = 0;
        int qtdeItem = listView.getAdapter().getCount();

        View viewItem = listView.getAdapter().getView(0, null, listView);
        viewItem.measure(0, 0);

        height = (viewItem.getMeasuredHeight() * qtdeItem) + (listView.getDividerHeight() * qtdeItem);

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = height;
        listView.setLayoutParams(params);
    }
}
