import java.util.List;

public class addNewAlbumCommand implements Command {

    Album<SoundClip> album;
    String newAlbum;
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

            System.out.println("name " + memName + " parent " + memParent + " nr. of children = " + memChildren.size());
        }
    }

    public addNewAlbumCommand(Album<SoundClip> album, String newAlbum) {
        this.album = album;
        this.newAlbum = newAlbum;
    }

    @Override
    public void execute() {
        storedState = new Memento(album.getChildren(),
                album.getParent(),
                album.toString(),
                album.getItems());
        try {
            album.addAlbum(newAlbum);
        } catch (Exception e){

        }
    }

    @Override
    public void undo() {
        album.removeAlbum(album.getAlbumByName(newAlbum));
    }

    @Override
    public void redo() {
        try {
            album.addAlbum(newAlbum);
        } catch (Exception e) {

        }
    }

    public String commandAction() {
        return "album";
    }

}
