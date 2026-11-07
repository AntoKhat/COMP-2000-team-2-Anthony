import java.util.List;

public class DeadState implements AnimalState {
    @Override
    public void update(Animal animal, int screenWidth, int screenHeight, List<Animal> animals, List<Grass> grassList) {
        animal.health = 0;
    }
}