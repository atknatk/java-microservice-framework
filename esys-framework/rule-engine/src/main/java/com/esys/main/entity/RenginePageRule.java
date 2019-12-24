 package com.esys.main.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "RENGINE_PAGE_RULE")
public class RenginePageRule implements Serializable {
	 		
		private static final long serialVersionUID = 3193640072349415215L;

		/**
		 * 
		 */
		@Id
		@SequenceGenerator(name = "RENGINE_PAGE_RULE_Id_GENERATOR", sequenceName = "RENGINE_PAGE_RULE_SEQ", allocationSize = 1)
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RENGINE_PAGE_RULE_Id_GENERATOR")
		@Column(name = "PAGE_RULE_ID")
		private Long pageRuleId;

		@Column(name = "PAGE_ID")
		private Long pageId;

		@Column(name = "RULE_NAME")
		private String ruleName;

		@Column(name = "RULE_TEXT")
		private String ruleText;	

		@Column(name = "ORDER_NO")
		private BigDecimal orderNo;

		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "CREATE_DATE")
		private Date createDate;

		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "UPDATE_DATE")
		private Date updateDate;
		
		@Column(name = "PLATFORM")
		private String platform;
		

		public Long getPageRuleId() {
			return pageRuleId;
		}

		public void setPageRuleId(Long pageRuleId) {
			this.pageRuleId = pageRuleId;
		}

		public String getRuleName() {
			return ruleName;
		}

		public void setRuleName(String ruleName) {
			this.ruleName = ruleName;
		}

		public String getRuleText() {
			return ruleText;
		}

		public void setRuleText(String ruleText) {
			this.ruleText = ruleText;
		}

		public BigDecimal getOrderNo() {
			return orderNo;
		}

		public void setOrderNo(BigDecimal orderNo) {
			this.orderNo = orderNo;
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
					+ ((pageRuleId == null) ? 0 : pageRuleId.hashCode());
			return result;
		}

		public Long getPageId() {
			return pageId;
		}

		public void setPageId(Long pageId) {
			this.pageId = pageId;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof RenginePageRule)) {
				return false;
			}
			RenginePageRule other = (RenginePageRule) obj;
			if (pageRuleId == null) {
				if (other.pageRuleId != null) {
					return false;
				}
			} else if (!pageRuleId.equals(other.pageRuleId)) {
				return false;
			}
			return true;
		}

}
