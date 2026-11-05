import java.awt.*;

public class Volcano extends Zone {

    // set the x value for where i want the zone to begin
    int r;
    int lavaRadius;
    boolean errupting = false;
    int tickCount = 0;

    // tick rate for timing on when the volcano errupts

    Volcano(int spawnX, int spawnY, int radius, int lavaRadius) {
        x = spawnX;
        y = spawnY;
        r = radius;
        this.lavaRadius = lavaRadius;

    }

    @Override
    public Shape getShape() {
        return new java.awt.geom.Ellipse2D.Double(x - lavaRadius, (y - r) - lavaRadius, lavaRadius * 2, lavaRadius * 2);
    }

    @Override
    public void update() { // increases the size of the lava while its errupting
        tickCount++;
        if (tickCount >= 1500) { // tick count is set to ~10 seconds change accordingly
            errupting = true;
        }
        if (errupting && lavaRadius < 500) {

            lavaRadius += 10;
        }
    }

    @Override
    public boolean contains(int checkX, int checkY) { // checks if an animal is within the lava radius
        if (!errupting) {
            return false;
        }
        double distance = Math.sqrt(((checkX - x) * (checkX - x)) + ((checkY - (y - r)) * (checkY - (y - r))));
        return distance <= lavaRadius;

    }

    @Override
    public void draw(Graphics2D drawing) { // draws the shape of the volcano and lava
        // volcano base
        drawing.setColor(new Color(90, 70, 60));
        int[] volcX = { x, x - r, x + r };
        int[] volcY = { y - r, y + r, y + r };
        drawing.fillPolygon(volcX, volcY, 3);

        // lava
        drawing.setColor(new Color(100, 10, 10));
        int lavaX = x - lavaRadius;
        int lavaY = (y - r) - lavaRadius;
        ;
        drawing.fillOval(lavaX, lavaY, lavaRadius * 2, lavaRadius * 2);

    }

}
