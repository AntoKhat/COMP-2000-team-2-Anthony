import java.awt.*;
import java.awt.image.BufferedImage;


public class Grass {
    private static final BufferedImage sprite =
            SpriteLoader.load("assets/plant.png");
    public int x, y;
    public int size = 30;
    public int foodValue = 500;
    public boolean isEaten = false;
    public Grass(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics2D g2) {
        if (!isEaten) {
            if (sprite != null) {
                g2.drawImage(sprite, x, y, size, size, null);
            } else {
                g2.setColor(Color.YELLOW);
                g2.fillOval(x, y, size, size);
            }
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }
}