import java.awt.Color;

// The abstract "Decorator" in the Decorator pattern.
// It wraps another BitmapEdit and, by default, just passes every call through.
// Each concrete decorator overrides only the method it needs to change.
public abstract class BitmapEditDecorator implements BitmapEdit {
    protected BitmapEdit wrapped;
    protected int rows;    // height of the bitmap (needed for the invert decorators)
    protected int cols;    // width of the bitmap

    public BitmapEditDecorator(BitmapEdit wrapped, int rows, int cols) {
        this.wrapped = wrapped;
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public int getRow() {
        return wrapped.getRow();
    }

    @Override
    public int getCol() {
        return wrapped.getCol();
    }

    @Override
    public Color getColor() {
        return wrapped.getColor();
    }
}