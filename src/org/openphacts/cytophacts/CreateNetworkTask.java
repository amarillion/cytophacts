package org.openphacts.cytophacts;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.cytoscape.app.CyAppAdapter;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

public class CreateNetworkTask extends AbstractAction 
{
	private CyAppAdapter adapter;
	
	public CreateNetworkTask(CySwingAppAdapter adapter) 
	{
		super ("load network");
		this.adapter = adapter;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// Create an empty network
		CyNetwork myNet = adapter.getCyNetworkFactory().createNetwork();
		myNet.getRow(myNet).set(CyNetwork.NAME, "My network");
		
		// Add two nodes to the network
		CyNode node1 = myNet.addNode();
		CyNode node2 = myNet.addNode();
		
		// set name attribute for new nodes
		myNet.getDefaultNodeTable().getRow(node1.getSUID()).set("name", "Node1");
		myNet.getDefaultNodeTable().getRow(node2.getSUID()).set("name", "Node2");
		
		// Add an edge
		myNet.addEdge(node1, node2, true);
				
		adapter.getCyNetworkManager().addNetwork(myNet);
		
		//// The following code will destroy the network
		boolean destroyNetwork = false;
		if (destroyNetwork){
			// Destroy it
			 adapter.getCyNetworkManager().destroyNetwork(myNet);			
		}	
	}
}
