import java.awt.*;

abstract class ZoneDecorator extends Zone {
    protected final Zone inner;

    ZoneDecorator(Zone inner) {
        this.inner = inner;
        this.x = inner.x; // copy position so code reading zone.x isn't given 0
        this.y = inner.y;
    }

    @Override public void draw(Graphics2D drawing) { inner.draw(drawing); }
    @Override public void update() { inner.update(); }
    @Override public boolean contains(int checkX, int checkY) { return inner.contains(checkX, checkY); }
    @Override public Shape getShape() { return inner.getShape(); }
    @Override public void applyEffect(Animal a) { inner.applyEffect(a); }
    @Override public void onGrass(Grass g) { inner.onGrass(g); }
    @Override public boolean suppressesGrass() { return inner.suppressesGrass(); }
}