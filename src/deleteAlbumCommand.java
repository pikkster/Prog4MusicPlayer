import javafx.scene.paint.Stop;

import java.util.List;

public class deleteAlbumCommand implements Command{

    Album<SoundClip> album;
    Album<SoundClip> parent;
    private Memento storedState;

    private class Memento {
        private List<Album<SoundClip>> memChildren;
        private Album<SoundClip> memParent;
        private String memName;
        private List<SoundClip> memItems;

        public Memento(List<Album<SoundClip>> children,
                       Album<SoundClip> parent,
                       String name,
                       List<SoundClip> items) {
            this.memChildren = children;
            this.memParent = parent;
            this.memName = name;
            this.memItems = items;
        }
    }

    public deleteAlbumCommand (Album<SoundClip> albumToRemove, Album<SoundClip> parent) {
        this.album = albumToRemove;
        this.parent = parent;
    }
    @Override
    public void execute() {
        storedState = new Memento(album.getChildren(),
                album.getParent(),
                album.toString(),
                album.getItems());
        parent.removeAlbum(album);
    }

    @Override
    public void undo() {
        try {
            parent.addAlbum(storedState.memName);
        } catch (Exception e) {

        }
    }
}
