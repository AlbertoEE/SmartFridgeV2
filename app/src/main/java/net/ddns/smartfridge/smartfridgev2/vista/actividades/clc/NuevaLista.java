package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.animation.Animator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import net.ddns.smartfridge.smartfridgev2.R;

public class NuevaLista extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_lista);
        //Cogemos la referencia al floating action button
        FloatingActionsMenu fab = (FloatingActionsMenu) findViewById(R.id.fab);
        //Le asignamos el listener
        fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Mostramos el menú con las dos opciones
                }
            });
        }
    }

