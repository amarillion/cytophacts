package org.openphacts.cytophacts;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.cytoscape.app.CyAppAdapter;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;

/**
 * Task to fetch network data from the OpenPhacts API.
 */
public class CreateNetworkTask extends AbstractAction 
{
	private CyAppAdapter adapter;
	
	public CreateNetworkTask(CySwingAppAdapter adapter) 
	{
		super ("load network");
		this.adapter = adapter;
	}
	
	private CyNetwork myNet;
	private Map<String, CyNode> idMap = new HashMap<String, CyNode>();
	
	/** returns the node for a given key, or creates a new one if it doesn't exist. */
	public CyNode createOrGet (String key)
	{
		if (idMap.containsKey(key))
		{
			return idMap.get(key);
		}
		else
		{
			CyNode node = myNet.addNode();
			idMap.put (key, node);
			return node;
		}
	}
	
	/** actually create the network */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		try
		{
			OpenPhactsMethods om = new OpenPhactsMethods();
			
			// Create an empty network
			myNet = adapter.getCyNetworkFactory().createNetwork();
			myNet.getRow(myNet).set(CyNetwork.NAME, "OpenPhacts");
			
			// Some hard-coded search values. TODO: use an input dialog. 
			String valylglucose = "CC(C)C(C(=O)O[C@@H](C=O)[C@H]([C@@H]([C@@H](CO)O)O)O)N";
			String aspirin = "CC(=O)Oc1ccccc1C(=O)O";
			String ethylPyruvate = "CCOC(=O)C(=O)C";
			List<String> compounds = om.getSubStructureSearch(ethylPyruvate);
			
			// TODO: look up the name of the compound instead of hard-coding
			String mainName = "Aspirin";
			CyNode nodeMainCompound = createOrGet(mainName);
			
			CyTable table = myNet.getDefaultNodeTable();
			
			// set name attribute for new nodes
			table.getRow(nodeMainCompound.getSUID()).set("name", mainName);
	
			int count = 0;
			for (String comp : compounds)
			{
				System.out.println ("Compound: " + comp);
				if (count++ >= 20) break; //Don't look further than top 20.
				
				// Add two nodes to the network
				CyNode nodeComp = createOrGet(comp);
				// set name attribute for new nodes
				myNet.getDefaultNodeTable().getRow(nodeComp.getSUID()).set("name", comp);
				
				// first get a list of compounds
				List<String> pwys = om.getPathwaysForCompound(comp, "Homo sapiens");
	
				// Add an edge
				myNet.addEdge(nodeMainCompound, nodeComp, true);
	
				for (String pwy : pwys)
				{
					// Add two nodes to the network
					CyNode nodePwy = createOrGet(pwy);
					
					// set name attribute for new nodes
					myNet.getDefaultNodeTable().getRow(nodePwy.getSUID()).set("name", pwy);
					
					// Add an edge
					myNet.addEdge(nodeComp, nodePwy, true);
				}
			
			}
					
			adapter.getCyNetworkManager().addNetwork(myNet);
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(null, "Exception: " + ex.getClass().getName() + " " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}
