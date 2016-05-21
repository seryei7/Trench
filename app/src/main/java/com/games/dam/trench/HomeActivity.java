package com.games.dam.trench;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.games.dam.trench.Login.LoginWithEmail;
import com.games.dam.trench.MultiPlayer.MPMain;
import com.games.dam.trench.SoloPlayer.SPMain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends Activity {
    private FirebaseAuth user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.home_fragment);
    }

    public void salirApp(View view) {
        user.getCurrentUser();
        user.signOut();
        finish();
    }

    public void iniciarAjustes(View view) {
        //Intent intentAjustes = new Intent(this, );
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
