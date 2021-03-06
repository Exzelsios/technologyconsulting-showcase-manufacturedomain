package de.novatec.showcase.manufacture.dto;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

import de.novatec.showcase.manufacture.GlobalConstants;

public class PurchaseOrder {

	private int poNumber;

	@JsonFormat(pattern = GlobalConstants.DATE_FORMAT, locale = "de_DE")
	private Date sentDate;

	private int siteId;

	@JsonFormat(pattern = GlobalConstants.DATE_FORMAT, locale = "de_DE")
	private Timestamp startDate;

	private Integer supplierId;

	private Integer version;

	private Collection<PurchaseOrderLine> purchaseOrderlines;

	public PurchaseOrder() {
	}

	public int getPoNumber() {
		return this.poNumber;
	}

	public void setPoNumber(int poNumber) {
		this.poNumber = poNumber;
	}

	public Date getSentDate() {
		return this.sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public int getSiteId() {
		return this.siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Integer getSupplierId() {
		return this.supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Collection<PurchaseOrderLine> getPurchaseOrderlines() {
		return this.purchaseOrderlines;
	}

	public void setPurchaseOrderlines(Collection<PurchaseOrderLine> purchaseOrderlines) {
		this.purchaseOrderlines = purchaseOrderlines;
	}

	@Override
	public String toString() {
		return "PurchaseOrder [poNumber=" + poNumber + ", sentDate=" + sentDate + ", siteId=" + siteId + ", startDate="
				+ startDate + ", supplierId=" + supplierId + ", version=" + version + ", purchaseOrderlines="
				+ purchaseOrderlines + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(purchaseOrderlines, poNumber, sentDate, siteId, startDate, supplierId, version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PurchaseOrder)) {
			return false;
		}
		PurchaseOrder other = (PurchaseOrder) obj;
		return Objects.equals(purchaseOrderlines, other.purchaseOrderlines) && poNumber == other.poNumber
				&& Objects.equals(sentDate, other.sentDate) && siteId == other.siteId
				&& Objects.equals(startDate, other.startDate) && supplierId == other.supplierId
				&& version == other.version;
	}
}