package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed, bombPressed, statsPressed, firePressed;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {  // clicca il tasto
        int code = e.getKeyCode();  // ritorna l'intero KeyCode associato con il tasto cliccato
        if(code == KeyEvent.VK_W){  // se preme il tasto W (su)
            upPressed = true;
        }
        if(code == KeyEvent.VK_S){  // se preme il tasto S (giu)
            downPressed = true;
        }
        if(code == KeyEvent.VK_A){  // se preme il tasto A (sinistra)
            leftPressed = true;
        }
        if(code == KeyEvent.VK_D){  // se preme il tasto D (destra)
            rightPressed = true;
        }
        if(code == KeyEvent.VK_P){  // se preme il tast P (bomba)
            bombPressed = true;
        }

        if(code == KeyEvent.VK_L)  // da eliminare
            statsPressed = true;
        if(code == KeyEvent.VK_K)
            firePressed = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {  // rilascia il tasto
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_W){  // se preme il tasto W (su)
            upPressed = false;
        }
        if(code == KeyEvent.VK_S){  // se preme il tasto S (giu)
            downPressed = false;
        }
        if(code == KeyEvent.VK_A){  // se preme il tasto A (sinistra)
            leftPressed = false;
        }
        if(code == KeyEvent.VK_D){  // se preme il tasto D (destra)
            rightPressed = false;
        }
        if(code == KeyEvent.VK_P){  // se preme il tast P (bomba)
            bombPressed = false;
        }
    }
    
    
}
