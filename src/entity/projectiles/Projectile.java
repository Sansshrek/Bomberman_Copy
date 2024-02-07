package entity.projectiles;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Projectile {
    Rectangle hitbox;
    int width, height, startX, startY, moveX, moveY;
    BufferedImage image;
    boolean extinguished = false;
    
    public void update(){} 
}
