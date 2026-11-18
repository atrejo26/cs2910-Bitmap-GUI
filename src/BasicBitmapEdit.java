import java.awt.Color;

// The "Concrete Component" in the Decorator pattern.
// Just stores the raw edit exactly as the user made it.
public class BasicBitmapEdit implements BitmapEdit {
    private int row;
    private int col;
    private Color color;

    public BasicBitmapEdit(int row, int col, Color color) {
        this.row = row;
        this.col = col;
        this.color = color;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getCol() {
        return col;
    }

    @Override
    public Color getColor() {
        return color;
    }
}