import javax.swing.SwingUtilities;

public class MainApplication {

	/**
	 * Main entry point of music organizer.
	 */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				MusicOrganizerController controller = new MusicOrganizerController();
				if (args.length == 0) {
					controller.loadSoundClips("sample-sound");
				} else if (args.length == 1) {
					controller.loadSoundClips(args[0]);
				} else {
					System.err.println("too many command-line arguments");
					System.exit(0);
				}
			
			}
		});
	}

}
