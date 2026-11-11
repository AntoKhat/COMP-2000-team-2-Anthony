import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpriteLoader {
    public static BufferedImage load(String path) {
        if (!path.startsWith("src/")) {
            path = "src/" + path;
        }
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Could not load image: " + path);
            return null;
        }
    }
}