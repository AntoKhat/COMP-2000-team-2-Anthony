import java.awt.*;
import java.awt.geom.Ellipse2D;

public class WaterHole extends Zone {
    private final int radius;

    WaterHole(int centreX, int centreY, int radius) {
        this.x = centreX;
        this.y = centreY;
        this.radius = radius;
    }

    @Override
    public void update() { }

    @Override
    public boolean contains(int checkX, int checkY) {
        double dx = checkX - x;
        double dy = checkY - y;
        return Math.sqrt(dx * dx + dy * dy) <= radius;
    }

    @Override
    public Shape getShape() {
        return new Ellipse2D.Double(x - radius, y - radius, radius * 2, radius * 2);
    }

    @Override
    public void draw(Graphics2D drawing) {
        drawing.setColor(new Color(60, 120, 220));
        drawing.fill(getShape());
    }
}