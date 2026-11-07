import java.awt.*;
import java.awt.image.BufferedImage;

public class Plant {
    private static final BufferedImage sprite =
            SpriteLoader.load("src/assets/plant.png");

    int x;
    int y;
    int width = 35;
    int height = 35;

    Plant(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g2) {
        if (sprite != null) {
            g2.drawImage(sprite, x, y, width, height, null);
        } else {
            g2.setColor(Color.YELLOW);
            g2.fillOval(x, y, width, height);
        }
    }
}