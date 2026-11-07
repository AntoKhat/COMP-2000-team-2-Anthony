import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Simulator extends JPanel {
    private List<Animal> animals = new ArrayList<>();
    private List<Zone> environment = new ArrayList<>();
    private List<Grass> grassList = new ArrayList<>();
    private List<Plant> plants = new ArrayList<>();
    private final Timer timer;
    private int grassSpawnTimer = 0;
    private final Random rand = new Random();
    private final int targetPlantCount = 12;

    private final BufferedImage background =
        SpriteLoader.load("src/assets/background.png");

    public Simulator() {
        createDinosaur(150, 200);
        createDinosaur(150, 500);
        createDinosaur(400, 400);
        createDinosaur(400, 50);
        createCaveman(210, 200);
        createCaveman(220, 200);
        createCaveman(500, 500);
        createCaveman(500, 400);
        createCow(200, 300);
        createCow(100, 250);
        createCow(250, 100);
        createCow(400, 400);
        createCow(450, 400);
        createCow(300, 300);

        // Creates environment with their related status effect


        environment.add(ZoneEffects.heal(ZoneEffects.shelter(new Cave(800, 700), a -> a instanceof Caveman), a -> a instanceof Caveman));
        environment.add(ZoneEffects.heal(ZoneEffects.shelter(new Cave(2000, 500), a -> a instanceof Caveman), a -> a instanceof Caveman));
        Volcano volcano = new Volcano(400, 300, 150, 0);
        environment.add(ZoneEffects.poison(new AshCloud(volcano), a -> true, 300));
        environment.add(volcano);
        environment.add(ZoneEffects.cleanse(new WaterHole(120, 1000, 90), a -> true));
        environment.add(ZoneEffects.slow(new Swamp(1300, 20, 400, 380), a -> true));
        environment.add(ZoneEffects.speedBoost(new Plain(1000, 800, 800, 900), a -> a instanceof Dinosaur, 1.5f));

        for (int i = 0; i < targetPlantCount; i++) {
            createPlant();
        }

        timer = new Timer(16, e -> {
            updateSimulation();
            repaint();
        });
        timer.start();
    }

    private void updateSimulation() {
        int screenWidth = getWidth();
        int screenHeight = getHeight();

        animals.forEach(Animal::resetEffects);

        for (Zone zone : environment) {
            zone.update();
            for (Animal a : animals) {
                if (zone.contains(a.x, a.y)) {
                    zone.applyEffect(a);
                }
            }
            for (Grass g : grassList) {
                if (zone.contains(g.x, g.y)) {
                    zone.onGrass(g);
                }
            }
        }

        // Spawning grass gets supressed when volcano errupts
        boolean suppressGrass = environment.stream().anyMatch(Zone::suppressesGrass);
        if (!suppressGrass) {
            grassSpawnTimer++;
            if (grassSpawnTimer >= 30) {
                int randomX = 30 + rand.nextInt(Math.max(100, screenWidth - 60));
                int randomY = 30 + rand.nextInt(Math.max(100, screenHeight - 60));
                grassList.add(new Grass(randomX, randomY));
                grassSpawnTimer = 0;
            }
        }

        // Update animals state
        List<Animal> animalsCopy = new ArrayList<>(animals); // Make a copy of animal list
        for (Animal a : animalsCopy) {
            a.update(screenWidth, screenHeight, animals, grassList);
        }

        // (read only) find every pair of animals collided - parallel stream
        List<Animal[]> collisions = IntStream.range(0, animals.size())
            .parallel()
            .boxed()
            .flatMap(i -> IntStream.range(i + 1, animals.size())
                .filter(j -> animals.get(i).getBounds().intersects(animals.get(j).getBounds()))
                .mapToObj(j -> new Animal[] { animals.get(i), animals.get(j) }))
            .collect(Collectors.toList());

        // resolve the fights one at a time -> avoids race conditions
        for (Animal[] pair : collisions) {
            CombatSystem.handleCollision(pair[0], pair[1], animals);
        }

        // Remove dead animals
        animals.removeIf(Animal::isDead);
        grassList.removeIf(g -> g.isEaten);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (background != null) {
            g2.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Zone zone : environment) {
            zone.draw(g2);
        }

        for (Plant plant : plants) {
            plant.draw(g2);
        }

        for (Grass grass : grassList) {
            grass.draw(g2);
        }

        for (Animal animal : animals) {
            animal.draw(g2);
        }
        EcosystemDashboard.draw(g2, animals);
    }

    public void createVolcano(int spawnX, int spawnY, int radius, int lavaRad) {
        environment.add(new Volcano(spawnX, spawnY, radius, lavaRad));
    }

    public void createCave(int spawnX, int spawnY) {
        environment.add(new Cave(spawnX, spawnY));
    }

    public void createDinosaur(int spawnX, int spawnY) {
        animals.add(new Dinosaur(spawnX, spawnY));
    }

    public void createCaveman(int spawnX, int spawnY) {
        animals.add(new Caveman(spawnX, spawnY));
    }

    public void createCow(int spawnX, int spawnY) {
        animals.add(new Cow(spawnX, spawnY));
    }

    private void createPlant() {
        int panelWidth = getWidth() > 100 ? getWidth() : 1000;
        int panelHeight = getHeight() > 100 ? getHeight() : 700;

        int x = 30 + rand.nextInt(Math.max(1, panelWidth - 90));
        int y = 60 + rand.nextInt(Math.max(1, panelHeight - 140));

        plants.add(new Plant(x, y));
    }
}