// CHANGED: new file.
// The "Subject" interface in the Observer pattern.
// IconEdit is the Concrete Subject that implements it; EditObserver is the Observer interface.
public interface EditSubject {
    void attach(EditObserver o);
    void detach(EditObserver o);
    void notifyObservers();
}