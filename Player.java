import java.awt.Color;
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
            dino = ImageIO.read(new File("/home/hholt/IdeaProjects/FINAL_OOP_PROJECT/assets/Dino.gif"));
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
