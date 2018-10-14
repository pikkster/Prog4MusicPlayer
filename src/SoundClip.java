import java.io.File;

/**
 * SoundClip is a class representing a digital
 * sound clip file on disk.
 */
public class SoundClip {

	private final File file;
	private boolean flagged;
	private int rating;
	
	/**
	 * Make a SoundClip from a file.
	 * Requires file != null.
	 */
	public SoundClip(File file) {
		assert file != null;
		this.file = file;
		this.flagged = false;
		this.rating = 0;
	}

	/**
	 * @return the file containing this sound clip.
	 */
	public File getFile() {
		return file;
	}
	
	public String toString(){
		return file.getName();
	}

	public String songName() {
		return file.getName().substring(0,file.getName().lastIndexOf("."));
	}

	public void setFlagged (boolean flag) {
		this.flagged = flag;
	}
	public boolean getFlagged () {
		return flagged;
	}

	public void setRating (int rating) {
		this.rating = rating;
	}

	public int getRating () {
		return rating;
	}
	
	@Override
	public boolean equals(Object obj) {
		return 
			obj instanceof SoundClip
			&& ((SoundClip)obj).file.equals(file);
	}
	
	@Override
	public int hashCode() {
		return file.hashCode();
	}
}
