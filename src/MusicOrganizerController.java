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
	
	/**
	 * Adds an album to the Music Organizer
	 */
	public void addNewAlbum(Album<SoundClip> album){
		String newAlbum = view.promptForAlbumName();
		Command command = new addNewAlbumCommand(album, newAlbum);
		command.execute();
		undoStack.push(command);
		undoPush(command);
		view.onAlbumAdded(album.getAlbumByName(newAlbum));
	}
	
	/**
	 * Removes an album from the Music Organizer
	 */
	public void deleteAlbum(Album<SoundClip> albumToRemove){ //TODO Update parameters if needed
		Command command = new deleteAlbumCommand(albumToRemove, albumToRemove.getParent());
		command.execute();
		undoStack.push(command);
		undoPush(command);
		view.onAlbumRemoved(albumToRemove);
	}
	
	/**
	 * Adds sound clips to an album
	 */
	public void addSoundClips(Album<SoundClip> albumToAddSong, List<SoundClip> soundClips){
		Command command = new addSoundClipsCommand(albumToAddSong,soundClips);
		command.execute();
		undoStack.push(command);
		undoPush(command);
		view.onClipsUpdated();
	}
	
	/**
	 * Removes sound clips from an album
	 */
	public void removeSoundClips(Album<SoundClip> albumToRemoveSong, List<SoundClip> soundclips){ //TODO Update parameters if needed
		Command command = new removeSoundClipsCommand(albumToRemoveSong, soundclips);
		command.execute();
		undoStack.push(command);
		undoPush(command);
		view.onClipsUpdated();
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
			if (command.commandAction()=="album") {
				view.updateTree(root);
			} else {
				view.onClipsUpdated();
			}
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
			if (command.commandAction()=="album") {
				view.updateTree(root);
			} else {
				view.onClipsUpdated();
			}
			if(redoStack.size()==0){
				view.setRedoEnabled(false);
			}
		}
	}

	private void undoPush(Command command){
		undoStack.push(command);
		view.setUndoEnabled(true);
	}
}

