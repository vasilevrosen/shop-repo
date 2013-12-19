package de.shop.bestellverwaltung.service;

import java.util.List;
import java.util.Locale;

import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.kundenverwaltung.domain.Kunde;

public interface BestellungService {
	List<Bestellung> findBestellungenByKunde(Kunde kunde);
	Bestellung createBestellung(Bestellung bestellung, Kunde kunde, Locale locale);
	Bestellung createBestellung(Bestellung bestellung, Kunde kunde);
	void updateBestellung(Bestellung bestellung);
	Bestellung findBestellungById(Long id);
}