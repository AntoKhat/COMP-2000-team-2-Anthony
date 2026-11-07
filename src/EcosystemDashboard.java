import java.awt.*;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class EcosystemDashboard {

    public static void draw(Graphics2D g2, List<Animal> animals) {
        List<Animal> alive = animals.stream()
            .filter(a -> !a.isDead())
            .collect(Collectors.toList());

        // species -> count + hunger stats (min/max/average)
        Map<String, IntSummaryStatistics> hungerBySpecies = alive.stream()
            .collect(Collectors.groupingBy(
                a -> a.getClass().getSimpleName(),
                TreeMap::new,
                Collectors.summarizingInt(a -> a.hunger)));

        // species -> average health
        Map<String, Double> healthBySpecies = alive.stream()
            .collect(Collectors.groupingBy(
                a -> a.getClass().getSimpleName(),
                TreeMap::new,
                Collectors.averagingDouble(a -> a.health)));

        // draw the panel
        int panelHeight = 40 + hungerBySpecies.size() * 16;
        g2.setColor(new Color(255, 255, 255, 200));
        g2.fillRoundRect(10, 10, 330, panelHeight, 10, 10);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Monospaced", Font.BOLD, 12));
        g2.drawString("Ecosystem Dashboard", 20, 28);

        g2.setFont(new Font("Monospaced", Font.PLAIN, 11));
        int lineY = 46;
        for (Map.Entry<String, IntSummaryStatistics> e : hungerBySpecies.entrySet()) {
            String species = e.getKey();
            IntSummaryStatistics hunger = e.getValue();
            double avgHealth = healthBySpecies.get(species);

            String line = String.format("%-14s n=%-3d HP=%5.1f Hunger=%5.0f",
                species, hunger.getCount(), avgHealth, hunger.getAverage());
            g2.drawString(line, 20, lineY);
            lineY += 16;
        }
    }
}