package de.shop.bestellverwaltung.domain;

import java.io.Serializable;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.NotEmpty;

import de.shop.kundenverwaltung.domain.Kunde;

import static de.shop.util.Constants.KEINE_ID;

@XmlRootElement
@Entity
@Table(indexes = {
		@Index(columnList = "kunde_fk"),
		@Index(columnList = "erzeugt")
})

//TODO @NamedQueries

//TODO @NamedEntityGraphs

@Cacheable
public class Bestellung implements Serializable {
	
private static final long serialVersionUID = 1618359234119003714L;
	
	@Id
	@GeneratedValue
	@Basic(optional = false)
	private Long id = KEINE_ID;
	
	private boolean ausgeliefert;	

	@ManyToOne
	@JoinColumn(name = "kunde_fk", nullable = false, 
		insertable = false, updatable = false)
	@XmlTransient
	private Kunde kunde;
	
	@OneToMany
	@JoinColumn(name = "bestellung_fk", nullable = false)
	@NotEmpty(message = "{bestellung.posten.notEmpty}")
	@Valid
	private List<Posten> posten;
	
	@Transient
	private URI kundeUri;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isAusgeliefert() {
		return ausgeliefert;
	}

	public void setAusgeliefert(boolean ausgeliefert) {
		this.ausgeliefert = ausgeliefert;
	}

	public Kunde getKunde() {
		return kunde;
	}

	public void setKunde(Kunde kunde) {
		this.kunde = kunde;
	}

	public List<Posten> getPosten() {
		return Collections.unmodifiableList(posten);
	}

	@OneToMany
	@JoinColumn(name = "bestellung_fk", nullable = false)
	public void setPosten(List<Posten> posten) {
		if (this.posten == null) {
			this.posten = posten;
			return;
		}
		this.posten.clear();
		if (posten != null) {
			this.posten.addAll(posten);
		}
	}

	public URI getKundeUri() {
		return kundeUri;
	}

	public void setKundeUri(URI kundeUri) {
		this.kundeUri = kundeUri;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (ausgeliefert ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((kunde == null) ? 0 : kunde.hashCode());
		result = prime * result
				+ ((kundeUri == null) ? 0 : kundeUri.hashCode());
		result = prime * result + ((posten == null) ? 0 : posten.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Bestellung other = (Bestellung) obj;
		
		if (ausgeliefert != other.ausgeliefert)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} 
		else if (!id.equals(other.id))
			return false;
		if (kunde == null) {
			if (other.kunde != null)
				return false;
		}
		else if (!kunde.equals(other.kunde))
			return false;
		if (kundeUri == null) {
			if (other.kundeUri != null)
				return false;
		}
		else if (!kundeUri.equals(other.kundeUri))
			return false;
		if (posten == null) {
			if (other.posten != null)
				return false;
		}
		else if (!posten.equals(other.posten))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Bestellung [id=" + id + ", ausgeliefert=" + ausgeliefert
			 + ", kunde=" + kunde
				+ ", posten=" + posten + ", kundeUri=" + kundeUri + "]";
	}
}