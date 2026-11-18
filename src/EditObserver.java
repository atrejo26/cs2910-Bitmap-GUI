// The "Observer" interface in the Observer pattern.
// Every bitmap editor window will implement this so IconEdit can tell it about new edits.
public interface EditObserver {
    void update(BitmapEdit edit);
}