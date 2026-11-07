import java.util.List;

@FunctionalInterface
public interface MovementStrategy {
    void chooseDirection(Animal animal, List<Animal> animals, List<Grass> grassList);

    default void move(Animal animal, List<Animal> animals, List<Grass> grassList, int screenWidth, int screenHeight) {
        chooseDirection(animal, animals, grassList);
        applyMovement(animal, screenWidth, screenHeight);
    }

    static void applyMovement(Animal animal, int screenWidth, int screenHeight) {
        animal.x += scaled(animal.dx, animal.speedMultiplier);
        animal.y += scaled(animal.dy, animal.speedMultiplier);

        if (animal.x <= 0) {
            animal.x = 0;
            animal.dx = -animal.dx;
        } else if (animal.x >= screenWidth - animal.width) {
            animal.x = screenWidth - animal.width;
            animal.dx = -animal.dx;
        }

        if (animal.y <= 0) {
            animal.y = 0;
            animal.dy = -animal.dy;
        } else if (animal.y >= screenHeight - animal.height) {
            animal.y = screenHeight - animal.height;
            animal.dy = -animal.dy;
        }
    }

    static int scaled(int d, float multiplier) {
        return (int) Math.signum(d) * Math.round(Math.abs(d) * multiplier);
    }

    // Point dx / dy at a target (was Animal.moveToward)
    static void moveToward(Animal animal, int targetX, int targetY) {
        int speed = (int) animal.speed;

        if (targetX > animal.x)
            animal.dx = speed;
        else if (targetX < animal.x)
            animal.dx = -speed;
        else
            animal.dx = 0;

        if (targetY > animal.y)
            animal.dy = speed;
        else if (targetY < animal.y)
            animal.dy = -speed;
        else
            animal.dy = 0;
    }
}