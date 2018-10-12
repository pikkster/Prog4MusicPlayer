import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SoundClipTable extends JTable {

	private List<SoundClip> clips;
	private String[] columnNames = {"Song","Flagged","Rating"};
	private Object[][] data;

	public SoundClipTable() {
		super();
		clips = new ArrayList<>();

	}
	
	/**
	 * Displays the contents of the specified album
	 * @param a - the album which contents are to be displayed
	 */
	public void display(Album a){
//		this.clearTable();

		// to the instance variable 'clips'.
		//
		// Something like this:
		//
		// clips.addAll(a.getAllSoundClips());

		clips.addAll(a.getItems());

		DefaultTableModel model = new DefaultTableModel();

		for (String columnName : columnNames) {
			model.addColumn(columnName);
		}
		this.setModel(model);

		this.setValueAt(clips.get(0).toString(),0 ,0);
//		Object[] data = new Object[clips.size()];
//		Iterator<SoundClip> it = clips.iterator();
//		int i = 0;
//		while(it.hasNext()){
//			SoundClip s = it.next();
//			data[i++] = s.toString();
//		}
//		this.setListData(data);

		invalidate();
		revalidate();
		doLayout();
		repaint();
	}

	private JTable temp () {
		data = new Object[clips.size()][columnNames.length];

		for (int i = 0;i<clips.size();i++) {
			for (int j = 0; j < 3; j++) {
				data[i][0] = clips.get(i).toString();
				data[i][1] = clips.get(i).getFlagged();
				data[i][2] = clips.get(i).getRating();
				System.out.println(data[i][j]);
			}
		}
		return new JTable(data,columnNames);
	}
	
	/**
	 * Clears the contents of the table and the clips List
	 */
	private void clearTable(){
		clips.removeAll(clips);
//		this.setListData(new String[0]);
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
