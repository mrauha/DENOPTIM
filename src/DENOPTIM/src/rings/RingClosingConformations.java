package rings;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

import molecule.DENOPTIMVertex;

/**
 * Serializable object to store/get a list of conformations that allow to close
 * a ring from an open chain of atoms.
 * Fragment ID and Attachment point ID are used to identify the chain.
 * Each closable conformation is recorded as the ordered vector of dihedral 
 * angles calculated from the reference points is the chain. Note that the
 * angles do NOT necessarily refer to the torsion in the chain, infact they
 * constitue a standardized representation of the conformation of a bond
 * and can be used to compare different chains that involve the same bond.
 * For instance, interdependent non-same chains of atoms. The definition of 
 * the standard reference points for the calculation of the dihedral angles 
 * is performed in the parent object representing the chain. 
 * See {@link #rings.PathSubGraph  here}.
 *
 * @author Marco Foscato 
 */

public class RingClosingConformations implements Serializable
{
    /**
     * The identifier of the chain
     */
    private String chainID;

    /**
     * List of conformations (each as list of dihedrals)
     */
    private ArrayList<ArrayList<Double>> lstConfs;

//-----------------------------------------------------------------------------

    /**
     *  Constructs an empty RingClosure 
     */

    public RingClosingConformations()
    {
	this.chainID = "";
	this.lstConfs = new ArrayList<ArrayList<Double>>();
    }

//-----------------------------------------------------------------------------

    /**
     * Constructs a RingClosure from data
     * @param chainID the computer-generated string identifier of the chain
     * @param lstConfs the list of closable conformations 
     */

    public RingClosingConformations(String chainID, 
                                        ArrayList<ArrayList<Double>> lstConfs)
    {
        this.chainID = chainID;
        this.lstConfs = lstConfs;
    }

//-----------------------------------------------------------------------------

    public String getChainID()
    {
        return chainID;
    }

//-----------------------------------------------------------------------------

    public int getNumberOfConformations()
    {
	return lstConfs.size();
    }

//-----------------------------------------------------------------------------

    public ArrayList<ArrayList<Double>> getListOfConformations()
    {
        return lstConfs;
    }

//-----------------------------------------------------------------------------

    @Override
    public String toString()
    {
        return "RingClosingConformations [chainID=" + chainID 
                                + ", lstConfs=" + lstConfs 
				+ "]";
    }

//-----------------------------------------------------------------------------
}
