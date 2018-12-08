package org.sang.dto;

import java.io.Serializable;
import java.util.List;

import org.sang.bean.ResourceDetail;

public class ResourceDetailDto extends ResourceDetail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
  
    private String[] dateRange;

	public String[] getDateRange() {
		return dateRange;
	}

	public void setDateRange(String[] dateRange) {
		this.dateRange = dateRange;
	}
    
	
}