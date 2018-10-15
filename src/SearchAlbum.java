import java.util.ArrayList;
import java.util.List;

public class SearchAlbum<T> extends Album<T> {

    private List<SoundClip> items = new ArrayList<>();

    public SearchAlbum(String name) {
        super(name);
    }

    @Override
    public void addItem(T t) {

    }

    @Override
    public void removeItem(T t) {

    }


}
