import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Simulator extends JPanel {
    // Owned by the sim thread only. Never touch these from the EDT after startup.
    private final List<Animal> animals = new ArrayList<>();
    private final List<Zone> environment = new ArrayList<>();

    // Written only in the constructor, so safe to read from the EDT.
    private final List<Plant> plants = new ArrayList<>();

    private final Random random = new Random();
    private final int targetPlantCount = 12;

    private final BufferedImage background =
        SpriteLoader.load("src/assets/background.png");

    // Background scaled to the panel size. EDT only (built in paintComponent).
    private BufferedImage scaledBg;

    // Sim thread: runs the update loop (movement, collisions, combat)
    private final ScheduledExecutorService simExecutor =
        Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "sim-loop");
            t.setDaemon(true);
            return t;
        });

    // UI -> sim thread: actions to run at the start of the next tick
    private final Queue<Runnable> commands = new ConcurrentLinkedQueue<>();

    // Sim thread -> EDT: copies the painter reads. Replaced every tick.
    private volatile List<Animal> animalsView = List.of();
    private volatile List<Zone> environmentView = List.of();

    // Panel size, cached so the sim thread never calls Swing getters
    private volatile int simWidth;
    private volatile int simHeight;

    public Simulator() {
        // add some initial dinosaurs and cavemen
        // (these are queued and run on the first tick, in this order)
        createDinosaur(100, 200);
        createDinosaur(100, 50);
        createCaveman(210, 200);
        createCaveman(220, 200);
        createCaveman(500, 500);
        createCaveman(500, 400);
        createVolcano(400, 300, 150, 0);
        createCave(1500, 1000);
        createCow(200, 300);
        createCow(100, 250);
        createCow(250, 100);

        for (int i = 0; i < targetPlantCount; i++) {
            createPlant();
        }

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                simWidth = getWidth();
                simHeight = getHeight();
            }
        });

        // Runs on the sim thread, so the EDT is free for painting
        simExecutor.scheduleAtFixedRate(this::tick, 0, 16, TimeUnit.MILLISECONDS);
    }

    // One simulation step: runs on the sim thread
    private void tick() {
        try {
            Runnable c;
            while ((c = commands.poll()) != null) {
                c.run();
            }

            updateSimulation();

            // Publish fresh copies for the painter
            animalsView = new ArrayList<>(animals);
            environmentView = new ArrayList<>(environment);

            repaint(); // safe to call from any thread
        } catch (Throwable t) {
            // scheduleAtFixedRate stops silently forever if a task throws
            t.printStackTrace();
        }
    }

    private void updateSimulation() {
        int screenWidth = simWidth;
        int screenHeight = simHeight;

        if (environment.isEmpty()) return;
        Volcano volcano = (Volcano) environment.get(0);

        for (int j = 0; j < environment.size(); j++) { // updates the environment
            environment.get(j).update();
        }

        for (int i = 0; i < animals.size(); i++) { // kills animals inside the volcano's lava range
            if (volcano.contains(animals.get(i).x, animals.get(i).y)) {
                animals.get(i).kill();
            }
        }

        

        for (int i = 0; i < animals.size(); i++) {
            for (int j = i + 1; j < animals.size(); j++) {
                Animal a = animals.get(i);
                Animal b = animals.get(j);

                // check if animals collide with each other
                if (a.getBounds().intersects(b.getBounds())) {
                    CombatSystem.handleCollision(a, b, animals);
                }
            }
        }

        for (int i = animals.size() - 1; i >= 0; i--) {
            if (animals.get(i).isDead()) {
                animals.remove(i); // remove dead animal
            }
        }
    }

    // EDT only paints. It reads the published views, never the live lists.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int w = getWidth(), h = getHeight();

        // Keep the sim thread's size up to date (covers the startup gap before any resize event)
        simWidth = w;
        simHeight = h;

        // Scale the background once per size change instead of every frame
        if (background != null && w > 0 && h > 0) {
            if (scaledBg == null || scaledBg.getWidth() != w || scaledBg.getHeight() != h) {
                BufferedImage s = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                Graphics2D bg = s.createGraphics();
                bg.drawImage(background, 0, 0, w, h, null);
                bg.dispose();
                scaledBg = s;
            }
            g2.drawImage(scaledBg, 0, 0, null);
        }

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Zone z : environmentView) {
            z.draw(g2);
        }

        for (Plant p : plants) {
            p.draw(g2);
        }

        for (Animal a : animalsView) {
            a.draw(g2);
        }
    }

    // All create methods go through the queue so the EDT never mutates sim state
    public void createVolcano(int spawnX, int spawnY, int radius, int lavaRad) {
        commands.add(() -> environment.add(new Volcano(spawnX, spawnY, radius, lavaRad)));
    }

    public void createCave(int spawnX, int spawnY) {
        commands.add(() -> environment.add(new Cave(spawnX, spawnY)));
    }

    public void createDinosaur(int spawnX, int spawnY) {
        commands.add(() -> animals.add(new Dinosaur(spawnX, spawnY)));
    }

    public void createCaveman(int spawnX, int spawnY) {
        commands.add(() -> animals.add(new Caveman(spawnX, spawnY)));
    }

    public void createCow(int spawnX, int spawnY) {
        commands.add(() -> animals.add(new Cow(spawnX, spawnY)));
    }

    private void createPlant() {
        int panelWidth = getWidth() > 100 ? getWidth() : 1000;
        int panelHeight = getHeight() > 100 ? getHeight() : 700;

        int x = 30 + random.nextInt(Math.max(1, panelWidth - 90));
        int y = 60 + random.nextInt(Math.max(1, panelHeight - 140));

        plants.add(new Plant(x, y));
    }

    // Stop the sim thread when the panel is removed from the window
    @Override
    public void removeNotify() {
        simExecutor.shutdownNow();
        super.removeNotify();
    }
}