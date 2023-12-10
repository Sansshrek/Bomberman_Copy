package objects;

import javax.imageio.ImageIO;

import main.GamePanel;

public class PowerUp extends SuperObject{
    public PowerUp(GamePanel gp, int x, int y, String powerUp){
        super(gp);
        name = powerUp;
        this.x = x;
        this.y = y;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("../res/powerup/"+powerUp+".png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
