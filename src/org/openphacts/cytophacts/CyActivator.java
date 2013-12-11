package org.openphacts.cytophacts;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CyNodeViewContextMenuFactory;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyTableManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.work.ServiceProperties;
import org.cytoscape.work.TaskFactory;
import org.osgi.framework.BundleContext;
import org.xml.sax.InputSource;

public class CyActivator extends AbstractCyActivator 
{
	private BundleContext context;
	
	@Override
	public void start(BundleContext context) throws Exception 
	{
		System.out.println ("Initializing Cytophacts plugin");
		this.context = context;		
	}

}
