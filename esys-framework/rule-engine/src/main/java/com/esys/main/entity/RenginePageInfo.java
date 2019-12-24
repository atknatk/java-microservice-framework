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
@Table(name = "RENGINE_PAGE_INFO")
public class RenginePageInfo implements Serializable {
	 		
		private static final long serialVersionUID = 3193640072349415215L;

		/**
		 * 
		 */
		@Id
		@SequenceGenerator(name = "RENGINE_PAGE_INFO_Id_GENERATOR", sequenceName = "RENGINE_PAGE_INFO_SEQ", allocationSize = 1)
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RENGINE_PAGE_INFO_Id_GENERATOR")
		@Column(name = "PAGE_INFO_ID")
		private Long pageInfoId;

		@Column(name = "FIELD_NAME")
		private String fieldName;

		@Column(name = "PAGE_ID")
		private Long pageId;
		
		@Column(name = "DATA_TYPE")
		private String dataType;

		@Column(name = "DEFAULT_VALUE")
		private String defaultValue;

		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "CREATE_DATE")
		private Date createDate;

		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "UPDATE_DATE")
		private Date updateDate;
		
		@Column(name = "PLATFORM")
		private String platform;
		
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

		public Long getPageInfoId() {
			return pageInfoId;
		}

		public void setPageInfoId(Long pageInfoId) {
			this.pageInfoId = pageInfoId;
		}

		public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

		public String getDefaultValue() {
			return defaultValue;
		}

		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
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
					+ ((pageInfoId == null) ? 0 : pageInfoId.hashCode());
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
			if (!(obj instanceof RenginePageInfo)) {
				return false;
			}
			RenginePageInfo other = (RenginePageInfo) obj;
			if (pageInfoId == null) {
				if (other.pageInfoId != null) {
					return false;
				}
			} else if (!pageInfoId.equals(other.pageInfoId)) {
				return false;
			}
			return true;
		}

		public Long getPageId() {
			return pageId;
		}

		public void setPageId(Long pageId) {
			this.pageId = pageId;
		}

		
}
