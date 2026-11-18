import java.util.ArrayList;

// The "Concrete Subject" in the Observer pattern.
// CHANGED: now implements EditSubject (the "Subject" interface), so attach, detach
// and notifyObservers are marked @Override.
public class IconEdit implements EditSubject {

    private ArrayList<EditObserver> observers;
    private ArrayList<BitmapEdit> editHistory;

    // CHANGED: the subject's state, the most recent edit
    private BitmapEdit lastEdit;

    public IconEdit() {
        observers = new ArrayList<EditObserver>();
        editHistory = new ArrayList<BitmapEdit>();
        lastEdit = null;
    }

    @Override
    public void attach(EditObserver o) {

        if (!observers.contains(o)) {

            observers.add(o);

            // Synchronize the newly attached editor
            for (BitmapEdit edit : editHistory) {
                o.update(edit);
            }
        }
    }

    @Override
    public void detach(EditObserver o) {
        observers.remove(o);
    }

    public boolean isAttached(EditObserver o) {
        return observers.contains(o);
    }

    public void setEdit(BitmapEdit edit) {

        lastEdit = edit;   // CHANGED: remember the most recent edit

        editHistory.add(edit);

        notifyObservers();   // CHANGED: was notifyObservers(edit)
    }

    // CHANGED: new getter for the subject's state
    public BitmapEdit getEdit() {
        return lastEdit;
    }

    // CHANGED: now public with no parameter, because it comes from EditSubject.
    // It sends the most recent edit to every attached observer.
    @Override
    public void notifyObservers() {

        ArrayList<EditObserver> copy =
            new ArrayList<EditObserver>(observers);

        for (EditObserver o : copy) {
            o.update(lastEdit);
        }
    }
    
}