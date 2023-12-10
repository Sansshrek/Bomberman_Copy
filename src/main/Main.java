package main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Super Bomberman");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack(); // causa la finestra a impostarsi come i componenti (gamePanel) 

        window.setLocationRelativeTo(null); // non specifica la posizione della finestra (verra mostrata al centro dello schermo)
        window.setVisible(true);

        gamePanel.setupGame();
        gamePanel.startGameThread();
    }
}
