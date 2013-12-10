package de.shop.bestellverwaltung.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import de.shop.artikelverwaltung.domain.Artikel;

@XmlRootElement
public class Posten implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private long anzahl;
	
	private Artikel artikel;
	
	public long getAnzahl() {
		return anzahl;
	}

	public void setAnzahl(long anzahl) {
		this.anzahl = anzahl;
	}

	public Artikel getArtikel() {
		return artikel;
	}

	public void setArtikel(Artikel artikel) {
		this.artikel = artikel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (anzahl ^ (anzahl >>> 32));
		result = prime * result + ((artikel == null) ? 0 : artikel.hashCode());
		
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
		Posten other = (Posten) obj;
		if (anzahl != other.anzahl)
			return false;
		if (artikel == null) {
			if (other.artikel != null)
				return false;
		} else if (!artikel.equals(other.artikel))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "Posten [anzahl=" + anzahl + ", artikel=" + artikel
				+ "]";
	}
}