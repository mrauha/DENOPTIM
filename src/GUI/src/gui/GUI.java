package gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Graphical User Interface of the DENOPTIM package.
 * This GUI aims to facilitate:
 * <ul>
 * <li>creation of the various types of DENOPTIM input files</li>
 * <li>reuse and modification of DENOPTIM input files</li>
 * </ul>
 * 
 * @author Marco Foscato
 */

public class GUI 
{

	private JFrame frame;     //GUI window frame
	private int mainFrameInitX = 100;
	private int mainFrameInitY = 100;
	private JPanel framePane; //Panel including tool bar
	private GUIMainPanel mainPanel; //Panel including all but tool bar

	/**
	 * Default width
	 */
	private int width = 800;
	
	/**
	 * Default height
	 */
	private int height = 500;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

//-----------------------------------------------------------------------------
	
	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

//-----------------------------------------------------------------------------
	
	/**
	 * Create and fill frame.
	 */
	private void initialize() {
		frame = new JFrame("DENOPTIM - GUI");
		frame.setBounds(mainFrameInitX, mainFrameInitY, width, height);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent windowEvent) {
		    	closeIfAllSaved();
		    }
		});
		
		//Frame pane contains all (tools bar and the rest)
		framePane = (JPanel) frame.getContentPane();

		//Menu bar
		MainToolBar menuBar = new MainToolBar();
		frame.setJMenuBar(menuBar);
		
		//Main panel is a deck of cards that contains all but the tool bar
		mainPanel = new GUIMainPanel(menuBar);
		framePane.add(mainPanel);
		menuBar.setRefToMainPanel(mainPanel);
		menuBar.setRefToMasterGUI(this);
		
		mainPanel.add(new HomePanel(mainPanel));		
	}
	
//-----------------------------------------------------------------------------
	
	/**
	 * Close GUI is there are no unsaved changes to the open components
	 */
	protected void closeIfAllSaved()
	{
		if (mainPanel.hasUnsavedChanges())
		{
			String[] options = new String[]{"Yes","No"};
			int res = JOptionPane.showOptionDialog(frame,
					"<html>Found unsaved changes.<br>Are you sure you want to "
				            + "close this window?<html>",
				            "Close Window?", 
	                JOptionPane.DEFAULT_OPTION,
	                JOptionPane.QUESTION_MESSAGE,
	                UIManager.getIcon("OptionPane.warningIcon"),
	                options,
	                options[1]);
		    if (res == 0)
		    {
		        System.exit(0);
		    }
		}
		else
		{
			System.exit(0);
		}
	}
	
//-----------------------------------------------------------------------------
	
}