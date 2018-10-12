import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class MusicOrganizerController {

	private MusicOrganizerWindow view;
	private SoundClipBlockingQueue queue;
	private Album root;
	private Stack<Command> undoStack = new Stack<>();
	private Stack<Command> redoStack = new Stack<>();
	private Album flaggedAlbum;
	private Album ratingAlbum;

	public MusicOrganizerController() {
		root = new Album<>("All Sound Clips");
		
		// Create the View in Model-View-Controller
		view = new MusicOrganizerWindow(this);
		
		// Create the blocking queue
		queue = new SoundClipBlockingQueue();
		
		// Create a separate thread for the sound clip player and start it
		(new Thread(new SoundClipPlayer(queue))).start();
	}

	/**
	 * Load the sound clips found in all subfolders of a path on disk. If path is not
	 * an actual folder on disk, has no effect.
	 */
	public Set<SoundClip> loadSoundClips(String path) {
		Set<SoundClip> clips = SoundClipLoader.loadSoundClips(path);
		for (SoundClip sc : clips) {
			root.addItem(sc);
		}
		return clips;
	}
	
	/**
	 * Returns the root album
	 */
	public Album<SoundClip> getRootAlbum(){
		return root;
	}

	public Album<SoundClip> getFlaggedAlbum(){
		flaggedAlbum = new Album<>("Flagged");
		return flaggedAlbum;
	}

	public Album<SoundClip> getRatingAlbum () {
		ratingAlbum = new Album<>("Rated Songs");
		return ratingAlbum;
	}
	
	/**
	 * Adds an album to the Music Organizer
	 */
	public void addNewAlbum(Album<SoundClip> album){
		String newAlbum = view.promptForAlbumName();
		Command command = new addNewAlbumCommand(album, newAlbum,view);
		command.execute();
		undoStack.push(command);
		undoPush(command);

	}
	
	/**
	 * Removes an album from the Music Organizer
	 */
	public void deleteAlbum(Album<SoundClip> albumToRemove){
		Command command = new deleteAlbumCommand(albumToRemove,
				albumToRemove.getParent(),
				view);
		command.execute();
		undoStack.push(command);
		undoPush(command);

	}
	
	/**
	 * Adds sound clips to an album
	 */
	public void addSoundClips(Album<SoundClip> albumToAddSong, List<SoundClip> soundClips){
		Command command = new addSoundClipsCommand(albumToAddSong,
				soundClips,
				view);
		command.execute();
		undoStack.push(command);
		undoPush(command);

	}
	
	/**
	 * Removes sound clips from an album
	 */
	public void removeSoundClips(Album<SoundClip> albumToRemoveSong, List<SoundClip> soundclips){
		Command command = new removeSoundClipsCommand(albumToRemoveSong,
				soundclips,
				view);
		command.execute();
		undoStack.push(command);
		undoPush(command);

	}

	/**
	 * Puts the selected sound clips on the queue and lets
	 * the sound clip player thread play them. Essentially, when
	 * this method is called, the selected sound clips in the 
	 * SoundClipTable are played.
	 */
	public void playSoundClips(){
		List<SoundClip> l = view.getSelectedSoundClips();
		for(int i=0;i<l.size();i++)
			queue.enqueue(l.get(i));
	}

	public void undo () {
		if (undoStack.size()>0) {
			Command command = undoStack.pop();
			redoStack.push(command);
			command.undo();
			view.setRedoEnabled(true);
			if(undoStack.size()==0){
				view.setUndoEnabled(false);
			}
		}
	}
	public void redo () {
		if (redoStack.size()>0) {
			Command command = redoStack.pop();
			undoStack.push(command);
			command.redo();
			if(redoStack.size()==0){
				view.setRedoEnabled(false);
			}
		}
	}
	private void undoPush(Command command){
		undoStack.push(command);
		view.setUndoEnabled(true);
	}

	public void flag (List<SoundClip> soundClips) {


	}

	public void rating (List<SoundClip> soundClips) {

	}
}

