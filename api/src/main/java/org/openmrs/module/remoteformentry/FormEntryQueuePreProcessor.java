/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.remoteformentry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.formentry.FormEntryQueue;
import org.openmrs.module.formentry.FormEntryQueueProcessor;
import org.openmrs.module.formentry.FormEntryService;

public class FormEntryQueuePreProcessor {

	private final Log log = LogFactory.getLog(getClass());

	private static Boolean isRunning = false; // allow only one running

	/**
	 * Starts up a thread to process all existing FormEntryQueue entries
	 */
	public void preProcessFormEntryQueue() throws APIException {
		synchronized (isRunning) {
			if (isRunning) {
				log.warn("FormEntryQueue pre-processor aborting (another processor already running)");
				return;
			}
			isRunning = true;
		}
		try {
			log.debug("Start pre-processing FormEntry queue");
			log.debug("RemoteFormEntry pre-processor hash: " + this.hashCode());
			while (preProcessNextFormEntryQueue()) {
				// loop until queue is empty
			}
			log.debug("Done pre-processing FormEntry queue");
		} finally {
			isRunning = false;
		}
	}

	private boolean preProcessNextFormEntryQueue() {
		boolean preProcessOccurred = false;
		FormEntryService fes;
		try {
			fes = Context.getService(FormEntryService.class);
		}
		catch (APIException e) {
			log.debug("FormEntryService not found");
			return false;
		}

		FormEntryQueue feq;
		if ((feq = fes.getNextFormEntryQueue()) != null) {
			preProcessFormEntryQueue(feq);
			preProcessOccurred = true;
		}
		return preProcessOccurred;

	}

	private void preProcessFormEntryQueue(FormEntryQueue feq) {
		// make a copy of the form data
		Context.getService(RemoteFormEntryService.class).copyFormEntryQueueToOutbound(feq);

		// send through the original route
		new FormEntryQueueProcessor().transformFormEntryQueue(feq);
	}


}
