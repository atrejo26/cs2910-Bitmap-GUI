import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BitmapFile {
    // Writes the Icon's pixels to a 24-bit .bmp file. Returns true if it worked.
    public static boolean save(Icon icon, int rows, int cols, File file) {
        boolean retVal = false;
        BufferedImage img = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_RGB);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Pixel p = icon.getPixel(r, c);
                Color color = new Color(p.getRed(), p.getGreen(), p.getBlue());
                img.setRGB(c, r, color.getRGB());
            }
        }

        try {
            retVal = ImageIO.write(img, "bmp", file);
        } catch (IOException e) {
            retVal = false;
        }
        return retVal;
    }
    // Reads a bitmap file into an image. Returns null if it can't be read as an image.
    public static BufferedImage load(File file) {
        BufferedImage retVal = null;
        try {
            retVal = ImageIO.read(file);
        } catch (IOException e) {
            retVal = null;
        }
        return retVal;
    }
}