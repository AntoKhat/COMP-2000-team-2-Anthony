import java.util.List;

public interface AnimalState {
    void update(Animal animal, int screenWidth, int screenHeight, List<Animal> animals, List<Grass> grassList);
}