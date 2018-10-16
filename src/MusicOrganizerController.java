import java.util.List;
import java.util.Set;
import java.util.Stack;

public class MusicOrganizerController {

	private MusicOrganizerWindow view;
	private SoundClipBlockingQueue queue;
	private Album root;
	private Stack<Command> undoStack = new Stack<>();
	private Stack<Command> redoStack = new Stack<>();
	private SearchAlbum flaggedAlbum;
	private SearchAlbum ratingAlbum;

	public MusicOrganizerController() {
		root = new UserAlbum<>("All Sound Clips");
		
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
	Set<SoundClip> loadSoundClips(String path) {
		Set<SoundClip> clips = SoundClipLoader.loadSoundClips(path);
		for (SoundClip sc : clips) {
			root.addItem(sc);
		}
		return clips;
	}
	
	/**
	 * Returns the root album
	 */
	Album<SoundClip> getRootAlbum(){
		return root;
	}

	SearchAlbum getFlaggedAlbum(){
		flaggedAlbum = new SearchAlbum("Flagged");
		return flaggedAlbum;
	}

	SearchAlbum getRatingAlbum () {
		ratingAlbum = new SearchAlbum("Rated Songs");
		return ratingAlbum;
	}

	SearchAlbum getFlagAlbum () {
		return flaggedAlbum;
	}
	SearchAlbum getRateAlbum () {
		return ratingAlbum;
	}

	
	/**
	 * Adds an album to the Music Organizer
	 */
	void addNewAlbum(UserAlbum<SoundClip> album){
		String newAlbum = view.promptForAlbumName();
		Command command = new addNewAlbumCommand(album, newAlbum,view);
		command.execute();
		undoStack.push(command);
		undoPush(command);
	}
	
	/**
	 * Removes an album from the Music Organizer
	 */
	void deleteAlbum(UserAlbum<SoundClip> albumToRemove){
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
	void addSoundClips(Album<SoundClip> albumToAddSong, List<SoundClip> soundClips){
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
	void removeSoundClips(Album<SoundClip> albumToRemoveSong, List<SoundClip> soundclips){
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
	void playSoundClips(){
		List<SoundClip> l = view.getSelectedSoundClips();
		for(int i=0;i<l.size();i++)
			queue.enqueue(l.get(i));
	}

	void undo () {
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
	void redo () {
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

	void flag (List<SoundClip> soundClips) {
		for (SoundClip sc : soundClips) {
			if(!sc.getFlagged()) sc.setFlagged(true);
			else {sc.setFlagged(false);}
		}

		view.onClipsUpdated();
	}

	void rating (List<SoundClip> soundClips) {
		int rating = Integer.parseInt(view.askForRating());
		for (SoundClip sc : soundClips) {
			sc.setRating(rating);
		}
		view.onClipsUpdated();
	}

	void disableButtons (boolean disable) {
		view.disableButtons(disable);
	}
}

