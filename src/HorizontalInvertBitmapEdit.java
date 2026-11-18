public class HorizontalInvertBitmapEdit extends BitmapEditDecorator {

    public HorizontalInvertBitmapEdit(BitmapEdit wrapped, int rows, int cols) {
        super(wrapped, rows, cols);
    }

    // CHANGED: uses the handout's formula, width - col (18 columns, column 4 -> column 14).
    // An edit on column 0 lands one past the right edge, and BitmapGUI.update() skips it.
    @Override
    public int getCol() {
        return cols - wrapped.getCol();
    }
}