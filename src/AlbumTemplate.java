import java.util.List;

public abstract class AlbumTemplate<T> {

    private Album<T> parent;
    private String name;
    private List<T> items;

    abstract void addItem (T t);
    abstract void removeItem(T t);

    Album<T> getParent () {
        return parent;
    }

    List<T> getItems () {
        return items;
    }

    boolean hasParent (){
        return parent != null;
    }

    public String toString() {
        return name;
    }

}
