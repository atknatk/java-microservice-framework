 package com.esys.main.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "RENGINE_ACTION")
public class RengineAction implements Serializable {
	 		
		private static final long serialVersionUID = 3193640072349415215L;

		/**
		 * 
		 */
		@Id
		@SequenceGenerator(name = "RENGINE_ACTION_Id_GENERATOR", sequenceName = "RENGINE_ACTION_SEQ", allocationSize = 1)
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RENGINE_ACTION_Id_GENERATOR")
		@Column(name = "ACTION_ID")
		private Long actionId;

		@Column(name = "ACTION_NAME")
		private String actionName;

		@Column(name = "PAGE_RULE_ID")
		private Long pageRuleId;

		@Column(name = "PAGE_ID")
		private Long pageId;
	
     	@Column(name = "ACTION_TYPE")
		private String actionType;
		
		@Column(name = "ACTION_BODY")
		private String actionBody;	
		
		@Column(name = "SEVERITY")
		private String severity;
		
		@Column(name = "HTTP_METHOD")
		private String httpMethod;
		
		@Column(name = "URL")
		private String url;
		
		@Column(name = "CONTENT_TYPE")
		private String contentType;
		
		@OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER, orphanRemoval = true)
		@JoinColumn(name = "ACTION_ID",referencedColumnName="ACTION_ID")
		private List<RengineActionHeaders> headers;
		
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "CREATE_DATE")
		private Date createDate;
		
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "UPDATE_DATE")
		private Date updateDate;
		
		@Column(name = "PLATFORM")
		private String platform;

		public Long getActionId() {
			return actionId;
		}

		public void setActionId(Long actionId) {
			this.actionId = actionId;
		}

		public String getActionName() {
			return actionName;
		}

		public void setActionName(String actionName) {
			this.actionName = actionName;
		}

		public String getActionType() {
			return actionType;
		}

		public void setActionType(String actionType) {
			this.actionType = actionType;
		}

		public String getActionBody() {
			return actionBody;
		}

		public void setActionBody(String actionBody) {
			this.actionBody = actionBody;
		}

		public String getHttpMethod() {
			return httpMethod;
		}

		public void setHttpMethod(String httpMethod) {
			this.httpMethod = httpMethod;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public List<RengineActionHeaders> getHeaders() {
			return headers;
		}

		public void setHeaders(List<RengineActionHeaders> headers) {
			this.headers = headers;
		}

		public Date getCreateDate() {
			return createDate;
		}

		public void setCreateDate(Date createDate) {
			this.createDate = createDate;
		}

		public Date getUpdateDate() {
			return updateDate;
		}

		public void setUpdateDate(Date updateDate) {
			this.updateDate = updateDate;
		}

		public String getPlatform() {
			return platform;
		}

		public void setPlatform(String platform) {
			this.platform = platform;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((actionId == null) ? 0 : actionId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof RengineAction)) {
				return false;
			}
			RengineAction other = (RengineAction) obj;
			if (actionId == null) {
				if (other.actionId != null) {
					return false;
				}
			} else if (!actionId.equals(other.actionId)) {
				return false;
			}
			return true;
		}

		public Long getPageRuleId() {
			return pageRuleId;
		}

		public void setPageRuleId(Long pageRuleId) {
			this.pageRuleId = pageRuleId;
		}

		public Long getPageId() {
			return pageId;
		}

		public void setPageId(Long pageId) {
			this.pageId = pageId;
		}

		public String getSeverity() {
			return severity;
		}

		public void setSeverity(String severity) {
			this.severity = severity;
		}
}
