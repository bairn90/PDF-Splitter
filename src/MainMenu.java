import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class MainMenu extends JFrame implements ActionListener, Observer {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private JPanel MenuPanel;
	/**
	 * 
	 */
	private JButton noNameSub, customNameSub;
	/**
	 * 
	 */
	private JTextArea lblHeader;
	/**
	 * 
	 */
	private NameChoice choiceGUI;
	/**
	 * 
	 */
	private Splitter sp;
	
	/**
	 * 
	 */
	public MainMenu() {
		
		/*
		 * 
		 */
		setTitle("PDF Splitter");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 205);
		setResizable(false);
		
		/*
		 * 
		 */
		MenuPanel = new JPanel();
		MenuPanel.setBackground(UIManager.getColor("ColorChooser.background"));
		MenuPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(MenuPanel);
		MenuPanel.setLayout(null);
		
		/*
		 * Header label creation
		 * Sets the textarea box as a label so as to include line wrapping
		 */
		lblHeader = new JTextArea();
		lblHeader.setBackground(UIManager.getColor("ColorChooser.background"));
		lblHeader.setEditable(false);
		lblHeader.setText("This program will split any PDF file into indivdual PDFs for each page."
				+ "\r\nIf you know the names you want to give to the PDFs please click the Custom Names button."
				+ "\r\nOtherwise click the Auto Generate Names button and the program will give the file names for you");
		lblHeader.setBounds(10, 11, 415, 80);
		lblHeader.setLineWrap(true);
		lblHeader.setWrapStyleWord(true);
		MenuPanel.add(lblHeader);
		
		/*
		 * Submit button creation
		 * Creates button and adds the event listener
		 */
		noNameSub = new JButton("Auto Generate Names");
		noNameSub.setBounds(23, 112, 178, 25);
		MenuPanel.add(noNameSub);
		
		customNameSub = new JButton("Custom Names");
		customNameSub.setBounds(245, 112, 178, 25);
		MenuPanel.add(customNameSub);
		
		noNameSub.addActionListener(this);
		customNameSub.addActionListener(this);
		
		setLocationRelativeTo(null);
		setVisible(true);

	} //MainMenu

	
	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == noNameSub) {
			sp = new Splitter();
			int spReturn = sp.selectFile(this);
			boolean created = false;
			
			
			if(spReturn == 999) {
				JOptionPane.showMessageDialog(this, "Please select a PDF File!");
			}
			
			if(spReturn == 1) {
				
				created = sp.createFiles();
				
				if(created) {
					JOptionPane.showMessageDialog(this, "The PDF's have now been created in a new folder next to the original PDF");
				} else {
					JOptionPane.showMessageDialog(this, "Uh-Oh, somethings gone wrong");
				}
			}
			sp = null;
		}
		
		if(e.getSource() == customNameSub) {
			sp = new Splitter();
			int spReturn = sp.selectFile(this);
			
			if(spReturn == 999) {
				JOptionPane.showMessageDialog(this, "Please select a PDF File!");
			}
			
			if(spReturn == 1) {
				
				//setVisible(false);
				choiceGUI = new NameChoice(sp.getPageNum());
				choiceGUI.addObserver(this);
				
			}	
		}
		
	} //ActionPerformed


	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
		boolean created = false;
				
		created = sp.createFiles(choiceGUI.getNames());
		choiceGUI = null;
		
		//setVisible(true);
		
		if(created) {
			JOptionPane.showMessageDialog(this, "The PDF's have now been created in a new folder next to the original PDF");
		} else {
			JOptionPane.showMessageDialog(this, "Uh-Oh, somethings gone wrong");
		}

		sp = null;
		
	}
}
