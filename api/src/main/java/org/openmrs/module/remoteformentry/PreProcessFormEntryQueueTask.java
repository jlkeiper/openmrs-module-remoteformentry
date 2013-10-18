package org.openmrs.module.remoteformentry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.scheduler.tasks.AbstractTask;

/**
 * Clean up the encounters+obs that are pointed to by these xml files
 * 
 * @see RemoteFormEntryCleanupProcessor
 */
public class PreProcessFormEntryQueueTask extends AbstractTask {

	// Logger
	private static Log log = LogFactory.getLog(PreProcessFormEntryQueueTask.class);

	// Instance of form processor
	private FormEntryQueuePreProcessor processor = null;

	/**
	 *
	 */
	public PreProcessFormEntryQueueTask() {
		if (processor == null)
			processor = new FormEntryQueuePreProcessor();
	}
	
	/**
	 * Process the next form entry in the database and then remove the form
	 * entry from the database.
	 */
	public void execute() {
		Context.openSession();
		log.debug("Pre-processing form entry queue ... ");
		try {
			if (Context.isAuthenticated() == false)
				authenticate();
			// do the processing
			processor.preProcessFormEntryQueue();
			
		} catch (APIException e) {
			log.error("Error processing form entry queue", e);
			throw e;
		}
		catch (Exception e) {
			log.error("Error while processing form entry queue", e);
		}
		finally {
			Context.closeSession();
		}
	}
	
	/**
	 * Clean up any resources here
	 *
	 */
	public void shutdown() {
		
	}
	
}
