import java.awt.FileDialog;

import javax.swing.JFrame;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BadPdfFormatException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;

public class Splitter {

	/**
	 * FileDialog box to obtain the filename of the PDF
	 */
	private FileDialog fileChooser;
	/**
	 * Folder holds the filepath with the pdf stripped out
	 * inFile holds the full filepath selected in the fileDialog
	 * Outfile holds the name of the split pdf being created at each split
	 * fileType hold the extension to check the it is PDF
	 */
	private String folder, inFile, outFile, fileType;
	/**
	 * List to hold the new split filenames
	 */
	private ArrayList<String> pageName;
	/**
	 * Holds the number of PDF Pages
	 */
	private int pageNum;
	
	/**
	 * Object to read in original PDF
	 */
	private PdfReader reader;
	/**
	 * Object to create new PDF
	 */
	private PdfCopy writer;
	
	
	public Splitter() {
		
	}
	
	/**
	 * Returns 0 if user hit cancel
	 * Returns 999 if user didn't select a PDF
	 * Returns 1 if user selected PDF file
	 */
	public int selectFile(JFrame gui) {
		 	

		/*
		 * Show the file dialog box and ask the user to choose a PDF
		 */
    	fileChooser = new FileDialog(gui, "Choose a pdf", FileDialog.LOAD);
    	fileChooser.setVisible(true);
    	folder = fileChooser.getDirectory();
    	inFile = folder + fileChooser.getFile();
   	
    	/*
    	 * Set the filepath to lowercase for later comparison
    	 */
    	inFile.toLowerCase();
    	fileType = inFile.substring(inFile.length()-4);
 
    	/*
    	 * Return based on the users input
    	 */
    	if(inFile.equals("nullnull")) {
    		return 0;
    	} else if(!fileType.equals(".pdf")) {
    		return 999;
    	} else {
    		
    		/*
    		 * Initialise reader object that will read in the file and get page numbers
    		 */
    		try {
    			reader = new PdfReader(inFile);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			return 0;
    		}
    		
    		pageNum = reader.getNumberOfPages();
    		
    		return 1;
    	}
    	
	} //selectFile
	
	/**
	 * returns the number of pages in the selected PDF from the select file method
	 */
	public int getPageNum() {
		return pageNum;
	}
	
	/**
	 * Creates the pageName list and calls the split method with false
	 * so that the method will create filenames for the new PDFs
	 */
	public boolean createFiles() {		

		pageName = new ArrayList<String>();
		
		return split(false);
		
	}
	
	/**
	 * Sets the list to hold the new file names from the parameter and then
	 * run the split method with true so method doesn't input automatic filenames
	 */
	public boolean createFiles(ArrayList<String> pageName) {
		
		this.pageName = pageName;
		
		return split(true);
	} 
	
	/**
	 * Takes in the original filename, creates a new folder then saves 
	 * the individual pages as new files inside that folder
	 */
	private boolean split(Boolean hasNames) {
		
    	/*
    	 * In the same path as the PDF create a folder called Split PDFs
    	 */
    	File dir = new File(inFile + " - Split");
    	dir.mkdir();
    	
    	/*
    	 * Amend folder to now point to the newly created folder
    	 */
    	folder = inFile + " - Split\\";
		
    	/*
    	 * If there has been no names given for the new files 
    	 * then loop through adding to the list
    	 */
		if (!hasNames) {
			for(int y=0;y<pageNum;y++) {
				pageName.add("Page "+(y+1));
			}	
		}
		
        /*
         * Loop through for each page and create the new PDF
         */
        for(int i=0;i<pageName.size();i++) {
        	/*
        	 * Set the new path and name from the lines array
        	 */
        	outFile = folder + pageName.get(i) + ".pdf";
        	
        	/*
        	 * Opens the document sents the outfile to the writer 
        	 * then gets the page from the original file to write
        	 */
            Document document = new Document(reader.getPageSizeWithRotation(1));
			
            try {
				writer = new PdfCopy(document, new FileOutputStream(outFile));
			} catch (FileNotFoundException | DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
            document.open();
            PdfImportedPage page = writer.getImportedPage(reader, i+1);
			
            try {
				writer.addPage(page);
			} catch (BadPdfFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
            document.close();
            
        }
        
        writer.close();
        reader.close();
        pageName.clear();
        return true;

    	
	} //Split
	
}
