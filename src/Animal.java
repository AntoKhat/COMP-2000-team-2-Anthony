import java.awt.*;
import java.util.List;

abstract class Animal { // parent class for all animals
    float health;
    float maxHealth;
    float reproductionCooldown = 350;
    int damage;
    public int attackCooldown = 0;
    int hunger;
    int maxHunger = 600;
    int foodValue = 150;
    boolean isHungry = false;
    int starvationTimer = 600;
    int defaultStarvationTime = 600;
    boolean invulnerable = false;
    private boolean statsCaptured;
    int poisonTicks = 0;
    float speedMultiplier = 1f;

    float speed;
    int x; // current x position
    int y; // current y position
    int dx; // direction or speed for x
    int dy; // direction or speed for y

    int width = 40;
    int height = 40;

    public MovementStrategy getWanderStrategy() {
        return wanderStrategy;
    }

    public MovementStrategy getHuntStrategy() {
        return huntStrategy;
    }

    public void setWanderStrategy(MovementStrategy s) {
        this.wanderStrategy = s;
    }

    public void setHuntStrategy(MovementStrategy s) {
        this.huntStrategy = s;
    }

    protected AnimalState currentState;
    protected MovementStrategy wanderStrategy = new RandomWanderStrategy();
    protected MovementStrategy huntStrategy = new RandomWanderStrategy();

    public Animal() {
        this.currentState = new WanderingState();
    }

    public void resetEffects() { // calls once per tick, before zones run
        if (!statsCaptured) {
            maxHealth = health;
            statsCaptured = true; // full health at first tick = cap for healing
        }
        invulnerable = false; // zones re-apply it each tick while the animal is inside
        speedMultiplier = 1f;

        if (poisonTicks > 0) {
            takeDamage(0.02f);
            poisonTicks--;
        }
    }

    public void heal(float amount) {
        health = Math.min(maxHealth, health + amount);
    }

    public void setState(AnimalState newState) {
        this.currentState = newState;
    }

    public void kill() {
        if (invulnerable)
            return;
        health = 0;
        this.currentState = new DeadState();
    }

    public void takeDamage(float damage) {
        if (invulnerable)
            return;
        health -= damage;
        if (health <= 0) {
            this.currentState = new DeadState();
        }
    }

    public boolean isDead() {
        if (health <= 0 || starvationTimer <= 0) { // if hungry too long => die
            return true;
        } else {
            return false;
        }
    }

    public void setPoison(int ticks) {
        poisonTicks = Math.max(poisonTicks, ticks); // re-entering ash refreshes the timer, it doesn't stack
    }

    public void cleanse() {
        poisonTicks = 0;
    }

    public Rectangle getBounds() { // a collider
        return new Rectangle(x, y, width, height);
    }

    public double getDistanceTo(Animal other) {
        int deltaX = this.x - other.x;
        int deltaY = this.y - other.y;
        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
    }

    // updateCore to call State
    public void updateTimers() {
        // Hunger logic
        if (hunger > 0) {
            hunger = hunger - 1; // count down hunger
        } else {
            isHungry = true;
        }

        // if animal hungry, start starvation time
        if (isHungry) {
            starvationTimer = starvationTimer - 1;
        }

        if (attackCooldown > 0) {
            attackCooldown--;
        }

        if (reproductionCooldown > 0) {
            reproductionCooldown--;
        }
    }

    public void update(int screenWidth, int screenHeight, List<Animal> animals, List<Grass> grassList) {
        if (currentState != null) {
            currentState.update(this, screenWidth, screenHeight, animals, grassList);
        }
    }

    void draw(Graphics2D g2) {
        System.out.println("this is the abstract class method, downcast and use the subclasses methods");
    }

    public void drawStats(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.PLAIN, 10));

        // Show current health
        g2.setColor(Color.RED);
        String hpText = "HP: " + (int) health;
        g2.drawString(hpText, x, y - 25);

        // Show current hunger
        g2.setColor(Color.BLUE);
        String hungerText = "Hunger: " + hunger;
        g2.drawString(hungerText, x, y - 15);

        // Show current starvation
        if (isHungry == true) {
            g2.setColor(Color.ORANGE);
            int secondsLeft = starvationTimer / 60;
            String starvText = "Dying in: " + secondsLeft + "s (" + starvationTimer + "t)";
            g2.drawString(starvText, x, y - 5);
        }

        // create show status effects
    }
}