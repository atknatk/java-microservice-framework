 package com.esys.main.entity;

import java.io.Serializable;
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
@Table(name = "RENGINE_SQL_RESULT_SET")
public class RengineSqlResultSet implements Serializable {
	 		
		private static final long serialVersionUID = 3193640072349415215L;

		/**
		 * 
		 */
		@Id
		@SequenceGenerator(name = "RENGINE_SQL_RESULT_SET_Id_GENERATOR", sequenceName = "RENGINE_SQL_RESULT_SET_SEQ", allocationSize = 1)
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RENGINE_SQL_RESULT_SET_Id_GENERATOR")
		@Column(name = "SQL_ID")
		private Long sqlId;
		
		@Column(name = "SQL_TXT")
		private String sql;
		
		@Column(name = "RESULT_SET_NAME")
		private String resultSetName;

		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "CREATE_DATE")
		private Date createDate;

		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "UPDATE_DATE")
		private Date updateDate;
		
		@Column(name = "PLATFORM")
		private String platform;

		@Column(name = "PAGE_ID")
		private Long pageId;
		
		@Column(name = "PAGE_RULE_ID")
		private Long pageRuleId;
		
		public Long getSqlId() {
			return sqlId;
		}

		public void setSqlId(Long sqlId) {
			this.sqlId = sqlId;
		}

		public Long getPageId() {
			return pageId;
		}

		public Long getPageRuleId() {
			return pageRuleId;
		}

		public void setPageId(Long pageId) {
			this.pageId = pageId;
		}

		public void setPageRuleId(Long pageRuleId) {
			this.pageRuleId = pageRuleId;
		}

		public String getSql() {
			return sql;
		}

		public void setSql(String sql) {
			this.sql = sql;
		}

		public String getResultSetName() {
			return resultSetName;
		}

		public void setResultSetName(String resultSetName) {
			this.resultSetName = resultSetName;
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
					+ ((sqlId == null) ? 0 : sqlId.hashCode());
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
			if (!(obj instanceof RengineSqlResultSet)) {
				return false;
			}
			RengineSqlResultSet other = (RengineSqlResultSet) obj;
			if (sqlId == null) {
				if (other.sqlId != null) {
					return false;
				}
			} else if (!sqlId.equals(other.sqlId)) {
				return false;
			}
			return true;
		}


}
