package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.Point3d;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

import denoptim.constants.DENOPTIMConstants;
import denoptim.exception.DENOPTIMException;
import denoptim.io.DenoptimIO;
import denoptim.molecule.DENOPTIMAttachmentPoint;
import denoptim.molecule.DENOPTIMFragment;
import denoptim.utils.FragmentUtils;


/**
 * A modal dialog with a molecular viewer that understands DENOPTIM fragments
 * and allows to select fragments.
 * 
 * @author Marco Foscato
 */

public class GUIFragmentSelector extends GUIModalDialog
{
	/**
	 * Version UID
	 */
	private static final long serialVersionUID = 912850110991449553L;
	
	/**
	 * The currently loaded list of fragments
	 */
	private ArrayList<DENOPTIMFragment> fragmentLibrary =
			new ArrayList<DENOPTIMFragment>();
	
	/**
	 * The currently loaded fragment
	 */
	private DENOPTIMFragment fragment;
	
	/**
	 * The index of the currently loaded fragment [0–(n-1)}
	 */
	private int currFrgIdx = 0;
	
	/**
	 * The index of the selected AP
	 */
	private int currApIx = -1;
	
	private FragmentViewPanel fragmentViewer;
	private JPanel fragCtrlPane;
	private JPanel fragNavigPanel;
	private JPanel fragNavigPanel2;
	
	private JSpinner fragNavigSpinner;
	private JLabel totalFragsLabel;
	private final FragSpinnerChangeEvent fragSpinnerListener = 
			new FragSpinnerChangeEvent();
	
	private boolean enforceAPSelection = false;
	
//-----------------------------------------------------------------------------
	
	/**
	 * Constructor
	 */
	public GUIFragmentSelector(ArrayList<IAtomContainer> fragLib)
	{
		super();
		this.setBounds(150, 150, 400, 550);
		this.setTitle("Select fragment and AP");
		
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		// Define the list of frags among which we are selecting
		for (IAtomContainer mol : fragLib)
		{				
			try {
				fragmentLibrary.add(new DENOPTIMFragment(mol));
			} catch (DENOPTIMException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null,"<html>Error importing "
						+ "a fragment.<br>The list of fragment is incomplete."
						+ "<br>Please report this to the DENOPTIM "
						+ "team.</html>",
		                "Error",
		                JOptionPane.PLAIN_MESSAGE,
		                UIManager.getIcon("OptionPane.errorIcon"));
			}
		}
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			
		// The viewer with Jmol and APtable (not editable)
		fragmentViewer = new FragmentViewPanel(false);
		addToCentralPane(fragmentViewer);
		
		// Controls for navigating the fragments list
        fragCtrlPane = new JPanel();
        fragCtrlPane.setVisible(true);
		
        // NB: avoid GroupLayout because it interferes with Jmol viewer and causes exception
        
        fragNavigPanel = new JPanel();
        fragNavigPanel2 = new JPanel();
        JLabel navigationLabel1 = new JLabel("Fragment # ");
        JLabel navigationLabel2 = new JLabel("Current library size: ");
        totalFragsLabel = new JLabel("0");
        
		fragNavigSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 0, 1));
		fragNavigSpinner.setToolTipText("Move to fragment number # in the currently loaded library.");
		fragNavigSpinner.setPreferredSize(new Dimension(75,20));
		fragNavigSpinner.addChangeListener(fragSpinnerListener);
        fragNavigPanel.add(navigationLabel1);
		fragNavigPanel.add(fragNavigSpinner);
		fragCtrlPane.add(fragNavigPanel);
		
        fragNavigPanel2.add(navigationLabel2);
        fragNavigPanel2.add(totalFragsLabel);
		fragCtrlPane.add(fragNavigPanel2);
		addToNorthPane(fragCtrlPane);
		
		// Edit global dialog controls
		this.btnDone.setText("Select current fragment");
		this.btnDone.setToolTipText("<html>Process the currently displayed "
				+ "fragment<br>and the currently slected AP, if any.</html>");
		this.btnDone.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Integer> ids = fragmentViewer.getSelectedAPIDs();
				if (ids.size() > 0)
				{
					currApIx = fragmentViewer.getSelectedAPIDs().get(0);
				}
				else
				{
					if (enforceAPSelection)
					{
						JOptionPane.showMessageDialog(null,"<html>"
								+ "No attachment point (AP) selected.<br>"
								+ "Please select an AP in the table."
								+ "</html>",
				                "Error",
				                JOptionPane.PLAIN_MESSAGE,
				                UIManager.getIcon("OptionPane.errorIcon"));
						return;
					}
				}
				result = new Integer[]{currFrgIdx, currApIx};
				fragmentViewer.clearAPTable();
				fragmentViewer.clearMolecularViewer();
				close();
			}
		});
		
		// Load the first fragment
		currFrgIdx = 0;
		loadCurrentFragIdxToViewer();
		updateFragListSpinner();	
	}
	
//-----------------------------------------------------------------------------

	/**
	 * Allows to control whether confirming the selection of a fragment without
	 * having selected an attachment point is permitted or not.
	 * @param enforced use <code>true</code> to enforce the selection of an AP.
	 */
	public void setRequireApSelection(boolean enforced)
	{
		this.enforceAPSelection = enforced;
	}
	
//-----------------------------------------------------------------------------
	
	/**
	 * Loads the fragments corresponding to the field index.
	 * The molecular data is loaded in the Jmol viewer,
	 * and the attachment point (AP) information in the the list of APs.
	 * Jmol is not aware of AP-related information, so this also launches
	 * the generation of the graphical objects representing the APs.
	 */
	private void loadCurrentFragIdxToViewer()
	{
		if (fragmentLibrary == null)
		{
			JOptionPane.showMessageDialog(null,
	                "No list of fragments loaded.",
	                "Error",
	                JOptionPane.PLAIN_MESSAGE,
	                UIManager.getIcon("OptionPane.errorIcon"));
			return;
		}
		
		clearCurrentSystem();

		fragment = fragmentLibrary.get(currFrgIdx);
		fragmentViewer.loadFragImentToViewer(fragment);
	}
	
//-----------------------------------------------------------------------------

	private void updateFragListSpinner()
	{		
		fragNavigSpinner.setModel(new SpinnerNumberModel(currFrgIdx+1, 1, 
				fragmentLibrary.size(), 1));
		totalFragsLabel.setText(Integer.toString(fragmentLibrary.size()));
	}
	
//-----------------------------------------------------------------------------
	
	private void clearCurrentSystem()
	{
		// Get rid of currently loaded mol
		fragment = null;
		
		// Clear viewer?
		// No, its clears upon loading of a new system.
		// The exception (i.e., removal of the last fragment) is dealt with by
		// submitting "zap" only in that occasion.
		
		// Remove tmp storage of APs
		fragmentViewer.mapAPs = null;
		
		// Remove table of APs
		fragmentViewer.clearAPTable();
	}
	
//-----------------------------------------------------------------------------
	
	private class FragSpinnerChangeEvent implements ChangeListener
	{
		private boolean inEnabled = true;
		
		public FragSpinnerChangeEvent()
		{}
		
		/**
		 * Enables/disable the listener
		 * @param var <code>true</code> to activate listener, 
		 * <code>false</code> to disable.
		 */
		public void setEnabled(boolean var)
		{
			this.inEnabled = var;
		}
		
        @Override
        public void stateChanged(ChangeEvent event)
        {
        	if (!inEnabled)
        	{
        		return;
        	}
      
        	activateTabEditsListener(false);
        	
        	//NB here we convert from 1-based index in GUI to 0-based index
        	currFrgIdx = ((Integer) fragNavigSpinner.getValue()).intValue() - 1;
        	loadCurrentFragIdxToViewer();
        	
        	activateTabEditsListener(true);
        }
	}
	
//-----------------------------------------------------------------------------

    private void activateTabEditsListener(boolean var)
    {
		fragmentViewer.activateTabEditsListener(var);
    }
		
//-----------------------------------------------------------------------------
  	
}
