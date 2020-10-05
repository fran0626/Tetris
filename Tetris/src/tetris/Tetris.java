/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

//@author Francesca Bonilla Mariño

public class Tetris extends JPanel {

   private final Color[] colores= {//Se asignan los posibles colores
       Color.WHITE,
       Color.BLUE,
       Color.GREEN,
       Color.YELLOW,
       Color.RED,
       Color.PINK,
       Color.CYAN,      
};
    private final Point [][][] puntos= {// Cada cuadro estará dado por la pieza, la rotación y el punto en el que se encuentra 
        // I
        {
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1)},
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3)},
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1)},
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3)}
            },
        // S
        {
            { new Point(0, 2), new Point(1, 2), new Point(1, 1), new Point(2, 1)},
            { new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(2, 2)},
            { new Point(0, 3), new Point(1, 3), new Point(1, 2), new Point(2, 2)},
            { new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2)}
            },
        // Z
        {
            { new Point(0, 1), new Point(1, 1), new Point(1, 2), new Point(2, 2)},
            { new Point(0, 2), new Point(0, 1), new Point(1, 1), new Point(1, 0)},
            { new Point(0, 2), new Point(1, 2), new Point(1, 3), new Point(2, 3)},
            { new Point(1, 2), new Point(1, 1), new Point(2, 1), new Point(2, 0)}
            },
        // T
        {
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 0)},
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 1)},
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2)},
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 1)}
            },
        // L 
        {
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0)},
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2)},
            { new Point(0, 1), new Point(1, 0), new Point(2, 0), new Point(0, 0)},
            { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(1, 2)}
            },
        // J
        {
            { new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1)},
            { new Point(1, 2), new Point(1, 1), new Point(1, 0), new Point(2, 0)},
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2)},
            { new Point(0, 2), new Point(1, 2), new Point(1, 1), new Point(1, 0)}
            },
        // O 
        {
            { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)},
            { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)},
            { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)},
            { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)}
            }
        };
    
    private int a= 20;
    private int b= 26;//Se asigna el tamaño del tablero.
    public static boolean fin= false; 
    private Point pt;
    private int pieza;
    private int rotacion;
    private ArrayList<Integer> siguientesPiezas= new ArrayList<Integer>();
    private long puntaje;
    private Color [][] tablero;
    
    private void init() {//Tendremos un tablero de cuadros negros y bordes grises
        tablero= new Color[a][b];
        for (int i = 0; i < a; i++) {
            for (int j = 0; j < b-1; j++) {
                if(i== 0 || i== a-1 || j== b-2) {
                    tablero[i][j]= Color.DARK_GRAY;
                } else {
                    tablero[i][j]= Color.BLACK;
                }
            }
        }
        nuevapieza();
    }
    
    public void nuevapieza() {//Permite que caiga la siguiente ficha
        fin();
        if(!fin) {
            pt= new Point(8,1);//Punto de partida para que caigan las fichas
            rotacion= 0;
            if(siguientesPiezas.isEmpty()) {
                Collections.addAll(siguientesPiezas, 0,1,2,3,4,5,6);//Reúne las posiciones inciales de las fichas
                Collections.shuffle(siguientesPiezas);//Elige una ficha cualquiera
            }
            pieza = siguientesPiezas.get(0);
            siguientesPiezas.remove(0);//Elimina la ficha que cayó para que no caiga siempre
        }
    }
    
    private void fin() {//Revisa si las fichas llegaron al límite para dar fin al juego
        for(int i= 1; i< 15;  i++) {
            if (tablero[i][2]!= Color.BLACK) {
                fin= true;
            }
        }
    }
    
    private boolean dospiezas(int x, int y, int rotacion) {//Revisa si es posible girar las fichas
        for (Point p: puntos[pieza][rotacion]) {
            if (tablero[p.x+x][p.y+y]!= Color.BLACK) {
                return true;
            }
        }
        return false;
    }
    
    private void rotar(int r) {//Permite la rotación de las fichas
        int nuevarotacion= (rotacion+ r)%4;
        if (nuevarotacion <0) {
            nuevarotacion= 3;
        }
        if(!dospiezas(pt.x, pt.y, nuevarotacion)) {
            rotacion= nuevarotacion;
        }
        repaint();//Dibuja la ficha con la rotación
    }
    
    public void mover(int mov) {//Mueve las fichas de lado a lado
        if(!dospiezas(pt.x + mov, pt.y, rotacion)){
            pt.x+= mov;
        }
        repaint();//Dibuja la ficha en su nueva posición
    }
    
    public void caida(){//Permite que las fichas caigan veticalmente
        if(!dospiezas(pt.x, pt.y + 1, rotacion)){
            pt.y+= 1;
        } else {
            ajuste();
        }
        repaint();//Dibuja la ficha en su nueva posición
    }
    
    public void ajuste() {//Cambia la posición de las fichas dependiendo las filas que se borren
        for (Point p : puntos[pieza][rotacion]) {
            tablero[pt.x + p.x][pt.y + p.y] = colores[pieza];
        }
        matarFilas();
        nuevapieza();
    }
    
 public void borrarFila(int fila) {//Elimina la fila completa
        for (int i = fila-1; i > 0; i--) {
            for (int j = 1; j < 11; j++) {
                tablero[j][i+1]= tablero[j][i];
            }
        }
    }
    
    public void matarFilas() {//Baja las filas pintándolas del color de la que está arriba
        boolean espacio;
        int filas = 0;
        for (int i = b-3; i > 0; i--) {
            espacio= false;
            for (int j = 1; j < a-1; j++) {
                if(tablero[j][i]== Color.BLACK) {
                    espacio= true;
                    break;
                }
            }
            if (!espacio) {
                borrarFila(i);
                i+=1;
                filas+=1;
            }
        }
        switch (filas) {//Variación del puntaje según las filas eliminadas
            case 1:
                puntaje+=100;
                break;
            case 2:
                puntaje+=250;
                break;
            case 3:
                puntaje+=400;
                break;
            case 4:
                puntaje+=550;
            break;
        }
    }
    
    private void dibujodePieza(Graphics rtx) {//Asigna el color a cada ficha
       rtx.setColor(colores[pieza]);
       for (Point p: puntos[pieza][rotacion]) {
           rtx.fillRect((pt.x + p.x)*26, (pt.y + p.y)*26, 25, 25);
       }
    }
    
    @Override
    public void paintComponent(Graphics rtx) {//Permite dar color al tablero
    rtx.fillRect(0, 0, 26*a, 26*(b-1));
    for (int i = 0; i < a; i++) {
        for (int j = 0; j < b-1; j++) {
            rtx.setColor(tablero[i][j]);
            rtx.fillRect(26*i, 26*j, 25, 25);
        }
    }
    rtx.setColor(Color.WHITE);
    rtx.drawString("PUNTAJE: " + puntaje, 19*12, 25);
    dibujodePieza(rtx);
    }
    
    private void comenzar () {//Permite la caída de la ficha en determinado tiempo
        new Thread() {
            @Override
            public void run() {
                while (!fin) {
                    try {
                        Thread.sleep(650);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Tetris.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    caida();
                }
            }
        }.start();
    };
    
    public static void main(String[] args) {//Configuración del tablero
        final Tetris t= new Tetris();
        t.init(); 
        JFrame frame= new JFrame("TETRIS");
        frame.setDefaultCloseOperation(3);
        frame.setSize(26* t.a +10, 26*(t.b-1)+25);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.add(t);
        frame.addKeyListener(new KeyListener() {//Da una función a cada tecla para el juego
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP://Rotación hacia la izquierda
                        t.rotar(-1);
                        break;
                    case KeyEvent.VK_DOWN://Rotación hacia la derecha
                        t.rotar(1);
                        break;
                    case KeyEvent.VK_LEFT://La ficha se mueve hacia la izquierda
                        t.mover(-1);
                        break;
                    case KeyEvent.VK_RIGHT://La ficha se mueve hacia la derecha
                        t.mover(1);
                        break;
                    case KeyEvent.VK_SPACE://La ficha aumenta la velocidad de caída
                        t.caida();
                        if (!fin) {
                           t.puntaje += 2;//Si aumenta la velocidad, aumenta el puntaje
                        }
                        break;
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        t.comenzar();
    }
}