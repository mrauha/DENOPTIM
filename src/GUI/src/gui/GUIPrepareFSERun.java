package gui;

import javax.swing.JPanel;


/**
 * Master form containing all sub-forms that need to be filled to define the input parameters for FragSpaceExplorer.
 * 
 * @author Marco Foscato
 *
 */

public class GUIPrepareFSERun extends GUIPrepare
{

	/**
	 * Version UID
	 */
	private static final long serialVersionUID = 2579606720045728971L;

	/**
	 * Constructor
	 */
	public GUIPrepareFSERun(GUIMainPanel mainPanel) {
		super(mainPanel, "Prepare FSE experiment");
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		FSEParametersForm gaParsPane = new FSEParametersForm(mainPanel.getSize());
		super.allParams.add(gaParsPane);
		super.tabbedPane.addTab("Combinatorial Explorer", null, gaParsPane, null);
		
		FSParametersForm fseParsPane = new FSParametersForm(mainPanel.getSize());
		super.allParams.add(fseParsPane);
		super.tabbedPane.addTab("Fragment Space", null, fseParsPane, null);
		
		FitnessParametersForm fitParsPane = new FitnessParametersForm(mainPanel.getSize());
		super.allParams.add(fitParsPane);
		super.tabbedPane.addTab("Fitness Provider", null, fitParsPane, null);
		
	}
}
