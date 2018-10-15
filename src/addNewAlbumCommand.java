public class addNewAlbumCommand implements Command {

    private UserAlbum<SoundClip> album;
    private String newAlbum;
    private UserAlbum<SoundClip> temp;
    MusicOrganizerWindow view;

    public addNewAlbumCommand(UserAlbum<SoundClip> album,
                              String newAlbum,
                              MusicOrganizerWindow view) {
        this.album = album;
        this.newAlbum = newAlbum;
        this.view = view;
    }

    @Override
    public void execute() {
        try {
            album.addAlbum(newAlbum);
            temp = album.getAlbumByName(newAlbum);
            view.onAlbumAdded(temp);
        } catch (Exception e){

        }
    }

    @Override
    public void undo() {
        album.removeAlbum(temp);
        view.onAlbumRemoved(temp);
    }

    @Override
    public void redo() {
        this.execute();
    }


}
