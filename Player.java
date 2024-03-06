import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Image;


public class Player {
    public Player(int x, int y, Graphics g, boolean isCrouching) {
        // Draw player
        BufferedImage dino = null;
        try {

            String classPath = Player.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String projectRoot = new File(classPath).getParentFile().getParentFile().getParentFile().getPath();
            System.out.println(projectRoot);
            String imagePath = projectRoot + "/assets/Dino.gif";

            // Load the image using the path
            dino = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (isCrouching) {
            // Player if crouching
            if (dino != null) {
                Image scaledDino = dino.getScaledInstance(65, 40, Image.SCALE_DEFAULT);
                g.drawImage(scaledDino, x - 7, y + 15, null);
            }
        } else {
            // Draw player if not crouching
            if (dino != null) {
                Image scaledDino = dino.getScaledInstance(65, 65, Image.SCALE_DEFAULT);
                g.drawImage(scaledDino, x - 7, y - 7, null);
            }
        }
    }
}
