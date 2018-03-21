package net.ddns.smartfridge.smartfridgev2.vista.actividades.sr;

import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.vista.fragmentos.TabAlimento;
import net.ddns.smartfridge.smartfridgev2.vista.fragmentos.TabOtros;
import net.ddns.smartfridge.smartfridgev2.vista.fragmentos.TabTipo;

/**
 * Activity para mostrar las pestañas con los tres tipos de filtros disponibles para las recetas
 */
public class FiltroRecetaActivity extends AppCompatActivity {
    private FragmentTabHost tabHost;//Para poner las pestañas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro_receta);
        //Ponemos los listener a los tabs
        tabHost= (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this,getSupportFragmentManager(),android.R.id.tabcontent);
        //Añadimos 3 pesatañas
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Alimentos"),
                TabAlimento.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Tipo"),
                TabTipo.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Otros"),
                TabOtros.class, null);
    }
}
