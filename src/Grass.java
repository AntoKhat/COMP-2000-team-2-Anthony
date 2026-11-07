import java.awt.*;

public class Grass {
    public int x, y;
    public int size = 12;
    public int foodValue = 500;
    public boolean isEaten = false;
    public Grass(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics2D g2) {
        if (!isEaten) {
            g2.setColor(new Color(50, 200, 50));
            g2.fillOval(x, y, size, size);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }
}