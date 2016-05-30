package com.games.dam.trench.MultiPlayer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.games.dam.trench.Casilla;
import com.games.dam.trench.HomeActivity;
import com.games.dam.trench.R;

public class MPMain extends Activity implements OnTouchListener {
    private Tablero fondo;
    private Casilla[][] casillas;
    private boolean activo = true;
    private int puntuacion1, puntuacion2;
    private boolean jugador = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //no se muestra el titulo
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // modo pantalla completa
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mpmain);
        //inicio de la actividad

        LinearLayout layout = (LinearLayout) findViewById(R.id.LLTabla);
        fondo = new Tablero(this); //Añade el tablero
        fondo.setOnTouchListener(this);
        layout.addView(fondo);
        casillas = new Casilla[8][8];
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                casillas[f][c] = new Casilla();
            }
        }
        this.disponerBombas(); //Llama al método para colocar bombas
        this.contarBombasPerimetro(); //Llama al método para contar las bombas circundantes
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        TextView puntuacion = (TextView) findViewById(R.id.puntuacion);
        if (activo)
            for (int f = 0; f < 8; f++) {
                for (int c = 0; c < 8; c++) {
                    if (!casillas[f][c].destapado) {
                        if (casillas[f][c].dentro((int) event.getX(),
                                (int) event.getY())) {
                            casillas[f][c].destapado = true;
                            if (casillas[f][c].contenido == 80) {
                                puntuacion(jugador);
                                Toast.makeText(this, "+1",
                                        Toast.LENGTH_SHORT).show();
                            } else if (casillas[f][c].contenido >= 0 && casillas[f][c].contenido <= 8) {
                                recorrer(f, c);
                                Toast.makeText(this, "Has fallado",
                                        Toast.LENGTH_SHORT).show();
                                turno();
                            }
                            fondo.invalidate();
                            if (jugador) {
                                puntuacion.setText(String.valueOf(puntuacion1));
                            } else {
                                puntuacion.setText(String.valueOf(puntuacion2));
                            }
                        }
                    }
                }
            }
        if (gano() && activo) {
            if (puntuacion1 > puntuacion2){
                endGame(true).show();
                activo = false;
            } else {
                endGame(false).show();
                activo = false;
            }
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
        int cantidad = 9;
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
        if (puntuacion1==5 || puntuacion2 ==5)
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

    private void puntuacion(boolean jugador){
        if (jugador){
            puntuacion1++;
        } else {
            puntuacion2++;
        }
    }

    private boolean turno(){
        TextView jugadores = (TextView) findViewById(R.id.jugador);
        if (jugador){
            jugador = false;
            jugadores.setText("Jugador 2");
        } else {
            jugador = true;
            jugadores.setText("Jugador 1");
        }
        return jugador;
    }

    public AlertDialog endGame(boolean winpayer1){
        String title;
        if(winpayer1){
            title = "Enhorabuena jugador 1";
        }else{
            title = "Enhorabuena jugador 2";
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setIcon(R.drawable.emoji_mp)
                .setTitle(title)
                .setMessage("¿Quereis seguir manteniendo el duelo?")
                .setNegativeButton("Volver al menú", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(MPMain.this, HomeActivity.class));
                    }
                })
                .setPositiveButton("Revancha", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        Intent i = new Intent().setClass(MPMain.this, MPMain.class);
                        startActivity(i);
                        finish();
                    }
                })
                .create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.argb(180,255,255,255)));
        return dialog;
    }
}
