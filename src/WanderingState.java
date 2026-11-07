import java.util.List;

public class WanderingState implements AnimalState {

    @Override
    public void update(Animal animal, int screenWidth, int screenHeight, List<Animal> animals, List<Grass> grassList) {
        animal.getWanderStrategy().move(animal, animals, grassList, screenWidth, screenHeight); // strategy picks direction + moves
        animal.updateTimers(); // hunger, starvation, cooldowns
        
        if (animal.isHungry) {
            animal.setState(new HuntingState());
            return;
        }

        if (animal.hunger > 200 && animal.reproductionCooldown <= 0) {
            animal.setState(new ReproductionState());
        }
    }
}