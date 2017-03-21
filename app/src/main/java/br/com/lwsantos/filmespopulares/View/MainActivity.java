package br.com.lwsantos.filmespopulares.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import br.com.lwsantos.filmespopulares.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //carregarFilmes();
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
                //carregarFilmes();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
