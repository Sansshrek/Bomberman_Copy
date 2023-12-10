package objects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Fire extends SuperObject {
    private BufferedImage image;
    private int tileSize;
    private boolean destroyed;

    public Fire(GamePanel gp, int x, int y, int tileSize) {
        super(gp);
        this.x = x;
        this.y = y;
        this.tileSize = tileSize;
        this.destroyed = false;

        try {
            // Carica l'immagine delle fiamme
            image = ImageIO.read(getClass().getResourceAsStream("../res/fire.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        // Aggiorna lo stato delle fiamme se necessario
    }

    public void draw(Graphics2D g2, GamePanel gp) {
        if (!destroyed) {
            g2.drawImage(image, x, y, tileSize, tileSize, null);
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}