import java.util.Comparator;
import java.util.List;

public class GrazeStrategy implements MovementStrategy {

    @Override
    public void chooseDirection(Animal animal, List<Animal> animals, List<Grass> grassList) {
        if (grassList == null) return;

        grassList.stream()
            .filter(g -> !g.isEaten)
            .min(Comparator.comparingDouble(g -> Math.sqrt((animal.x - g.x) * (animal.x - g.x) + (animal.y - g.y) * (animal.y - g.y))))
            .ifPresent(grass -> {
                MovementStrategy.moveToward(animal, grass.x, grass.y);

                if (animal.getBounds().intersects(grass.getBounds())) {
                    grass.isEaten = true;
                    animal.hunger = Math.min(animal.maxHunger, animal.hunger + grass.foodValue);
                    animal.isHungry = false;
                    animal.starvationTimer = animal.defaultStarvationTime;
                }
            });
    }
}