import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class NameChoice extends Observable implements ActionListener {
	
	private JFrame gui;
	private JPanel namePanel;
	private JLabel description;
	private CustomTextField[] pageNames;
	private JButton submit;
	private int pageNum;
	private ArrayList<String> names;
	
	/**
	 * Create the frame.
	 */
	public NameChoice(int pageNum) {
		
		this.pageNum = pageNum;
		
		gui = new JFrame("Choose Names");

		namePanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5,10,5,10);
		gui.getContentPane().add(namePanel);
		namePanel.setBackground(UIManager.getColor("ColorChooser.background"));
		
		description = new JLabel("Please enter your file names for each page below");
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		namePanel.add(description, gbc);
		
		pageNames = new CustomTextField[pageNum];
				
		for(int i=0;i<pageNum;i++) {
			gbc.gridy = (i+1);
			gbc.gridx = 0;
			
			pageNames[i] = new CustomTextField(20);
			pageNames[i].setPlaceholder("Page " + (i+1));
					
			namePanel.add(pageNames[i], gbc);
		}
		

		submit = new JButton("Submit Names");
		submit.addActionListener(this);
		submit.requestFocus();
		
		gui.getRootPane().setDefaultButton(submit);
		
		gbc.gridy = (pageNum+1);
		gbc.gridx = 0;
		namePanel.add(submit,gbc);		
		
		gui.setLocationRelativeTo(null);
		gui.pack();
		gui.setVisible(true);
	}
	
	public ArrayList<String> getNames() {
		return names;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == submit) {
			names = new ArrayList<String>();
			
			for(int i=0;i<pageNum;i++) {
				names.add(pageNames[i].getText());
			}
			
			gui.setVisible(false);
			
	    	setChanged();
	    	notifyObservers();
		}
	}

}
