import java.util.ArrayList;
import java.util.List;

public class UserAlbum<T> extends Album<T> {

    private List<UserAlbum<T>> children = new ArrayList<>();

    UserAlbum(String name) {
        super(name);
    }

    private UserAlbum(String name, UserAlbum<T> parent) {
        super(name,parent);
    }

    void addAlbum(String name) throws AlbumNameAlreadyUsedException {
        if(getAlbumByName(name)!=null) throw new AlbumNameAlreadyUsedException();
        this.children.add(new UserAlbum<>(name,this));
    }

    public UserAlbum<T> getParent() {
        return (UserAlbum<T>) parent;
    }

    @Override
    public void addItem(T t) {
        if(!hasItem(t)) {
            if (hasParent()) {
                parent.addItem(t);
            }
            items.add(t);
        }
    }

    @Override
    public void removeItem(T t) {
        for (Album<T> a : children) {
            a.removeItem(t);
        }
        this.items.remove(t);
    }

    void removeAlbum(UserAlbum<T> albumToRemove) {
        this.children.remove(albumToRemove);
    }

    UserAlbum<T> getAlbumByName(String name) {
        for(int i=0;i<children.size();i++) {
            if(children.get(i).name.equals(name)) return children.get(i);
        }
        return null;
    }



}
