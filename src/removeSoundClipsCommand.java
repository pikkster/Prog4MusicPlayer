import java.util.List;

public class removeSoundClipsCommand implements Command {

    Album<SoundClip> album;
    List<SoundClip> soundClips;
    MusicOrganizerWindow view;

    public removeSoundClipsCommand (Album<SoundClip> album,
                                    List<SoundClip> soundClips,
                                    MusicOrganizerWindow view) {
        this.album = album;
        this.soundClips = soundClips;
        this.view = view;
    }

    @Override
    public void execute() {
        for (SoundClip sc : soundClips) {
            album.removeItem(sc);
        }
        view.onClipsUpdated();
    }

    @Override
    public void undo() {
        for (SoundClip sc :  soundClips) {
            album.addItem(sc);
        }
        view.onClipsUpdated();
    }

    @Override
    public void redo() {
        this.execute();
    }
}
