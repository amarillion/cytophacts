package org.openphacts.cytophacts;

import java.awt.event.ActionEvent;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.work.ServiceProperties;
import org.cytoscape.work.TaskFactory;
import org.osgi.framework.BundleContext;

public class CyActivator extends AbstractCyActivator 
{
	private BundleContext context;
	private CySwingAppAdapter adapter;
	
	@Override
	public void start(BundleContext context) throws Exception 
	{
		System.out.println ("Initializing Cytophacts plugin");
		this.context = context;		
		
		adapter = getService(context, CySwingAppAdapter.class);

		registerMenu(context, "Apps.OpenPhacts", new CreateNetworkTask(adapter));
		registerMenu(context, "Apps.OpenPhacts", new OpenPhactsAction());
	}

	private class OpenPhactsAction extends AbstractAction
	{
		OpenPhactsAction()
		{
			super ("hello world");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			JOptionPane.showMessageDialog(null, "Hello OpenPhacts");
		}
	
	}
	
	public void registerMenu(BundleContext context, String parentMenu, AbstractAction action)
	{
			
		// Configure the service properties first.
		Properties properties = new Properties();

		// Our task should be exposed in the "Apps" menu...
		properties.put(ServiceProperties.PREFERRED_MENU,
			parentMenu);

		String title = "" + action.getValue(AbstractAction.NAME);
		
		// ... as a sub menu item called "Say Hello".
		properties.put(ServiceProperties.TITLE, title);

		// Our menu item should only be enabled if at least one network
		// view exists.
//		properties.put(ServiceProperties.ENABLE_FOR, "networkAndView");

		registerService(context,
			new ActionWrapper(action), // Implementation
			TaskFactory.class, // Interface
			properties); // Service properties
	
	}

}
