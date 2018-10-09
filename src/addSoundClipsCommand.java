import java.util.List;

public class addSoundClipsCommand implements Command {

    Album<SoundClip> album;
    List<SoundClip> soundClips;
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

    public addSoundClipsCommand (Album<SoundClip> albumToAddSong, List<SoundClip> soundClips) {
        this.album = albumToAddSong;
        this.soundClips = soundClips;
    }

    @Override
    public void execute() {
        storedState = new Memento(album.getChildren(),
                album.getParent(),
                album.toString(),
                album.getItems());
        for (SoundClip sc : soundClips) {
            album.addItem(sc);
        }
    }

    @Override
    public void undo() {
        for(SoundClip sc : soundClips) {
            album.removeItem(sc);
        }
    }

    @Override
    public void redo() {
        for (SoundClip sc : soundClips) {
            album.addItem(sc);
        }
    }
    public String commandAction() {
        return null;
    }
}
