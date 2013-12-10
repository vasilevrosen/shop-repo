package de.shop.artikelverwaltung.service;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.validation.constraints.NotNull;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.util.interceptor.Log;
import de.shop.util.Mock;

@Dependent
@Log
public class ArtikelService implements Serializable {
	private static final long serialVersionUID = -5105686816948437276L;

	@NotNull(message = "{artikel.notFound.id}")
	public Artikel findArtikelById(Long id) {
		// TODO Datenbanzugriffsschicht statt Mock
		return Mock.findArtikelById(id);
	}
	
	@NotNull(message = "{artikel.notFound.id}")
	public Artikel createArtikel(Artikel artikel) {
		// TODO Datenbanzugriffsschicht statt Mock
		return Mock.createArtikel(artikel);
	}
	
	@NotNull(message = "{artikel.notFound.id}")
	public Artikel updateArtikel(Artikel artikel) {
		// TODO Datenbanzugriffsschicht statt Mock
		return Mock.createArtikel(artikel);
	}
}