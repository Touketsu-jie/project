package org.sang.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class ResourceDetail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long resourceDetailId;
    private Long resourceId;
    private BigDecimal resource;
    private Long employeeId;
    private String startDate;
    private String endDate;
	private String resourceLevel;
	private String employeeName;
    
	public Long getResourceDetailId() {
		return resourceDetailId;
	}
	public void setResourceDetailId(Long resourceDetailId) {
		this.resourceDetailId = resourceDetailId;
	}
	public Long getResourceId() {
		return resourceId;
	}
	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}
	public BigDecimal getResource() {
		return resource;
	}
	public void setResource(BigDecimal resource) {
		this.resource = resource;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getResourceLevel() {
		return resourceLevel;
	}

	public void setResourceLevel(String resourceLevel) {
		this.resourceLevel = resourceLevel;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
}