import java.awt.*;
import java.awt.image.BufferedImage;

public class Cave extends Zone {

    int length = 150;
    int width = 150;
    private static final BufferedImage sprite = SpriteLoader.load("src/assets/cave.png");

    Cave(int spawnX, int spawnY) {
        x = spawnX;
        y = spawnY;
    }       

    @Override
    public void draw(Graphics2D drawing) { // draws the shapes of the cave. (need to add more detail)
        if (sprite != null) {
            drawing.drawImage(sprite, x, y, length, width, null);
        } else {

            drawing.setColor(new Color(100, 100, 100));
            drawing.fillRect(x, y, length, width);
        }
    }

    @Override
    public Shape getShape() {
        return new Rectangle(x, y, length, width);
    }

    @Override
    public void update() {
    } // Cave is not animated

    public boolean contains(int checkX, int checkY) { // checks to see if a cavemans co-ordinates are within caves
                                                      // co-ordinates

        if (checkX >= x && checkX <= x + length && checkY >= y && checkY <= y + width) {
            return true;
        } else {
            return false;
        }
    }

}
