import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.List;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.tree.*;


public class MusicOrganizerWindow extends JFrame {

	private static int DEFAULT_WINDOW_WIDTH = 900;
	private static int DEFAULT_WINDOW_HEIGHT = 600;

	private final JTree albumTree;
	private final SoundClipTable clipTable;
	private MusicOrganizerButtonPanel buttonPanel;
	private MusicOrganizerController controller;
	
	public MusicOrganizerWindow(MusicOrganizerController contr) {

		// Store a reference to the controller
		controller = contr;
		
		// make the row of buttons
		buttonPanel = new MusicOrganizerButtonPanel(controller, this);
		
		// make the album tree
		albumTree = makeCatalogTree();
		
		// make the clip table
		clipTable = makeClipTable();
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				new JScrollPane(albumTree), new JScrollPane(clipTable));
		splitPane.setDividerLocation(DEFAULT_WINDOW_WIDTH/2);
		
		// Place the buttonpanel above the two Jscrollpanes
		JSplitPane horizontalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, buttonPanel, splitPane);

		this.add(horizontalSplit);

		this.setRedoEnabled(false);
		this.setUndoEnabled(false);

		addFlaggedRating(albumTree);
				
		// give the whole window a good default size
		this.setTitle("Music Organizer");
		this.setSize(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);

		// end the program when the user presses the window's Close button
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
				
		this.setVisible(true);
		
	}

	/**
	 * Make the tree showing album names. 
	 */
	private JTree makeCatalogTree() {

		DefaultMutableTreeNode tree_root = new DefaultMutableTreeNode();
		tree_root.setUserObject(controller.getRootAlbum());

		
		final JTree tree = new JTree(tree_root);
		tree.setMinimumSize(new Dimension(200, 400));
		
		tree.setToggleClickCount(3); // so that we can use double-clicks for
										// previewing instead of
										// expanding/collapsing

		DefaultTreeSelectionModel selectionModel = new DefaultTreeSelectionModel();
		selectionModel
				.setSelectionMode(DefaultTreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setSelectionModel(selectionModel);

		tree.addMouseListener(new MouseInputAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// if left-double-click @@@changed =2 to ==1
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2){
					// The code here gets invoked whenever the user double clicks in the album tree
					makeClipTable();
					onClipsUpdated();

					//needs cleaning up
					if (getSelectedTreeNode().getUserObject().equals(controller.getFlagAlbum())
					|| getSelectedTreeNode().getUserObject().equals(controller.getRateAlbum())) {
						controller.disableButtons(false);
					} else {
						controller.disableButtons(true);
					}
					System.out.println("show the sound clips for album " + getSelectedTreeNode().getUserObject());
				}
			}
		});

		return tree;
	}

	private void addFlaggedRating (JTree albumTree) {
		DefaultTreeModel model = (DefaultTreeModel) albumTree.getModel();

		DefaultMutableTreeNode tree_root = (DefaultMutableTreeNode) model.getRoot();

		DefaultMutableTreeNode flagged = new DefaultMutableTreeNode();
		flagged.setUserObject(controller.getFlaggedAlbum());

		DefaultMutableTreeNode rating = new DefaultMutableTreeNode();
		rating.setUserObject(controller.getRatingAlbum());

		model.insertNodeInto(flagged,tree_root,tree_root.getChildCount());
		model.insertNodeInto(rating, tree_root, tree_root.getChildCount());

		model.reload();

	}

	/**
	 * Make the table showing sound clips
	 */
	private SoundClipTable makeClipTable(){
		SoundClipTable table = new SoundClipTable();

		table.addMouseListener(new MouseInputAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// if left-double-click @@@changed =2 to ==1
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2){
					controller.playSoundClips();
					System.out.println("clicked on clipTable");
				}
			}
		});
		return table;
	}
	
	/**
	 * Pop up a dialog box prompting the user for a name for a new album.
	 * Returns the name, or null if the user pressed Cancel
	 */
	public String promptForAlbumName() {
		return (String) JOptionPane.showInputDialog(
				albumTree,
				"Album Name: ",
				"Add Album",
				JOptionPane.PLAIN_MESSAGE,
				null,
				null,
				"");
	}


	/**
	 * Return the album currently selected in the album tree. Returns null if no
	 * selection.
	 */
	private DefaultMutableTreeNode getSelectedTreeNode() {
		return  (DefaultMutableTreeNode) albumTree.getLastSelectedPathComponent();
	}
	
	
	/**
	 * Return all the sound clips currently selected in the clip table.
	 */
	public List<SoundClip> getSelectedSoundClips(){
		return clipTable.getClips(clipTable.getSelectedRows());
	}
	
	/**
	 * Return the album currently selected in the album tree. Returns null if no
	 * selection.
	 * @return the selected Album
	 */
	public Album getSelectedAlbum() {
		return  (Album) getSelectedTreeNode().getUserObject();
	}

	/**
	 * Methods to be called in response to events in the Music Organizer
	 */

	/**
	 * Updates the album hierarchy with a new album
	 * @param newAlbum
	 */
	public void onAlbumAdded(Album newAlbum){
		
		assert newAlbum != null;
		
		DefaultTreeModel model = (DefaultTreeModel) albumTree.getModel();
		
		//We search for the parent of the newly added Album so we can create the new node in the correct place
		for(Enumeration e = ((DefaultMutableTreeNode) model.getRoot()).breadthFirstEnumeration(); e.hasMoreElements();){
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) e.nextElement();

			Album parentAlbum;
			//parentAlbum = newAlbum.getParentAlbum();
			parentAlbum = newAlbum.getParent();
			
			if(parentAlbum.equals(parent.getUserObject())){
				
				DefaultMutableTreeNode trnode = new DefaultMutableTreeNode();
				trnode.setUserObject(newAlbum);
				
				model.insertNodeInto(trnode, parent,
						parent.getChildCount());
				albumTree.scrollPathToVisible(new TreePath(trnode.getPath()));
				
			}
		}
	}
	
	/**
	 * Updates the album hierarchy by removing an album from it
	 */
	public void onAlbumRemoved(Album album){
		assert album != null;
		
		DefaultTreeModel model = (DefaultTreeModel) albumTree.getModel();
		
		//We search for the parent node so we update the tree as intended
		for(Enumeration e = ((DefaultMutableTreeNode) model.getRoot()).breadthFirstEnumeration(); e.hasMoreElements();){
			DefaultMutableTreeNode current = (DefaultMutableTreeNode) e.nextElement();
			if(album.equals(current.getUserObject())){
				if(current != null){
					model.removeNodeFromParent(current);
				}
			}
		}
	}
	
	/**
	 * Refreshes the clipTable in response to the event that clips have
	 * been modified in an album
	 */
	public void onClipsUpdated(){
		Album a = (Album) getSelectedTreeNode().getUserObject();

		clipTable.display(a);
	}
	public void setUndoEnabled(boolean enabled){
		this.buttonPanel.setUndoEnabled(enabled);
	}

	public void setRedoEnabled(boolean enabled){
		this.buttonPanel.setRedoEnabled(enabled);
	}

	//Disable buttons when search album is chosen
	void disableButtons (boolean disable) {
		this.buttonPanel.setButtonsEnabled(disable);
	}

	public String askForRating () {

		Object[] ratings = {"0","1","2","3", "4", "5"};

		return Integer.toString(JOptionPane.showOptionDialog(
				clipTable,
				"Rating for song: ",
				"Rating",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				new ImageIcon("icons/favourites_32.png"),
				ratings,ratings[0]));

	}
}
