import java.util.List;

public class HuntingState implements AnimalState {

    @Override
    public void update(Animal animal, int screenWidth, int screenHeight, List<Animal> animals, List<Grass> grassList) {
        if (!animal.isHungry) {
            animal.setState(new WanderingState());
            return;
        }

        animal.getHuntStrategy().move(animal, animals, grassList, screenWidth, screenHeight);
        animal.updateTimers();

        if (animal.isDead()) {
            animal.setState(new DeadState());
        }
    }
}