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
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "RENGINE_PAGE")
public class RenginePage implements Serializable {
	 		
		private static final long serialVersionUID = 3193640072349415215L;

		/**
		 * 
		 */
		@Id
		@SequenceGenerator(name = "RENGINE_PAGE_Id_GENERATOR", sequenceName = "RENGINE_PAGE_SEQ", allocationSize = 1)
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RENGINE_PAGE_Id_GENERATOR")
		@Column(name = "PAGE_ID")
		private Long pageId;

		@Column(name = "PAGE_NAME")
		private String pageName;

		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "CREATE_DATE")
		private Date createDate;

		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "UPDATE_DATE")
		private Date updateDate;
		
		@Column(name = "PLATFORM")
		private String platform;

		@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY, orphanRemoval = true)
		@JoinColumn(name = "PAGE_ID",referencedColumnName="PAGE_ID")
		@OrderBy("ORDER_NO")
		private List<RenginePageRule> ruleList;
		
		
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

		public Long getPageId() {
			return pageId;
		}

		public void setPageId(Long pageId) {
			this.pageId = pageId;
		}

		public Date getCreateDate() {
			return createDate;
		}

		public void setCreateDate(Date createDate) {
			this.createDate = createDate;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((pageId == null) ? 0 : pageId.hashCode());
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
			if (!(obj instanceof RenginePage)) {
				return false;
			}
			RenginePage other = (RenginePage) obj;
			if (pageId == null) {
				if (other.pageId != null) {
					return false;
				}
			} else if (!pageId.equals(other.pageId)) {
				return false;
			}
			return true;
		}

		public String getPageName() {
			return pageName;
		}

		public void setPageName(String pageName) {
			this.pageName = pageName;
		}

		public List<RenginePageRule> getRuleList() {
			return ruleList;
		}

		public void setRuleList(List<RenginePageRule> ruleList) {
			this.ruleList = ruleList;
		}

}
