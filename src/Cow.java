import java.awt.*;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.util.List;

public class Cow extends Animal implements AnimalBehaviours {
    private static float defaultHealth = 8f;
    private static float defaultSpeed = 2f;
    private static int defaultMaxHunger = 800;
    private static int defaultFoodValue = 200;
    private static int defaultDamage = 0;

    private static final BufferedImage sprite = SpriteLoader.load("src/assets/cow.png");
    private static Random rand = new Random();
    
    private int wanderTimer = 0;

    Cow() { // default spawn
    setStats();
    setStrategies();
    x = rand.nextInt(700);
    y = rand.nextInt(500);
    }

    Cow(int x, int y) {
        setStats();
        setStrategies();
        this.x = x;
        this.y = y;
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
    public void createNest(){}

    @Override
    public void setStrategies() {
        setWanderStrategy(new RandomWanderStrategy());
        setHuntStrategy(new GrazeStrategy());
    }
    

    @Override
    public void draw(Graphics2D g2) {
        if (sprite != null) {
            g2.drawImage(sprite, x, y, 50, 50, null);
        } else {
            g2.setColor(new Color(180, 130, 90));
            g2.fillRoundRect(x, y, 35, 30, 8, 8);
        }
        drawStats(g2);
    }
}