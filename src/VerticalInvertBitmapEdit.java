// A "Concrete Decorator": flips the row of the edit top-to-bottom.
// Only getRow() is overridden; column and color pass through untouched.
public class VerticalInvertBitmapEdit extends BitmapEditDecorator {

    public VerticalInvertBitmapEdit(BitmapEdit wrapped, int rows, int cols) {
        super(wrapped, rows, cols);
    }

    // CHANGED: uses the handout's formula, height - row (10 rows, row 3 -> row 7).
    // An edit on row 0 lands on row "rows", one past the bottom edge, and
    // BitmapGUI.update() skips it. (Also fixed the indentation of this method.)
    @Override
    public int getRow() {
        return rows - wrapped.getRow();
    }
}