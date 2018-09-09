package excelsior.managers;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Manager<T> {

    protected List<T> objects;

    public Manager() {
        objects = new CopyOnWriteArrayList<>();
    }

    public void add(T t){
        objects.add(t);
    }

    public void remove(T t){
        objects.remove(t);
    }

    public List<T> getObjects() {
        return objects;
    }
}
