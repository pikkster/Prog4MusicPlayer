import java.util.ArrayList;
import java.util.List;

public class Album<T> {

    /*
    * Class representing our album structure
    * Tree library for albums
    */
    private List<Album<T>> children = new ArrayList<>();
    private Album<T> parent;
    private String name;
    private List<T> items = new ArrayList<>();

    //Constructor for Root album
    public Album (String name) {
        this.name = name;

    }
    //Constructor for Album
    private Album (String name, Album<T> parent) {
        this.name = name;
        this.parent = parent;

    }

    public String toString () {
        return name;
    }
    //set name of album
    public void setName(String name) {
        this.name = name;
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
    //adds item to album specified
    public void addItem (T t) {
        if(!hasItem(t)) {
            if (hasParent()) {
                parent.addItem(t);
            }
            items.add(t);
        }
    }
    //removes item from album specified
    public void removeItem (T t) {
        for (Album<T> a : children) {
            a.removeItem(t);
        }
        this.items.remove(t);
    }
    //checks if items is in album, item.contains() checks ArrayList<T> items
    public boolean hasItem (T t) {
        return items.contains(t);
    }
    //check for parent, needed to see when we're at root
    public boolean hasParent () {
        return parent != null;
    }
    //used for finding album by name within children, returns
    public Album<T> getAlbumByName(String name) {
        for(int i=0;i<children.size();i++) {
            if(children.get(i).name.equals(name)) return children.get(i);
        }
        return null;
    }
    //Adds album to root or subalbum specified, checks if children contains album with same name => throws error
    public void addAlbum (String name) throws AlbumNameAlreadyUsedException {
        if(getAlbumByName(name)!=null) throw new AlbumNameAlreadyUsedException();
        this.children.add(new Album<T>(name,this));
    }
    private void addAlbum (Album<T> album) {
        this.children.add(album);
    }
    //removes album
    public void removeAlbum (Album<T> album) {
        this.children.remove(album);
    }
    public Album<T> copy (){
        Album<T> albumCopy = new Album<>(name,parent);
        for (T i: items) {
            albumCopy.addItem(i);
        }
        for (Album<T> a: children) {
            albumCopy.addAlbum(a.copy());
        }
        return albumCopy;
    }
    public boolean hasChildren () {
        return children.size() != 0;
    }

}

class AlbumNameAlreadyUsedException extends Exception{
}