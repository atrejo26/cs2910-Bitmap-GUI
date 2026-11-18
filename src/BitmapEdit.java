import java.awt.Color;

// The "Component" in the Decorator pattern.
// Anything that describes a single pixel edit: where it happens and what color it uses.
// Both the plain edit and every decorator implement this, so they can be wrapped freely.
public interface BitmapEdit {
    int getRow();
    int getCol();
    Color getColor();
}