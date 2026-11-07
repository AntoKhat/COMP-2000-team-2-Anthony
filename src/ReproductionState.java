import java.util.List;
import java.util.Comparator;

public class ReproductionState implements AnimalState {

    private static final int MAX_POPULATION_PER_SPECIES = 12;
    private static final int REPRODUCTION_COOLDOWN = 600;
    private static final int MIN_HUNGER_TO_BREED = 200;
    private static final int BIRTH_HUNGER_COST = 100;

    @Override
    public void update(Animal animal, int screenWidth, int screenHeight, List<Animal> animals, List<Grass> grassList) {

        // If hungry, eating over breeding
        if (animal.isHungry) {
            animal.setState(new HuntingState());
            return;
        }

        // Too many of this species already -> give up and go wandering
        if (countSpecies(animal, animals) >= MAX_POPULATION_PER_SPECIES) {
            animal.reproductionCooldown = REPRODUCTION_COOLDOWN;
            animal.setState(new WanderingState());
            return;
        }

        Animal partner = findNearestPartner(animal, animals);

        if (partner == null) {
            MovementStrategy.applyMovement(animal, screenWidth, screenHeight);
            animal.updateTimers();
            animal.setState(new WanderingState());
            return;
        }

        MovementStrategy toPartner = (a, all, grass) -> MovementStrategy.moveToward(a, partner.x, partner.y);
        toPartner.move(animal, animals, grassList, screenWidth, screenHeight);
        animal.updateTimers();

        if (animal.getBounds().intersects(partner.getBounds())) {
            Animal child = null;
            if (animal instanceof Dinosaur) {
                child = new Dinosaur(animal.x + 15, animal.y + 15);
            } else if (animal instanceof Caveman) {
                child = new Caveman(animal.x + 15, animal.y + 15);
            } else if (animal instanceof Cow) {
                child = new Cow(animal.x + 15, animal.y + 15);
            }

            if (child != null) {
                // newborns can't breed right away
                child.reproductionCooldown = REPRODUCTION_COOLDOWN;
                animals.add(child);
            }

            animal.hunger -= BIRTH_HUNGER_COST;
            partner.hunger -= BIRTH_HUNGER_COST;

            animal.reproductionCooldown = REPRODUCTION_COOLDOWN;
            partner.reproductionCooldown = REPRODUCTION_COOLDOWN;

            animal.setState(new WanderingState());
            partner.setState(new WanderingState());
        }
    }

    private Animal findNearestPartner(Animal animal, List<Animal> animals) {
        return animals.stream()
            .filter(a -> a != animal)
            .filter(a -> a.getClass().equals(animal.getClass()))
            .filter(a -> !a.isDead())
            .filter(a -> !a.isHungry)
            .filter(a -> a.hunger > MIN_HUNGER_TO_BREED)
            .filter(a -> a.reproductionCooldown <= 0)
            .min(Comparator.comparingDouble(animal::getDistanceTo))
            .orElse(null);
    }

    private int countSpecies(Animal animal, List<Animal> animals) {
        return (int) animals.stream()
            .filter(a -> a.getClass().equals(animal.getClass()) && !a.isDead())
            .count();
    }
}