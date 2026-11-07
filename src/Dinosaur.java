import java.awt.*;
import java.awt.image.BufferedImage;

public class Dinosaur extends Animal implements AnimalBehaviours {

    //Dinosaur stats can change later for balancing
    private static float defaultHealth = 16f;
    private static int defaultSpeed = 2;
    private static int defaultDamage = 2;
    private static int defaultMaxHunger = 800;
    private static int defaultFoodValue = 400;

private static final BufferedImage sprite =
        SpriteLoader.load("src/assets/dinosaur.png");

        Dinosaur () {
            setStats();
            setStrategies();
            x = 0;
            y = 0;
        }

        //constructor for reproduced dinosaurs
        Dinosaur(int spawnX, int spawnY) {
            setStats();
            setStrategies();
            x = spawnX;
            y = spawnY;
            width = 100;
            height = 60;
        }

    @Override
    public void setStats(){
        this.health = defaultHealth;
        this.speed = defaultSpeed;
        this.hunger = defaultMaxHunger;
        this.maxHunger = defaultMaxHunger;
        this.foodValue = defaultFoodValue;
        this.damage = defaultDamage;
    }

    @Override
    public void createNest(){
    }

    @Override
    public void setStrategies() {
        setWanderStrategy(new RandomWanderStrategy());
        setHuntStrategy(new HuntStrategy(a -> a instanceof Caveman));
    }

    @Override
    public void draw(Graphics2D g2) {
        if (sprite != null) {
            g2.drawImage(sprite, x, y, 120, 120, null);
        } else {
            g2.setColor(new Color(60, 150, 60));
            g2.fillOval(x, y, 50, 30);
        }
        drawStats(g2);
    }
}
