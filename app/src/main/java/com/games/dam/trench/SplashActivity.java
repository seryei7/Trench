package com.games.dam.trench;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.games.dam.trench.Login.LoginActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {

    //duración de la pantalla de inicio
    private static final long SPLASH_SCREEN_DELAY = 3000;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //ocultar barra de título
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_fragment);

        TimerTask task = new TimerTask(){
            public void run(){
                Intent mainIntent = new Intent().setClass(
                        SplashActivity.this, LoginActivity.class);
                startActivity(mainIntent);
                finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }
}
