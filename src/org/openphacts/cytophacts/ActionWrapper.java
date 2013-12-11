package org.openphacts.cytophacts;

import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;

import org.cytoscape.work.Task;
import org.cytoscape.work.TaskFactory;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskMonitor;

/**
 * Wrapper for an AbstractAction - turns the AbstractAction into a Cytoscape Task,
 * so it can be registered as a menu item.
 */
public class ActionWrapper implements TaskFactory 
{
	@Override
	public boolean isReady() {
		// This method lets the factory do its own sanity checks to verify
		// it's ready to create and run its tasks.
		return true;
	}

	private final AbstractAction parent;
	
	public ActionWrapper (AbstractAction parent)
	{
//		super("" + parent.getValue(AbstractAction.NAME), cyApplicationManager, null, null);
		this.parent = parent;
//		setPreferredMenu("Apps.MARRS");
	}

	private class TaskWrapper implements Task
	{
		@Override
		public void cancel() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void run(TaskMonitor arg0) throws Exception 
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run() {
					parent.actionPerformed(null);					
				}	
			});
		}
	}
	
	@Override
	public TaskIterator createTaskIterator() 
	{
		return new TaskIterator(new TaskWrapper());
	}


}
