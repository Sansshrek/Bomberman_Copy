package main;

import javax.swing.JFrame;

import entity.MouseBehaviour;

import java.awt.event.*;

public class JBomberman {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Super Bomberman");

        GamePanel gamePanel = new GamePanel();

        window.addMouseListener(new MouseListener() {
        public void mousePressed(MouseEvent me) { }
        public void mouseReleased(MouseEvent me) { }
        public void mouseEntered(MouseEvent me) { }
        public void mouseExited(MouseEvent me) { }
        public void mouseClicked(MouseEvent me) { 
			if (me.getButton() == MouseEvent.BUTTON1){
				int x = me.getX()-7;
				int y = me.getY()-30;
				gamePanel.player.updateMousePosition(x, y);
			} else if (me.getButton() == MouseEvent.BUTTON3 && gamePanel.player.movementBehaviour instanceof MouseBehaviour) {
				gamePanel.player.createBomb();
			} 
        }
        });

        window.add(gamePanel);

        window.pack(); // causa la finestra a impostarsi come i componenti (gamePanel) 

        window.setLocationRelativeTo(null); // non specifica la posizione della finestra (verra mostrata al centro dello schermo)
        window.setVisible(true);

        // gamePanel.setupGame();  // inizia il gioco
        gamePanel.startGameThread();
    }
}
