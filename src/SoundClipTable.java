import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SoundClipTable extends JTable {

	private List<SoundClip> clips;
	private String[] columnNames = {"Song","Flagged","Rating"};
	private ImageIcon flag = new ImageIcon("icons/Actions-flag-icon.png");
	private ImageIcon one_heart = new ImageIcon("icons/1heart.png");
	private ImageIcon two_heart = new ImageIcon("icons/2heart.png");
	private ImageIcon three_heart = new ImageIcon("icons/3heart.png");
	private ImageIcon four_heart = new ImageIcon("icons/4heart.png");
	private ImageIcon five_heart = new ImageIcon("icons/5heart.png");
	public SoundClipTable() {
		super();
		clips = new ArrayList<>();

	}
	
	/**
	 * Displays the contents of the specified album
	 * @param a - the album which contents are to be displayed
	 */
	public void display(Album a){
		this.clearTable();

		clips.addAll(a.getItems());

		//creating JTable data for each new request
		Object[][] temp = new Object[clips.size()][columnNames.length];
		for(int i = 0;i<clips.size(); i++) {
			temp[i][0] = clips.get(i).songName();

			if (clips.get(i).getFlagged()) temp[i][1] = flag;
			else {temp[i][1] = ""; }

			switch (clips.get(i).getRating()) {
				case 1: temp[i][2] = one_heart;
					break;
				case 2: temp[i][2] = two_heart;
					break;
				case 3: temp[i][2] = three_heart;
					break;
				case 4: temp[i][2] = four_heart;
					break;
				case 5: temp[i][2] = five_heart;
					break;
				default: temp[i][2] = "";
					break;
			}
		}
		DefaultTableModel model = new DefaultTableModel(temp,columnNames){

			//Show imageicon instead of string
			@Override
			public Class getColumnClass(int columnIndex) {
				if (columnIndex == 1 || columnIndex == 2) return ImageIcon.class;
				return Object.class;
			}
			//make cells non-editable, user should not be able to edit a cell
			//through table
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}


		};

		this.setModel(model);
		this.setRowHeight(32);
		this.getColumnModel().getColumn(0).setPreferredWidth(250);
		this.getColumnModel().getColumn(1).setPreferredWidth(50);
		this.getColumnModel().getColumn(2).setPreferredWidth(50);

		invalidate();
		revalidate();
		doLayout();
		repaint();
	}

	/**
	 * Clears the contents of the table and the clips List
	 */
	private void clearTable(){
		clips.removeAll(clips);
	}
	
	/**
	 * Returns all the SoundClips at the specified indices
	 * @param indices of selected clips
	 * @return List of SoundClips at the indices
	 */
	public List<SoundClip> getClips(int[] indices){
		List<SoundClip> l = new ArrayList<>();
		for(int i=0;i<indices.length;i++){
			l.add(clips.get(indices[i]));
		}
		return l;
	}

}
