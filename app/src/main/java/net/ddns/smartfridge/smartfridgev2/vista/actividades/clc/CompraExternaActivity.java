//Activity para mostrar las opciones de alimentos de la bbdd externa
package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ComponenteListaCompra;

import java.util.ArrayList;

public class CompraExternaActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ANTERIOR = 5465;//Código de respuesta para el activity que lo llamó
    private static final int REQUEST_CODE_SIGUIENTE = 468;//Código de respuesta para el activity que lo llamó
    private static final String[] categorias = {"Verduras", "Carne", "Fruta", "Pescado", "Bebida", "Embutido", "Frutos secos", "Desayuno"};//Array con los nombres
    //de las categorías de alimentos para coger el seleccionado por el usuario
    private String seleccion;//Seleccion hecha por el usuario
    private Intent intent;//Para pasar información entre activitys
    private ArrayList<ComponenteListaCompra> alimentosExternos;//Para almacenar el resultado de la seleccion del usuario y pasárselo al intent que nos llamó

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra_externa);
        alimentosExternos = new ArrayList<ComponenteListaCompra>();
    }

    //Programamos el método que va a haber en cada botón. Programamos el de las verduras
    public void abrirVerduras(View v){
        seleccion = categorias[0];
        intent = new Intent(this, CategoriaActivity.class);
        //Le asignamos la categoría
        intent.putExtra("Categoria", seleccion);
        startActivityForResult(intent, REQUEST_CODE_SIGUIENTE);
    }

    //Programamos el onActivityResult para recoger el arraylist con los datos que ha seleccionado el usuario
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Comrpobamos el intent que viene de vuelta
        if (requestCode == REQUEST_CODE_SIGUIENTE) {
            // Vemos que el resultado esté correcto
            if (resultCode == RESULT_OK) {
                //Recogemos los datos del intent y se los asignamos al ArrayList
                alimentosExternos = (ArrayList<ComponenteListaCompra>) data.getSerializableExtra("");
                intent = getIntent();
                intent.putExtra("Categorias", alimentosExternos);
                //Devolvemos el ArrayList con el request_code del intent
                setResult(REQUEST_CODE_ANTERIOR, intent);
            }
        }
    }
}
