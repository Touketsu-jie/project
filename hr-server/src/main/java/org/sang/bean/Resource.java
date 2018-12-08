package org.sang.bean;

import org.sang.dto.ResourceDetailDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Resource  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private Long resourceId;
    private Long projectId;
    private String month;
    private String status;
    private Date createdDate;
    
    private String projectName;
	private String projectCode;
	private String jpProjectCode;


	private Long members;
    
    private BigDecimal resourceTotal;

	private BigDecimal resourceFeeTotal;


	private List<ResourceDetailDto> details;
    
	public Long getResourceId() {
		return resourceId;
	}
	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public Long getMembers() {
		return members;
	}
	public void setMembers(Long members) {
		this.members = members;
	}
	public BigDecimal getResourceTotal() {
		return resourceTotal;
	}
	public void setResourceTotal(BigDecimal resourceTotal) {
		this.resourceTotal = resourceTotal;
	}
	public List<ResourceDetailDto> getDetails() {
		return details;
	}
	public void setDetails(List<ResourceDetailDto> details) {
		this.details = details;
	}
	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getJpProjectCode() {
		return jpProjectCode;
	}

	public void setJpProjectCode(String jpProjectCode) {
		this.jpProjectCode = jpProjectCode;
	}

	public BigDecimal getResourceFeeTotal() {
		return resourceFeeTotal;
	}

	public void setResourceFeeTotal(BigDecimal resourceFeeTotal) {
		this.resourceFeeTotal = resourceFeeTotal;
	}
}