public class deleteAlbumCommand implements Command{

    private UserAlbum<SoundClip> albumToRemove;
    private UserAlbum<SoundClip> parent;
    MusicOrganizerWindow view;

    public deleteAlbumCommand (UserAlbum<SoundClip> albumToRemove,
                               UserAlbum<SoundClip> parent,
                               MusicOrganizerWindow view) {
        this.albumToRemove = albumToRemove;
        this.parent = parent;
        this.view = view;
    }
    @Override
    public void execute() {
        this.parent.removeAlbum(albumToRemove);
        view.onAlbumRemoved(albumToRemove);

    }

    @Override
    public void undo() {
        try {
            parent.addAlbum(albumToRemove.toString());
            view.onAlbumAdded(albumToRemove);

            for(int i = 0; i<albumToRemove.getChildren().size(); i++) {
                updateTree(albumToRemove.getChildren().get(i));
            }


        } catch (Exception e) {

        }
    }

    @Override
    public void redo() {
        this.execute();
    }

    private void updateTree (Album<SoundClip> album){
        view.onAlbumAdded(album);
        for(int i = 0; i<album.getChildren().size(); i++) {
            updateTree(album.getChildren().get(i));
        }
    }

}
