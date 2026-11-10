import java.awt.*;
import java.awt.image.BufferedImage;

public class Plain extends Zone {
    private final int width;
    private final int height;
    private static final BufferedImage sprite = SpriteLoader.load("assets/plain.png");

    Plain(int spawnX, int spawnY, int width, int height) {
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
            drawing.setColor(new Color(190, 200, 120));
            drawing.fill(getShape());
        }
    }
}