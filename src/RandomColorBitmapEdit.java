import java.awt.Color;
import java.util.Random;

public class RandomColorBitmapEdit extends BitmapEditDecorator {

    private static final Random random = new Random();

    // CHANGED: the color is picked once and stored, so this edit always reports the same color
    private Color randomColor;

    public RandomColorBitmapEdit(BitmapEdit wrapped, int rows, int cols) {
        super(wrapped, rows, cols);

        // CHANGED: moved here from getColor()
        randomColor = new Color(
            random.nextInt(256),
            random.nextInt(256),
            random.nextInt(256)
        );
    }

    @Override
    public Color getColor() {
        return randomColor;   // CHANGED: was making a new random color on every call
    }
}