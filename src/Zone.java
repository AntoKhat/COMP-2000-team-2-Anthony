import java.awt.*;

abstract class Zone {

    int x;
    int y;

    
    
    public abstract void draw(Graphics2D drawing);
    public abstract void update();
    public abstract boolean contains(int checkX, int checkY); // abstract class to downcast to volcano and safe zone
    public abstract Shape getShape();


    // Defaults: a zone does nothing unless a subclass or decorator overrides these
    public void applyEffect(Animal a) { }
    public void onGrass(Grass g) { }
    public boolean suppressesGrass() { return false; }
}
