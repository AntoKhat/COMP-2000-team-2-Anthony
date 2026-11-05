import java.awt.Color;
import java.util.function.Predicate;

class ZoneEffects {


    static Zone shelter(Zone zone, Predicate<Animal> who) { // Invulnerable while inside
        return new EffectDecorator(zone, who, a -> a.invulnerable = true, new Color(255, 255, 0, 40));
    }


    static Zone heal(Zone zone, Predicate<Animal> who) {  // Heals a little each tick while inside
        return new EffectDecorator(zone, who, a -> a.heal(0.05f), new Color(0, 255, 0, 40));
    }


    static Zone poison(Zone zone, Predicate<Animal> who, int ticks) {  // Poison that lingers after the animal leaves
        return new EffectDecorator(zone, who, a -> a.setPoison(ticks), new Color(150, 0, 200, 50));
    }


    static Zone cleanse(Zone zone, Predicate<Animal> who) {  // Wipes any lingering poison
        return new EffectDecorator(zone, who, a -> a.cleanse(), new Color(0, 150, 255, 40));
    }
}