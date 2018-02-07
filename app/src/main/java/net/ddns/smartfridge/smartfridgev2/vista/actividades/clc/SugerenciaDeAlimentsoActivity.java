package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.ddns.smartfridge.smartfridgev2.R;

import java.util.ArrayList;

import cn.refactor.library.SmoothCheckBox;

public class SugerenciaDeAlimentsoActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> alimentosSugeridos;
    private ArrayList<String> nuevaLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugerencia_de_alimentso);

        nuevaLista = new ArrayList<String>();

        listView = findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(
                this,
                R.layout.fila_alimentos_sugeridos,
                R.id.textViewAlimentoSugerido,
                alimentosSugeridos
        );

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);

        cargarListeners();
    }

    private void cargarListeners(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                nuevaLista.add((String)listView.getItemAtPosition(position));
                SmoothCheckBox scb = (SmoothCheckBox)view.findViewById(R.id.smoothCheckBox);
                if(scb.isChecked()){
                    scb.setChecked(false);
                    nuevaLista.remove((String)listView.getItemAtPosition(position));
                } else {
                    scb.setChecked(true);
                    nuevaLista.add((String)listView.getItemAtPosition(position));
                }
            }
        });
    }
}
