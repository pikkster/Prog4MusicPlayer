import java.util.List;

public class addSoundClipsCommand implements Command {

    Album<SoundClip> albumToAddSong;
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
        }
    }

    public addSoundClipsCommand (Album<SoundClip> albumToAddSong, List<SoundClip> soundClips) {
        this.albumToAddSong = albumToAddSong;
        this.soundClips = soundClips;
    }

    @Override
    public void execute() {
        storedState = new Memento(albumToAddSong.getChildren(),
                albumToAddSong.getParent(),
                albumToAddSong.toString(),
                albumToAddSong.getItems());
        for (SoundClip sc : soundClips) {
            albumToAddSong.addItem(sc);
        }
    }

    @Override
    public void undo() {
        for(SoundClip sc : soundClips) {
            albumToAddSong.removeItem(sc);
        }
    }

    public String getCommandType(){
        return "addSoundClips";
    }
    public Album<SoundClip> getAlbum () {
        return albumToAddSong;
    }
}
