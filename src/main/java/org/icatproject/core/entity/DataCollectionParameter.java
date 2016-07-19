package org.icatproject.core.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Comment("A parameter associated with a DataCollection")
@SuppressWarnings("serial")
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "DATACOLLECTION_ID", "PARAMETER_TYPE_ID" }) })
public class DataCollectionParameter extends Parameter implements Serializable {

	@Comment("The associated DataCollection")
	@JoinColumn(name = "DATACOLLECTION_ID", nullable = false)
	@ManyToOne
	private DataCollection dataCollection;

	/* Needed for JPA */
	public DataCollectionParameter() {
	}

	public DataCollection getDataCollection() {
		return dataCollection;
	}

	public void setDataCollection(DataCollection dataCollection) {
		this.dataCollection = dataCollection;
	}

}