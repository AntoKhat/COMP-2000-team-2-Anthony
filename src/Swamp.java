import java.awt.*;
import java.awt.image.BufferedImage;

public class Swamp extends Zone {
    private final int width;
    private final int height;
    private static final BufferedImage sprite = SpriteLoader.load("assets/swamp.png");

    Swamp(int spawnX, int spawnY, int width, int height) {
        x = spawnX;
        y = spawnY;
        this.width = width;
        this.height = height;
    }

    @Override
    public void update() {
    }

    @Override
    public boolean contains(int checkX, int checkY) {
        return checkX >= x && checkX <= x + width && checkY >= y && checkY <= y + height;
    }

    @Override
    public Shape getShape() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public void draw(Graphics2D drawing) {
        if (sprite != null) {
        drawing.drawImage(sprite, x, y, width, height, null);
        } else {

            drawing.setColor(new Color(70, 90, 50));
            drawing.fill(getShape());
        }
    }
}