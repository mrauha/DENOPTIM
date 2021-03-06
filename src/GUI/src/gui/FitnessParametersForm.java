/*
 *   DENOPTIM
 *   Copyright (C) 2020 Marco Foscato <marco.foscato@uib.no>
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
 * Form collecting input parameters for a setting-up the fitness provider.
 */

public class FitnessParametersForm extends ParametersForm
{

    /**
	 * Version
	 */
	private static final long serialVersionUID = -282726238111247056L;
	
	/**
	 * Unique identified for instances of this form
	 */
	public static AtomicInteger fitFormUID = new AtomicInteger(1);
	
    /**
     * Map connecting the parameter keyword and the field
     * containing the parameter value. 
     */
	private Map<String,Object> mapKeyFieldToValueField;
	
    JPanel block;    
    JPanel localBlock1;
    JPanel localBlock2;
    JPanel localBlock3;
    JPanel localBlock4;
	
	JPanel lineSrcOrNew;
    JRadioButton rdbSrcOrNew;
    
    JPanel lineFPSource;
    JLabel lblFPSource;
    JTextField txtFPSource;
    JButton btnFPSource;
    JButton btnLoadFPSource;
	
	JPanel lineIntOrExt;
    JRadioButton rdbIntOrExt;

    String keyFitProviderSource = "FP-Source";
    JPanel lineFitProviderSource;
    JLabel lblFitProviderSource;
    JTextField txtFitProviderSource;
    JButton btnFitProviderSource;

    String keyFitProviderInterpreter = "FP-Interpreter";
    JPanel lineFitProviderInterpreter;
    JLabel lblFitProviderInterpreter;
    JComboBox<String> cmbFitProviderInterpreter;

    String keyEq = "FP-Equation";
    JPanel lineEq;
    JLabel lblEq;
    JTextField txtEq;

    //HEREGOFIELDS  this is only to facilitate automated insertion of code
        
        
    String NL = System.getProperty("line.separator");
    
    public FitnessParametersForm(Dimension d)
    {
    	mapKeyFieldToValueField = new HashMap<String,Object>();
    	
        this.setLayout(new BorderLayout()); //Needed to allow dynamic resizing!

        block = new JPanel();
        JScrollPane scrollablePane = new JScrollPane(block);
        block.setLayout(new BoxLayout(block, SwingConstants.VERTICAL));    

        localBlock1 = new JPanel();
        localBlock1.setVisible(false);
        localBlock1.setLayout(new BoxLayout(localBlock1, SwingConstants.VERTICAL));
        
        localBlock2 = new JPanel();
        localBlock2.setVisible(true);
        localBlock2.setLayout(new BoxLayout(localBlock2, SwingConstants.VERTICAL));
        
        localBlock3 = new JPanel();
        localBlock3.setVisible(false);
        localBlock3.setLayout(new BoxLayout(localBlock3, SwingConstants.VERTICAL));
        
        localBlock4 = new JPanel();
        localBlock4.setVisible(true);
        localBlock4.setLayout(new BoxLayout(localBlock4, SwingConstants.VERTICAL));
        
        String toolTipSrcOrNew = "Tick here to use settings from file.";
        lineSrcOrNew = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rdbSrcOrNew = new JRadioButton("Use parameters from existing file");
        rdbSrcOrNew.setToolTipText(toolTipSrcOrNew);
        rdbSrcOrNew.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		if (rdbSrcOrNew.isSelected())
        		{
    				localBlock1.setVisible(true);
        			localBlock2.setVisible(false);
        		}
        		else
        		{
        			localBlock1.setVisible(false);
        			localBlock2.setVisible(true);
        		}
        	}
        });
        lineSrcOrNew.add(rdbSrcOrNew);
        block.add(lineSrcOrNew);
        block.add(localBlock1);
        block.add(localBlock2);
        
        String toolTipFPSource = "<html>Pathname of a DENOPTIM's parameter file with fitness-provider settings.</html>";
        lineFPSource = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblFPSource = new JLabel("Use parameters from file:", SwingConstants.LEFT);
        lblFPSource.setToolTipText(toolTipFPSource);
        txtFPSource = new JTextField();
        txtFPSource.setToolTipText(toolTipFPSource);
        txtFPSource.setPreferredSize(fileFieldSize);
        btnFPSource = new JButton("Browse");
        btnFPSource.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
                DenoptimGUIFileOpener.pickFile(txtFPSource);
           }
        });
        btnLoadFPSource = new JButton("Load...");
        txtFPSource.setToolTipText("<html>Specify the file containing the "
        		+ "parameters to be loaded in this form.</html>");
        btnLoadFPSource.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
	        	try 
	        	{
					importParametersFromDenoptimParamsFile(txtFPSource.getText());
				} 
	        	catch (Exception e1) 
	        	{
	        		if (e1.getMessage().equals("") || e1.getMessage() == null)
	        		{
	        			e1.printStackTrace();
						JOptionPane.showMessageDialog(null,
								"<html>Exception occurred while importing parameters.<br>Please, report this to the DENOPTIM team.</html>",
				                "Error",
				                JOptionPane.ERROR_MESSAGE,
				                UIManager.getIcon("OptionPane.errorIcon"));
	        		}
	        		else
	        		{
						JOptionPane.showMessageDialog(null,
								e1.getMessage(),
				                "Error",
				                JOptionPane.ERROR_MESSAGE,
				                UIManager.getIcon("OptionPane.errorIcon"));
	        		}
					return;
				}
            }
        });
        lineFPSource.add(lblFPSource);
        lineFPSource.add(txtFPSource);
        lineFPSource.add(btnFPSource);
        lineFPSource.add(btnLoadFPSource);
        localBlock1.add(lineFPSource);

        String toolTipIntOrExt = "<html>A fitness provider is an existing "
        		+ "tool or script.<br> The fitness provider must produce an "
        		+ "output SDF file with the <code>&lt;FITNESS&gt;</code> or "
        		+ "<code>&lt;MOL_ERROR&gt;</code> tags.</html>";
        lineIntOrExt = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rdbIntOrExt = new JRadioButton("Use external fitnes provider:");
        rdbIntOrExt.setToolTipText(toolTipIntOrExt);
        
        //TODO: tmp code to restrict functionality
        rdbIntOrExt.setSelected(true);
		localBlock3.setVisible(true);
		localBlock4.setVisible(false);
        rdbIntOrExt.setEnabled(false);
        
        rdbIntOrExt.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		
        		//TODO: activate when fully implemented
        		/*
        		if (rdbIntOrExt.isSelected())
        		{
    				localBlock3.setVisible(true);
        			localBlock4.setVisible(false);
        		}
        		else
        		{
        			localBlock3.setVisible(false);
        			localBlock4.setVisible(true);
        		}
        		*/
        	}
        });
        lineIntOrExt.add(rdbIntOrExt);
        localBlock2.add(lineIntOrExt);
        localBlock2.add(localBlock3);
        localBlock2.add(localBlock4);

        //HEREGOESIMPLEMENTATION this is only to facilitate automated insertion of code

        String toolTipFitProviderSource = "Pathname of the executable file.";
        lineFitProviderSource = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblFitProviderSource = new JLabel("Fitness provider executable:", SwingConstants.LEFT);
        lblFitProviderSource.setPreferredSize(fileLabelSize);
        lblFitProviderSource.setToolTipText(toolTipFitProviderSource);
        txtFitProviderSource = new JTextField();
        txtFitProviderSource.setToolTipText(toolTipFitProviderSource);
        txtFitProviderSource.setPreferredSize(fileFieldSize);
        txtFitProviderSource.getDocument().addDocumentListener(fieldListener);
        mapKeyFieldToValueField.put(keyFitProviderSource.toUpperCase(),txtFitProviderSource);
        btnFitProviderSource = new JButton("Browse");
        btnFitProviderSource.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
                DenoptimGUIFileOpener.pickFile(txtFitProviderSource);
           }
        });
        lineFitProviderSource.add(lblFitProviderSource);
        lineFitProviderSource.add(txtFitProviderSource);
        lineFitProviderSource.add(btnFitProviderSource);
        localBlock3.add(lineFitProviderSource);
        
        String toolTipFitProviderInterpreter = "Interpreter to be used for the fitness provider executable";
        lineFitProviderInterpreter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblFitProviderInterpreter = new JLabel("Interpreter for fitnes provider", SwingConstants.LEFT);
        lblFitProviderInterpreter.setPreferredSize(fileLabelSize);
        lblFitProviderInterpreter.setToolTipText(toolTipFitProviderInterpreter);
        cmbFitProviderInterpreter = new JComboBox<String>(new String[] {"BASH", "Python", "JAVA"});
        cmbFitProviderInterpreter.setToolTipText(toolTipFitProviderInterpreter);
        
        //TODO: remove when functionality is fully implemented
        cmbFitProviderInterpreter.setEnabled(false);
        
        mapKeyFieldToValueField.put(keyFitProviderInterpreter.toUpperCase(),cmbFitProviderInterpreter);
        lineFitProviderInterpreter.add(lblFitProviderInterpreter);
        lineFitProviderInterpreter.add(cmbFitProviderInterpreter);
        localBlock3.add(lineFitProviderInterpreter);

        String toolTipEq = "Define integrated fitness provider equation.";
        lineEq = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblEq = new JLabel("<html>Calculate fitness (F) with equation  <i>F=</i></html>", SwingConstants.LEFT);
        //lblEq.setPreferredSize(fileLabelSize);
        lblEq.setToolTipText(toolTipEq);
        txtEq = new JTextField();
        txtEq.setToolTipText(toolTipEq);
        txtEq.setPreferredSize(strFieldSize);
        txtEq.getDocument().addDocumentListener(fieldListener);
        mapKeyFieldToValueField.put(keyEq.toUpperCase(),txtEq);
        lineEq.add(lblEq);
        lineEq.add(txtEq);
        localBlock4.add(lineEq);

        //HEREGOESADVIMPLEMENTATION this is only to facilitate automated insertion of code       
        
        // From here it's all about advanced options
        /*
        JPanel advOptsBlock = new JPanel();
        advOptsBlock.setVisible(false);
        advOptsBlock.setLayout(new BoxLayout(advOptsBlock, SwingConstants.VERTICAL));

        */
        
        /*
        JButton advOptShow = new JButton("Advanced Settings");
        advOptShow.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		if (advOptsBlock.isVisible())
        		{
        			advOptsBlock.setVisible(false);
        			advOptShow.setText("Show Advanced Settings");        			
        		}
        		else
        		{
        			advOptsBlock.setVisible(true);
        			advOptShow.setText("Hide Advanced Settings");
    				scrollablePane.validate();
    				scrollablePane.repaint();
    				scrollablePane.getVerticalScrollBar().setValue(
    						scrollablePane.getVerticalScrollBar().getValue() + (int) preferredHeight*2/3);
        		}
	        }
	    });
        
        JPanel advOptsController = new JPanel();
        advOptsController.setPreferredSize(fileLabelSize); 
        advOptsController.add(advOptShow);
        block.add(new JSeparator());
        block.add(advOptsController);
        block.add(advOptsBlock);  
        */
        
        this.add(scrollablePane);
    }
    
//-----------------------------------------------------------------------------
    
    /**
     * Imports parameters from a properly formatted parameters file.
     * The file is a text file with lines containing KEY=VALUE pairs.
     * @param fileName the pathname of the file to read
     * @throws Exception
     */
    
    @Override
    public void importParametersFromDenoptimParamsFile(String fileName) throws Exception
    {
    	importParametersFromDenoptimParamsFile(fileName,"FP-");
    	
    	rdbSrcOrNew.setSelected(false);
    	localBlock1.setVisible(false);
		localBlock2.setVisible(true);		
		if (rdbIntOrExt.isSelected())
		{
			localBlock3.setVisible(true);
			localBlock4.setVisible(false);
		}
		else
		{
			localBlock3.setVisible(false);
			localBlock4.setVisible(true);
		}
    }

//-----------------------------------------------------------------------------
    
  	@SuppressWarnings("unchecked")
	@Override
	public void importSingleParameter(String key, String value) throws Exception 
  	{
  		Object valueField;
  		String valueFieldClass;
  		if (mapKeyFieldToValueField.containsKey(key.toUpperCase()))
  		{
  		    valueField = mapKeyFieldToValueField.get(key.toUpperCase());
  		    valueFieldClass = valueField.getClass().toString();
  		}
  		else
  		{
			JOptionPane.showMessageDialog(null,
					"<html>Parameter '" + key + "' is not recognized<br> and will be ignored.</html>",
	                "WARNING",
	                JOptionPane.WARNING_MESSAGE,
	                UIManager.getIcon("OptionPane.errorIcon"));
			return;
  		}
    
 		switch (valueFieldClass)
 		{				
 			case "class javax.swing.JTextField":
 				((JTextField) valueField).setText(value);
 				
 				if (key.equals(keyFitProviderSource)) 
    			{
 				    rdbIntOrExt.setSelected(true);
    			}
 				break;
 				
 			case "class javax.swing.JRadioButton":
 				((JRadioButton) valueField).setSelected(true);
 				break;
 				
 			case "class javax.swing.JComboBox":
 				((JComboBox<String>) valueField).setSelectedItem(value);
 				break;
 				
 			case "class javax.swing.table.DefaultTableModel":

 				//WARNING: there might be cases where we do not take all the records

 				((DefaultTableModel) valueField).addRow(value.split(" "));
 				break;
 				
 			default:
 				throw new Exception("<html>Unexpected type for parameter: "  
 						+ key + " (" + valueFieldClass 
 						+ ").<br>Please report this to"
 						+ "the DEMOPTIM team.</html>");
 		}
	}
  	
//-----------------------------------------------------------------------------
  	
    @Override
    public void putParametersToString(StringBuilder sb) throws Exception
    {
        sb.append("# Fitness Provider - paramerers").append(NL);
        if (rdbIntOrExt.isSelected())
        {
	        sb.append(getStringIfNotEmpty(keyFitProviderSource,txtFitProviderSource));
	        sb.append(keyFitProviderInterpreter).append("=").append(cmbFitProviderInterpreter.getSelectedItem()).append(NL);
        }
        else
        {
        	sb.append(getStringIfNotEmpty(keyEq,txtEq));
        }
        //HEREGOESPRINT this is only to facilitate automated insertion of code       
    }
}
