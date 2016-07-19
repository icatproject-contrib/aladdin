package org.icatproject.core.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

@Comment("A parameter associated with a data file")
@SuppressWarnings("serial")
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "DATAFILE_ID", "PARAMETER_TYPE_ID" }) })
@XmlRootElement
public class DatafileParameter extends Parameter implements Serializable {

	@Comment("The associated data file")
	@JoinColumn(name = "DATAFILE_ID", nullable = false)
	@ManyToOne
	private Datafile datafile;

	/* Needed for JPA */
	public DatafileParameter() {
	}

	public Datafile getDatafile() {
		return this.datafile;
	}

	public void setDatafile(Datafile datafile) {
		this.datafile = datafile;
	}

}
