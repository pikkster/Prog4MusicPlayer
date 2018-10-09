public interface Command {
    void execute();
    void undo();
    String getCommandType();
    Album<SoundClip> getAlbum();
}
