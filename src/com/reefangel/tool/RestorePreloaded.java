/**
 * you can put a one sentence description of your tool here.
 *
 * ##copyright##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author		##author##
 * @modified	##date##
 * @version		##version##
 */

 package com.reefangel.tool;
 
 import processing.app.*;
 import processing.app.tools.Tool;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
  
 public class RestorePreloaded implements Tool {
	 Editor editor;
	 Font font = UIManager.getFont("Label.font");
     String bodyRule = "body { font-family: " + font.getFamily() + "; font-size: " + font.getSize() + "pt; }";

	public String getMenuTitle() {
		return "Restore Preloaded Code";
	}
 
	public void init(Editor theEditor) {
		this.editor = theEditor;
	}
 
	public void run() {
		Object[] options = { "Yes", "No" };
	    int result = JOptionPane.showOptionDialog(editor,
	                                              "Would you like to restore the Reef Angel preloaded code?",
	                                              "Restore Code",
	                                              JOptionPane.YES_NO_OPTION,
	                                              JOptionPane.QUESTION_MESSAGE,
	                                              null,
	                                              options,
	                                              options[1]);
	    if (result == JOptionPane.YES_OPTION) {

	    	if (editor.getSketch().getCode(0).isModified())
	    	{
	    		JOptionPane.showMessageDialog(editor,
                        "You must save the currect sketch before proceeding",
                        "Error",JOptionPane.ERROR_MESSAGE);
	    		return;
	    	}

    		editor.getBase();
//	    		Base.copyFile(new File (Base.getSketchbookFolder().getPath() + "/RA_Preloaded/features.txt"), new File (Base.getSketchbookFolder().getPath() + "/libraries/ReefAngel_Features/ReefAngel_Features.h"));
			//copy(Base.getSketchbookFolder().getPath() + "/RA_Preloaded/features.txt",Base.getSketchbookFolder().getPath() + "/libraries/ReefAngel_Features/ReefAngel_Features.h");
			editor.getBase().handleOpenReplace(Base.getSketchbookFolder().getPath() + "/RA_Preloaded/RA_Preloaded.ino");
			
			if (!ShowStep1()) return;
			
			editor.handleExport(false);
			ShowStep2();

			//	    	System.out.println(editor.getSketch().getPrimaryFile());
	    }
	  }
	
	private boolean ShowStep1()
	{
    	JPanel panel = new JPanel();
    	AddPanel(panel);
    	
    	JPanel panel2 = new JPanel();
    	
    	panel2.setLayout(new BoxLayout( panel2, BoxLayout.PAGE_AXIS));
		JLabel steps = new JLabel("Step 1");
    	steps.setForeground(new Color(58,95,205));
    	steps.setFont(new Font("Arial", Font.BOLD, 24));
    	panel2.add(steps);
    	JLabel text = new JLabel("<HTML><br>Please connect the USB-TTL cable to your Reef Angel Controller.<br>Make sure the controller is powered up.<br><br></HTML>");
    	panel2.add(text);
    	ImageIcon iconnection = null;
    	iconnection = new ImageIcon(Base.getSketchbookFolder().getPath() + "/tools/Wizard/data/connection.png");
		JLabel c = new JLabel(iconnection);
    	panel2.add(c);
    	panel.add(panel2);

    	Object[] options = { "Continue", "Cancel" };
	    int result = JOptionPane.showOptionDialog(editor,
    			panel,
                "Step 1",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
	    if (result == 1 ) {
	    	return false;
	    }
	    return true;
	}

	private void ShowStep2()
	{
    	JPanel panel = new JPanel();

    	AddPanel(panel);
    	JPanel panel2 = new JPanel();
    	
    	panel2.setLayout(new BoxLayout( panel2, BoxLayout.PAGE_AXIS));
		JLabel steps = new JLabel("Step 2");
    	steps.setForeground(new Color(58,95,205));
    	steps.setFont(new Font("Arial", Font.BOLD, 24));
    	panel2.add(steps);
    	JLabel text = new JLabel("<HTML><br>Arduino is now compiling your sketch.<br>In a few seconds, it will start uploading the code to your Reef Angel Controller.<br>When the upload process completes, your Reef Angel Controller will have<br>the same pre-loaded code that was originally shipped with your controller.<br><br></HTML>");
    	panel2.add(text);
    	panel.add(panel2);
		JOptionPane.showMessageDialog(editor,
    			panel,
                "Step 2",JOptionPane.DEFAULT_OPTION);		
	}
	
	  public static void copy(String fromFileName, String toFileName)
		      throws IOException {
		    File fromFile = new File(fromFileName);
		    File toFile = new File(toFileName);

		    if (!fromFile.exists())
		      throw new IOException("FileCopy: " + "no such source file: "
		          + fromFileName);
		    if (!fromFile.isFile())
		      throw new IOException("FileCopy: " + "can't copy directory: "
		          + fromFileName);
		    if (!fromFile.canRead())
		      throw new IOException("FileCopy: " + "source file is unreadable: "
		          + fromFileName);

		    if (toFile.isDirectory())
		      toFile = new File(toFile, fromFile.getName());

		    if (toFile.exists()) {
		    	toFile.delete();
//		      if (!toFile.canWrite())
//		        throw new IOException("FileCopy: "
//		            + "destination file is unwriteable: " + toFileName);
//		      System.out.print("Overwrite existing file " + toFile.getName()
//		          + "? (Y/N): ");
//		      System.out.flush();
//		      BufferedReader in = new BufferedReader(new InputStreamReader(
//		          System.in));
//		      String response = in.readLine();
//		      if (!response.equals("Y") && !response.equals("y"))
//		        throw new IOException("FileCopy: "
//		            + "existing file was not overwritten.");
		    } else {
		      String parent = toFile.getParent();
		      if (parent == null)
		        parent = System.getProperty("user.dir");
		      File dir = new File(parent);
		      if (!dir.exists())
		        throw new IOException("FileCopy: "
		            + "destination directory doesn't exist: " + parent);
		      if (dir.isFile())
		        throw new IOException("FileCopy: "
		            + "destination is not a directory: " + parent);
		      if (!dir.canWrite())
		        throw new IOException("FileCopy: "
		            + "destination directory is unwriteable: " + parent);
		    }

		    FileInputStream from = null;
		    FileOutputStream to = null;
		    try {
		      from = new FileInputStream(fromFile);
		      to = new FileOutputStream(toFile);
		      byte[] buffer = new byte[4096];
		      int bytesRead;

		      while ((bytesRead = from.read(buffer)) != -1)
		        to.write(buffer, 0, bytesRead); // write
		    } finally {
		      if (from != null)
		        try {
		          from.close();
		        } catch (IOException e) {
		          ;
		        }
		      if (to != null)
		        try {
		          to.close();
		        } catch (IOException e) {
		          ;
		        }
		    }
		  }

	  private void AddPanel(JPanel container)
	  {
	    	JPanel panel1 = new JPanel(); 
	    	panel1.setLayout(new BoxLayout( panel1, BoxLayout.PAGE_AXIS));
	    	ImageIcon icon=null;
	    	icon = new ImageIcon(Base.getSketchbookFolder().getPath() + "/tools/Wizard/data/ra_small.png");
			JLabel logo=new JLabel(icon);
			panel1.add(logo);
			logo.setAlignmentX(Component.CENTER_ALIGNMENT);
		    JEditorPane RA_link = new JEditorPane("text/html","<html><center><a href='http://www.reefangel.com'>www.reefangel.com</a></center></html>");   
			RA_link.setEditable(false);   
			RA_link.setOpaque(false);   
			RA_link.setBorder(null);
		    ((HTMLDocument)RA_link.getDocument()).getStyleSheet().addRule(bodyRule);
		    panel1.add(RA_link);
		    RA_link.addHyperlinkListener(new HyperlinkListener() {   
			public void hyperlinkUpdate(HyperlinkEvent hle)
			{   
				if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType()))
				{   
					if (isBrowsingSupported()) { 
					      try { 
					          Desktop desktop = java.awt.Desktop.getDesktop(); 
					          URI uri = new java.net.URI(hle.getURL().toString()); 
					          desktop.browse(uri); 
					      } catch (URISyntaxException use) { 
					          throw new AssertionError(use); 
					      } catch (IOException ioe) { 
					          ioe.printStackTrace(); 
					          JOptionPane.showMessageDialog(null, "Sorry, a problem occurred while trying to open this link in your system's standard browser.","A problem occured", JOptionPane.ERROR_MESSAGE); 
					      } 							       
					} 

				}   
			}
			});

		    container.add(panel1);
	  }

		private static boolean isBrowsingSupported() { 
		    if (!Desktop.isDesktopSupported()) { 
		        return false; 
		    } 
		    boolean result = false; 
		    Desktop desktop = java.awt.Desktop.getDesktop(); 
		    if (desktop.isSupported(Desktop.Action.BROWSE)) { 
		        result = true; 
		    } 
		    return result; 
		 
		} 	  
	}
 
 



