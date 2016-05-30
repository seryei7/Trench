package com.games.dam.trench.SoloPlayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.games.dam.trench.HomeActivity;
import com.games.dam.trench.R;
import com.games.dam.trench.Casilla;

public class SPMain extends Activity implements OnTouchListener {
    private Tablero fondo;
    private Chronometer crono;
    private long timestop = 0;
    private ImageView emoji;
    private String mark;
    private Casilla[][] casillas;
    private boolean activo = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spmain);

        crono = (Chronometer)findViewById(R.id.crono);
        emoji = (ImageView)findViewById(R.id.button);

        LinearLayout layout = (LinearLayout) findViewById(R.id.LLTabla);
        fondo = new Tablero(this);
        fondo.setOnTouchListener(this);
        layout.addView(fondo);
        casillas = new Casilla[8][8];
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                casillas[f][c] = new Casilla();
            }
        }
        this.disponerBombas();
        this.contarBombasPerimetro();
    }
    @Override
    public void onPause(){
        super.onPause();
        crono.stop();
        timestop = crono.getBase() - SystemClock.elapsedRealtime();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(!activo){
            crono.stop();
        }else{
            crono.setBase(SystemClock.elapsedRealtime() + timestop);
            crono.start();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (activo)
            for (int f = 0; f < 8; f++) {
                for (int c = 0; c < 8; c++) {
                    if (casillas[f][c].dentro((int) event.getX(),
                            (int) event.getY())) {
                        casillas[f][c].destapado = true;
                        if (casillas[f][c].contenido == 80) {
                            crono.stop();
                            destaparBombas(c,f);
                            emoji.setImageResource(R.drawable.emoji_lost);
                            endGame(false).show();
                            activo=false;
                        } else if (casillas[f][c].contenido == 0)
                            recorrer(f, c);
                        fondo.invalidate();
                    }
                }
            }
        if (gano() && activo) {
            crono.stop();
            mark = crono.getText().toString();
            endGame(true).show();
            emoji.setImageResource(R.drawable.emoji_win);
            activo = false;
        }

        return true;
    }

    class Tablero extends View {

        public Tablero(Context context) {
            super(context);
        }

        protected void onDraw(Canvas canvas) {

            int ancho;
            if (canvas.getWidth() < canvas.getHeight())
                ancho = fondo.getWidth();
            else
                ancho = fondo.getHeight();
            int anchocua = ancho / 8;
            Paint paint = new Paint();
            paint.setTextSize(20);
            Paint paint2 = new Paint(); //Numeros
            paint2.setTextSize((int)(anchocua/3.5));
            paint2.setTypeface(Typeface.DEFAULT_BOLD);
            Paint paintlinea1 = new Paint();
            paintlinea1.setARGB(255, 0, 0, 0);
            int filaact = 0;
            for (int f = 0; f < 8; f++) {
                for (int c = 0; c < 8; c++) {
                    casillas[f][c].fijarxy(c * anchocua, filaact, anchocua);
                    if (casillas[f][c].destapado == false)
                        paint.setARGB(150, 0, 139, 255);
                    else
                        paint.setARGB(200, 255, 255, 255);
                    canvas.drawRect(c * anchocua, filaact, c * anchocua
                            + anchocua - 2, filaact + anchocua - 2, paint);
                    // linea blanca
                    canvas.drawLine(c * anchocua, filaact, c * anchocua
                            + anchocua, filaact, paintlinea1);
                    canvas.drawLine(c * anchocua + anchocua - 1, filaact, c
                                    * anchocua + anchocua - 1, filaact + anchocua,
                            paintlinea1);

                    if (casillas[f][c].contenido >= 1
                            && casillas[f][c].contenido <= 8
                            && casillas[f][c].destapado){
                        switch (casillas[f][c].contenido){
                            case 1:
                                paint2.setARGB(255,0,153,61);
                                break;
                            case 2:
                                paint2.setARGB(255,20,0,174);
                                break;
                            case 3:
                                paint2.setARGB(255,192,3,9);
                                break;
                            case 4:
                                paint2.setARGB(255,124,0,200);
                                break;
                            case 5:
                                paint2.setARGB(255,124,0,200);
                                break;
                            case 6:
                                paint2.setARGB(255,124,0,200);
                                break;
                            case 7:
                                paint2.setARGB(255,124,0,200);
                                break;
                            case 8:
                                paint2.setARGB(255,124,0,200);
                                break;
                        }
                        canvas.drawText(
                                String.valueOf(casillas[f][c].contenido), c
                                        * anchocua + (anchocua / 2) - 8,
                                filaact + anchocua / 2, paint2);
                    }


                    if (casillas[f][c].contenido == 80
                            && casillas[f][c].destapado) {
                        Drawable d = ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_clear_black_24dp);
                        d.setBounds(c * anchocua, filaact, c * anchocua
                                + anchocua - 2, filaact + anchocua - 2);
                        d.draw(canvas);
                    }

                }
                filaact = filaact + anchocua;
            }
        }
    }

    private void disponerBombas() {
        int cantidad = 8;
        do {
            int fila = (int) (Math.random() * 8);
            int columna = (int) (Math.random() * 8);
            if (casillas[fila][columna].contenido == 0) {
                casillas[fila][columna].contenido = 80;
                cantidad--;
            }
        } while (cantidad != 0);
    }

    private boolean gano() {
        int cant = 0;
        for (int f = 0; f < 8; f++)
            for (int c = 0; c < 8; c++)
                if (casillas[f][c].destapado)
                    cant++;
        if (cant == 56)
            return true;
        else
            return false;
    }

    private void contarBombasPerimetro() {
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                if (casillas[f][c].contenido == 0) {
                    int cant = contarCoordenada(f, c);
                    casillas[f][c].contenido = cant;
                }
            }
        }
    }

    int contarCoordenada(int fila, int columna) {
        int total = 0;
        if (fila - 1 >= 0 && columna - 1 >= 0) {
            if (casillas[fila - 1][columna - 1].contenido == 80)
                total++;
        }
        if (fila - 1 >= 0) {
            if (casillas[fila - 1][columna].contenido == 80)
                total++;
        }
        if (fila - 1 >= 0 && columna + 1 < 8) {
            if (casillas[fila - 1][columna + 1].contenido == 80)
                total++;
        }

        if (columna + 1 < 8) {
            if (casillas[fila][columna + 1].contenido == 80)
                total++;
        }
        if (fila + 1 < 8 && columna + 1 < 8) {
            if (casillas[fila + 1][columna + 1].contenido == 80)
                total++;
        }

        if (fila + 1 < 8) {
            if (casillas[fila + 1][columna].contenido == 80)
                total++;
        }
        if (fila + 1 < 8 && columna - 1 >= 0) {
            if (casillas[fila + 1][columna - 1].contenido == 80)
                total++;
        }
        if (columna - 1 >= 0) {
            if (casillas[fila][columna - 1].contenido == 80)
                total++;
        }
        return total;
    }

    private void recorrer(int fil, int col) {
        if (fil >= 0 && fil < 8 && col >= 0 && col < 8) {
            if (casillas[fil][col].contenido == 0) {
                casillas[fil][col].destapado = true;
                casillas[fil][col].contenido = 50;
                recorrer(fil, col + 1);
                recorrer(fil, col - 1);
                recorrer(fil + 1, col);
                recorrer(fil - 1, col);
                recorrer(fil - 1, col - 1);
                recorrer(fil - 1, col + 1);
                recorrer(fil + 1, col + 1);
                recorrer(fil + 1, col - 1);
            } else if (casillas[fil][col].contenido >= 1
                    && casillas[fil][col].contenido <= 8) {
                casillas[fil][col].destapado = true;
            }
        }
    }

    public AlertDialog endGame(boolean win){
        String title, message;
        int emoji;
        if(win){
            title = "Enhorabuena";
            message = "¿Quieres mejorar tu marca? La marca de ahora está en: "+mark;
            emoji = R.drawable.emoji_win;
        }else{
            title = "No pasa nada...";
            message = "¿Quieres volver a intentarlo?";
            emoji = R.drawable.emoji_lost;
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setIcon(emoji)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton("Volver al menú", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(SPMain.this, HomeActivity.class));
                    }
                })
                .setPositiveButton("Reintentar", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        Intent i = new Intent().setClass(SPMain.this, SPMain.class);
                        startActivity(i);
                        finish();
                    }
                })
                .create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.argb(180,255,255,255)));
        return dialog;
    }

    public void destaparBombas(int c, int f){
        for (f = 0; f < 8; f++) {
            for (c = 0; c < 8; c++) {
                if (casillas[f][c].contenido == 80) {
                    casillas[f][c].destapado = true;
                }
            }
        }
    }
}