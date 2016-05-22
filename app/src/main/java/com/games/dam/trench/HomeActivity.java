package com.games.dam.trench;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.games.dam.trench.Login.LoginActivity;
import com.games.dam.trench.MultiPlayer.MPMain;
import com.games.dam.trench.SoloPlayer.SPMain;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends Activity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.home_fragment);

        mAuth = FirebaseAuth.getInstance();
    }

    public void salirApp(View view) {
        finishAffinity();
    }

    public void iniciarAjustes(View view) {
        mAuth.signOut();
        Toast.makeText(getApplicationContext(),"Cerrando sesion...", Toast.LENGTH_SHORT).show();
        Intent i = new Intent().setClass(HomeActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    public void iniciarDosJugadores(View view) {
        Intent intentDosJugadores = new Intent(this, MPMain.class);
        startActivity(intentDosJugadores);
    }

    public void iniciarUnJugador(View view) {
        Intent intentUnJugador = new Intent(this, SPMain.class);
        startActivity(intentUnJugador);
    }
}
