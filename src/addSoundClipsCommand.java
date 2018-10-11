import java.util.List;

public class addSoundClipsCommand implements Command {

    private Album<SoundClip> album;
    private List<SoundClip> soundClips;
    MusicOrganizerWindow view;

    public addSoundClipsCommand (Album<SoundClip> albumToAddSong,
                                 List<SoundClip> soundClips,
                                 MusicOrganizerWindow view) {
        this.album = albumToAddSong;
        this.soundClips = soundClips;
        this.view = view;
    }

    @Override
    public void execute() {
        for (SoundClip sc : soundClips) {
            album.addItem(sc);
        }
        view.onClipsUpdated();
    }

    @Override
    public void undo() {
        for(SoundClip sc : soundClips) {
            album.removeItem(sc);
        }
        view.onClipsUpdated();

    }

    @Override
    public void redo() {
        for (SoundClip sc : soundClips) {
            album.addItem(sc);
        }
        view.onClipsUpdated();
    }
}
