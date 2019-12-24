 package com.esys.main.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "RENGINE_ACTION_HEADERS")
public class RengineActionHeaders implements Serializable {
	 		
		private static final long serialVersionUID = 3193640072349415215L;

		/**
		 * 
		 */
		@Id
		@SequenceGenerator(name = "RENGINE_ACTION_HEADERS_Id_GENERATOR", sequenceName = "RENGINE_ACTION_HEADERS_SEQ", allocationSize = 1)
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RENGINE_ACTION_HEADERS_Id_GENERATOR")
		@Column(name = "HEADER_ID")
		private Long headerId;

		@Column(name = "HEADER_NAME")
		private String headerName;

		@Column(name = "HEADER_VALUE")
		private String headerValue;

		@Column(name = "ACTION_ID")
		private Long actionId;

		public Long getHeaderId() {
			return headerId;
		}

		public void setHeaderId(Long headerId) {
			this.headerId = headerId;
		}

		public String getHeaderName() {
			return headerName;
		}

		public void setHeaderName(String headerName) {
			this.headerName = headerName;
		}

		public String getHeaderValue() {
			return headerValue;
		}

		public void setHeaderValue(String headerValue) {
			this.headerValue = headerValue;
		}

		public Long getActionId() {
			return actionId;
		}

		public void setActionId(Long actionId) {
			this.actionId = actionId;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((headerId == null) ? 0 : headerId.hashCode());
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
			if (!(obj instanceof RengineActionHeaders)) {
				return false;
			}
			RengineActionHeaders other = (RengineActionHeaders) obj;
			if (headerId == null) {
				if (other.headerId != null) {
					return false;
				}
			} else if (!headerId.equals(other.headerId)) {
				return false;
			}
			return true;
		}


}
