import java.util.List;
import java.util.Random;

public class RandomWanderStrategy implements MovementStrategy {
    private static final Random rand = new Random();

    @Override
    public void chooseDirection(Animal animal, List<Animal> animals, List<Grass> grassList) {
        if (animal.dx == 0 && animal.dy == 0) {
            animal.dx = (Math.random() < 0.5 ? -1 : 1) * (int) animal.speed;
            animal.dy = (Math.random() < 0.5 ? -1 : 1) * (int) animal.speed;
        }
    }
}