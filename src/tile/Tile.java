package tile;

import java.awt.image.BufferedImage;
import java.awt.Rectangle;

public class Tile {
    public BufferedImage image;
    public boolean collision = false;
    public int width;
    public int height;

    public Tile(BufferedImage img, int size, boolean collision){
        this.image = img;
        this.width = size;
        this.height = size;
        this.collision = collision;
    }

    public void setImage(BufferedImage img, int scale){
        this.image = img;
        this.width = img.getWidth() * scale;
        this.height = img.getHeight() * scale;
    }
}
