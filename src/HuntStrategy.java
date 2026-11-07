import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/// Behaviour parameterisation Predicate (lambda)decide what is prey
public class HuntStrategy implements MovementStrategy {
    private final Predicate<Animal> isPrey;

    public HuntStrategy(Predicate<Animal> isPrey) {
        this.isPrey = isPrey;
    }

    @Override
    public void chooseDirection(Animal animal, List<Animal> animals, List<Grass> grassList) {
        animals.stream()
            .filter(a -> a != animal && !a.isDead())
            .filter(isPrey)
            .min(Comparator.comparingDouble(animal::getDistanceTo))
            .ifPresent(prey -> MovementStrategy.moveToward(animal, prey.x, prey.y));
    }
}