package org.openmrs.module.remoteformentry;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.module.formentry.FormEntryQueue;
import org.openmrs.util.OpenmrsUtil;

/**
 * Object holding the metadata around the a remote form entry
 * pending queue
 */
public class RemoteFormEntryPendingQueue {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private Integer remoteFormEntryPendingQueueId;
	private String formData;
	private User creator;
	private Date dateCreated;
	
	private String fileSystemUrl;
	
	
	/**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
    	if (!(obj instanceof RemoteFormEntryPendingQueue))
    		return false;
    	
    	RemoteFormEntryPendingQueue other = (RemoteFormEntryPendingQueue)obj;
    	
    	if (other.getRemoteFormEntryPendingQueueId() != null && getRemoteFormEntryPendingQueueId() != null)
    		return other.getRemoteFormEntryPendingQueueId().equals(remoteFormEntryPendingQueueId);
    	
    	if (other.getFormData() != null && getFormData() != null)
    		return other.getFormData().equals(formData);
    	
    	if (other.getFileSystemUrl() != null && getFileSystemUrl() != null)
    		return other.getFileSystemUrl().equals(fileSystemUrl);
    	
    	return false;
    }

	/**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
    	int hash = 7;
    	if (getRemoteFormEntryPendingQueueId() != null)
    		return hash += 3*remoteFormEntryPendingQueueId.hashCode();
    	
    	if (getFormData() != null)
    		return hash += 5 * formData.hashCode();
    	
    	if (getFileSystemUrl() != null)
    		return hash += 7 * fileSystemUrl.hashCode();
    	
    	return hash;
    }

	/**
	 * @return Returns the RemoteFormEntryPendingQueueId.
	 */
	public Integer getRemoteFormEntryPendingQueueId() {
		return remoteFormEntryPendingQueueId;
	}

	/**
	 * @param RemoteFormEntryPendingQueueId
	 *            The RemoteFormEntryPendingQueueId to set.
	 */
	public void setRemoteFormEntryPendingQueueId(Integer remoteFormEntryPendingQueueId) {
		this.remoteFormEntryPendingQueueId = remoteFormEntryPendingQueueId;
	}

	/**
	 * Gets the xml that this queue item holds.  If formData is null
	 * and fileSystemUrl is not null, the data is "lazy loaded" from
	 * the filesystem
	 * 
	 * @return Returns the formData.
	 */
	public String getFormData() {
		
		if (formData == null && fileSystemUrl != null) {
			// lazy load the form data from the filesystem
			
			File file = new File(fileSystemUrl);
			
			if (file.exists()) {
				try {
					formData = OpenmrsUtil.getFileAsString(file);
					return formData;
				}
				catch (IOException io) {
					log.warn("Unable to lazy load the formData from: " + fileSystemUrl, io);
				}
			}
			else {
				log.warn("File system url does not exist for formentry queue item.  Url: '" + fileSystemUrl + "'");
			}
				
		}
		
		return formData;
	}

	/**
	 * @param formData
	 *            The formData to set.
	 */
	public void setFormData(String formData) {
		this.formData = formData;
	}

	/**
	 * @return Returns the creator.
	 */
	public User getCreator() {
		return creator;
	}

	/**
	 * @param creator
	 *            The creator to set.
	 */
	public void setCreator(User creator) {
		this.creator = creator;
	}

	/**
	 * @return Returns the dateCreated.
	 */
	public Date getDateCreated() {
		return dateCreated;
	}

	/**
	 * @param dateCreated
	 *            The dateCreated to set.
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	/**
	 * Used when the queue items are stored on the server filesystem.
	 * 
     * @return the fileSystemUrl
     */
    public String getFileSystemUrl() {
    	return fileSystemUrl;
    }

	/**
	 * Used when the queue items are stored on the server filesystem
     * @param fileSystemUrl the fileSystemUrl to set
     */
    public void setFileSystemUrl(String fileSystemUrl) {
    	this.fileSystemUrl = fileSystemUrl;
    }

	/**
     * Transform this remote form entry pending queue into a normal
     * form entry queue so it can be processed normally
     * 
     * @return new FormEntryQueue to be saved
     */
    public FormEntryQueue getFormEntryQueue() {
    	FormEntryQueue queue = new FormEntryQueue();
		queue.setFormData(getFormData());
		queue.setCreator(getCreator());
		queue.setDateCreated(getDateCreated());
		
		return queue;
    }
	
	

}
