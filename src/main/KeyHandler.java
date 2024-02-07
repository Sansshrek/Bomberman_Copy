package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    private static KeyHandler instance = null;

    private KeyHandler() {}

    public static KeyHandler getInstance() {
        if(instance == null){
            instance = new KeyHandler();}
        return instance;
    }

    public boolean upPressed, downPressed, leftPressed, rightPressed, bombPressed, pausePressed, nextLevelPressed;
    public boolean backspacePressed, qPressed, wPressed, ePressed, rPressed, tPressed, yPressed, uPressed, iPressed, oPressed, pPressed, aPressed, sPressed, dPressed, fPressed, gPressed, hPressed, jPressed, kPressed, lPressed, zPressed, xPressed, cPressed, vPressed, bPressed, nPressed, mPressed;
    public boolean resetPressed;
    
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
        if(code == KeyEvent.VK_N){
            nextLevelPressed = true;  // se preme il tasto N (prossimo livello)
        }
        if(code == KeyEvent.VK_ENTER){  // preme il tasto Enter (Pausa)
            pausePressed = true;
        }

        //INPUT NICKNAME-------------------------------------------        
        if(code == KeyEvent.VK_Q)
            qPressed = true;
        if(code == KeyEvent.VK_W)
            wPressed = true;
        if(code == KeyEvent.VK_E)
            ePressed = true;
        if(code == KeyEvent.VK_R)
            rPressed = true;
        if(code == KeyEvent.VK_T)
            tPressed = true;
        if(code == KeyEvent.VK_Y)
            yPressed = true;
        if(code == KeyEvent.VK_U)
            uPressed = true;
        if(code == KeyEvent.VK_I)
            iPressed = true;
        if(code == KeyEvent.VK_O)
            oPressed = true;
        if(code == KeyEvent.VK_P)
            pPressed = true;
        if(code == KeyEvent.VK_A)
            aPressed = true;
        if(code == KeyEvent.VK_S)
            sPressed = true;
        if(code == KeyEvent.VK_D)
            dPressed = true;
        if(code == KeyEvent.VK_F)
            fPressed = true;
        if(code == KeyEvent.VK_G)
            gPressed = true;
        if(code == KeyEvent.VK_H)
            hPressed = true;
        if(code == KeyEvent.VK_J)
            jPressed = true;
        if(code == KeyEvent.VK_K)
            kPressed = true;
        if(code == KeyEvent.VK_L)
            lPressed = true;
        if(code == KeyEvent.VK_Z)
            zPressed = true;
        if(code == KeyEvent.VK_X)
            xPressed = true;
        if(code == KeyEvent.VK_C)
            cPressed = true;
        if(code == KeyEvent.VK_V)
            vPressed = true;
        if(code == KeyEvent.VK_B)
            bPressed = true;
        if(code == KeyEvent.VK_N)
            nPressed = true;
        if(code == KeyEvent.VK_M)
            mPressed = true;
        if(code == KeyEvent.VK_BACK_SPACE)
            backspacePressed = true;
        //Tasti speciali
        if(code == KeyEvent.VK_R)  // reset livello
            resetPressed = true;
    }

    public String getLetter(){
        if(isKeyPressed()){
            if(qPressed){
                qPressed = false;
                return "q";
            }if(wPressed){
                wPressed = false;
                return "w";
            }if(ePressed){
                ePressed = false;
                return "e";
            }if(rPressed){
                rPressed = false;
                return "r";
            }if(tPressed){
                tPressed = false;
                return "t";
            }if(yPressed){
                yPressed = false;
                return "y";
            }if(uPressed){
                uPressed = false;
                return "u";
            }if(iPressed){
                iPressed = false;
                return "i";
            }if(oPressed){
                oPressed = false;
                return "o";
            }if(pPressed){
                pPressed = false;
                return "p";
            }if(aPressed){
                aPressed = false;
                return "a";
            }if(sPressed){
                sPressed = false;
                return "s";
            }if(dPressed){
                dPressed = false;
                return "d";
            }if(fPressed){
                fPressed = false;
                return "f";
            }if(gPressed){
                gPressed = false;
                return "g";
            }if(hPressed){ 
                hPressed = false;  
                return "h";
            }if(jPressed){
                jPressed = false;
                return "j";
            }if(kPressed){
                kPressed = false;
                return "k";
            }if(lPressed){
                lPressed = false;
                return "l";
            }if(zPressed){
                zPressed = false;
                return "z";
            }if(xPressed){ 
                xPressed = false;
                return "x";
            }if(cPressed){
                cPressed = false;
                return "c";
            }if(vPressed){
                vPressed = false;
                return "v";
            }if(bPressed){
                bPressed = false;
                return "b";
            }if(nPressed){
                nPressed = false;
                return "n";
            }if(mPressed){
                mPressed = false;
                return "m";
            }
        }
        aPressed = false;
        return "";
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
        if(code == KeyEvent.VK_N){
            nextLevelPressed = false;  // se preme il tasto N (prossimo livello)
        }
        if(code == KeyEvent.VK_ENTER)
            pausePressed = false;
        
        if(code == KeyEvent.VK_Q)
            qPressed = false;
        if(code == KeyEvent.VK_W)
            wPressed = false;
        if(code == KeyEvent.VK_E)
            ePressed = false;
        if(code == KeyEvent.VK_R)
            rPressed = false;
        if(code == KeyEvent.VK_T)
            tPressed = false;
        if(code == KeyEvent.VK_Y)
            yPressed = false;
        if(code == KeyEvent.VK_U)
            uPressed = false;
        if(code == KeyEvent.VK_I)
            iPressed = false;
        if(code == KeyEvent.VK_O)
            oPressed = false;
        if(code == KeyEvent.VK_P)
            pPressed = false;
        if(code == KeyEvent.VK_A)
            aPressed = false;
        if(code == KeyEvent.VK_S)
            sPressed = false;
        if(code == KeyEvent.VK_D)
            dPressed = false;
        if(code == KeyEvent.VK_F)
            fPressed = false;
        if(code == KeyEvent.VK_G)
            gPressed = false;
        if(code == KeyEvent.VK_H)
            hPressed = false;
        if(code == KeyEvent.VK_J)
            jPressed = false;
        if(code == KeyEvent.VK_K)
            kPressed = false;
        if(code == KeyEvent.VK_L)
            lPressed = false;
        if(code == KeyEvent.VK_Z)
            zPressed = false;
        if(code == KeyEvent.VK_X)
            xPressed = false;
        if(code == KeyEvent.VK_C)
            cPressed = false;
        if(code == KeyEvent.VK_V)
            vPressed = false;
        if(code == KeyEvent.VK_B)
            bPressed = false;
        if(code == KeyEvent.VK_N)
            nPressed = false;
        if(code == KeyEvent.VK_M)
            mPressed = false;
        if(code == KeyEvent.VK_BACK_SPACE)
            backspacePressed = false;
    }
    
    public boolean isKeyPressed(){
        return aPressed || bPressed || cPressed || dPressed || ePressed || fPressed || gPressed || hPressed || iPressed || jPressed || kPressed || lPressed || mPressed || nPressed || oPressed || pPressed || qPressed || rPressed || sPressed || tPressed || uPressed || vPressed || wPressed || xPressed || yPressed || zPressed;
    }

    
}
