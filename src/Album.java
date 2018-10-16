import java.util.ArrayList;
import java.util.List;

public abstract class Album<T> {

    /*
        Template method album
     */
    List<Album<T>> children = new ArrayList<>();
    Album<T> parent;
    String name;
    List<T> items = new ArrayList<>();

    //Constructor for Root album
    public Album (String name) {
        this.name = name;
    }
    //Constructor for Album
    public Album (String name, Album<T> parent) {
        this.name = name;
        this.parent = parent;
    }
    public String toString () {
        return name;
    }
    //returns a ArrayList<T> with all items in album
    public List<T> getItems() {
        return items;
    }
    //returns ArrayList<Album<T>> with the children of a album
    public List<Album<T>> getChildren() {
        return children;
    }
    //returns the albums parent
    public Album<T> getParent() {
        return parent;
    }

    public abstract void addItem(T t);

    //removes item from album specified
    public abstract void removeItem(T t);

    //checks if items is in album, item.contains() checks ArrayList<T> items
    public boolean hasItem (T t) {
        return items.contains(t);
    }
    //check for parent, needed to see when we're at root
    public boolean hasParent () {
        return parent != null;
    }

//    public Album<T> copy (){
//        Album<T> albumCopy = new Album<>(name,parent);
//        for (T i: items) {
//            albumCopy.addItem(i);
//        }
//        for (Album<T> a: children) {
//            albumCopy.addAlbum(a.copy());
//        }
//        return albumCopy;
//    }
//    private void addAlbum (Album<T> album) {
//        this.children.add(album);
//    }
}

class AlbumNameAlreadyUsedException extends Exception{
}