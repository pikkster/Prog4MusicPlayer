import java.util.List;
import java.util.Set;

public class MusicOrganizerController {

	private MusicOrganizerWindow view;
	private SoundClipBlockingQueue queue;
	private Album root;
	
	public MusicOrganizerController() {
		
		// TODO: Create the root album for all sound clips
		//CHANGE TO OUR ALBUM CLASS
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
		// TODO: Add the loaded sound clips to the root album

		//ADDED CODE
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
	public void addNewAlbum(Album<SoundClip> album){ //TODO Update parameters if needed - e.g. you might want to give the currently selected album as parameter
		// TODO: Add your code here
		//ADDED CODE
		String newAlbum = view.promptForAlbumName();
		try {
			album.addAlbum(newAlbum);
			view.onAlbumAdded(album.getAlbumByName(newAlbum));
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
	
	/**
	 * Removes an album from the Music Organizer
	 */
	public void deleteAlbum(Album<SoundClip> albumToRemove){ //TODO Update parameters if needed
		// TODO: Add your code here
		//ADDED CODE
		Album parent = albumToRemove.getParent();
//		System.out.println("remove " + albumToRemove.getName()+ " parent " + parent.getName());
		parent.removeAlbum(albumToRemove);
		view.onAlbumRemoved(albumToRemove);
	}
	
	/**
	 * Adds sound clips to an album
	 */
	public void addSoundClips(Album<SoundClip> albumToAddSong, List<SoundClip> soundclips){ //TODO Update parameters if needed
		// TODO: Add your code here
		//ADDED CODE
		for (SoundClip sc : soundclips) {
			albumToAddSong.addItem(sc);
		}

	}
	
	/**
	 * Removes sound clips from an album
	 */
	public void removeSoundClips(Album<SoundClip> albumToRemoveSong, List<SoundClip> soundclips){ //TODO Update parameters if needed
		// TODO: Add your code here
		//ADDED CODE
		for (SoundClip sc : soundclips) {
			albumToRemoveSong.removeItem(sc);
		}
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
}