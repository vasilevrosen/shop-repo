package de.shop.util;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.logging.Logger;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.bestellverwaltung.domain.Posten;
import de.shop.kundenverwaltung.domain.AbstractKunde;
import de.shop.kundenverwaltung.domain.Adresse;
import de.shop.kundenverwaltung.domain.Firmenkunde;
import de.shop.kundenverwaltung.domain.HobbyType;
import de.shop.kundenverwaltung.domain.Privatkunde;

/**
 * Emulation des Anwendungskerns
 */
public final class Mock {
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	private static final int MAX_ID = 99;
	private static final int MAX_KUNDEN = 8;
	private static final int MAX_BESTELLUNGEN = 4;
	
	private static final int JAHR = 2001;
	private static final int MONAT = 0; // bei Calendar werden die Monate von 0 bis 11 gezaehlt
	private static final int TAG = 31;  // bei Calendar die Monatstage ab 1 gezaehlt

	public static AbstractKunde findKundeById(Long id) {
		
		if (id > MAX_ID) {
			return null;
		}
		
		final AbstractKunde kunde = id % 2 == 1 ? new Privatkunde() : new Firmenkunde();
		kunde.setId(id);
		kunde.setNachname("Nachname");
		kunde.setEmail("" + id + "@hska.de");
		final GregorianCalendar seitCal = new GregorianCalendar(JAHR, MONAT, TAG);
		final Date seit = seitCal.getTime();
		kunde.setSeit(seit);
		
		final Adresse adresse = new Adresse();
		adresse.setId(id + 1);        // andere ID fuer die Adresse
		adresse.setPlz("12345");
		adresse.setOrt("Testort");
		adresse.setKunde(kunde);
		kunde.setAdresse(adresse);
		
		if (kunde.getClass().equals(Privatkunde.class)) {
			final Privatkunde privatkunde = (Privatkunde) kunde;
			final Set<HobbyType> hobbies = new HashSet<>();
			hobbies.add(HobbyType.LESEN);
			hobbies.add(HobbyType.REISEN);
			privatkunde.setHobbies(hobbies);
		}
		
		return kunde;
	}
	
	public static AbstractKunde findKundeByEmail(String email) {
		if (email.startsWith("x")) {
			return null;
		}
		
		final AbstractKunde kunde = email.length() % 2 == 1 ? new Privatkunde() : new Firmenkunde();
		kunde.setId(Long.valueOf(email.length()));
		kunde.setNachname("Nachname");
		kunde.setEmail(email);
		final GregorianCalendar seitCal = new GregorianCalendar(JAHR, MONAT, TAG);
		final Date seit = seitCal.getTime();
		kunde.setSeit(seit);
		
		final Adresse adresse = new Adresse();
		adresse.setId(kunde.getId() + 1);        // andere ID fuer die Adresse
		adresse.setPlz("12345");
		adresse.setOrt("Testort");
		adresse.setKunde(kunde);
		kunde.setAdresse(adresse);
		
		if (kunde.getClass().equals(Privatkunde.class)) {
			final Privatkunde privatkunde = (Privatkunde) kunde;
			final Set<HobbyType> hobbies = new HashSet<>();
			hobbies.add(HobbyType.LESEN);
			hobbies.add(HobbyType.REISEN);
			privatkunde.setHobbies(hobbies);
		}

		return kunde;
	}

	public static List<AbstractKunde> findAllKunden() {
		final int anzahl = MAX_KUNDEN;
		final List<AbstractKunde> kunden = new ArrayList<>(anzahl);
		for (int i = 1; i <= anzahl; i++) {
			final AbstractKunde kunde = findKundeById(Long.valueOf(i));
			kunden.add(kunde);			
		}
		return kunden;
	}

	public static List<AbstractKunde> findKundenByNachname(String nachname) {
		final int anzahl = nachname.length();
		final List<AbstractKunde> kunden = new ArrayList<>(anzahl);
		for (int i = 1; i <= anzahl; i++) {
			final AbstractKunde kunde = findKundeById(Long.valueOf(i));
			kunde.setNachname(nachname);
			kunden.add(kunde);			
		}
		return kunden;
	}
	
	public static List<Bestellung> findBestellungenByKunde(AbstractKunde kunde) {
		// Beziehungsgeflecht zwischen Kunde und Bestellungen aufbauen
		final int anzahl = kunde.getId().intValue() % MAX_BESTELLUNGEN + 1;  // 1, 2, 3 oder 4 Bestellungen
		final List<Bestellung> bestellungen = new ArrayList<>(anzahl);
		for (int i = 1; i <= anzahl; i++) {
			final Bestellung bestellung = findBestellungById(Long.valueOf(i));
			bestellung.setKunde(kunde);
			bestellungen.add(bestellung);			
		}
		kunde.setBestellungen(bestellungen);
		
		return bestellungen;
	}
	
	public static List<Posten> findPostenByBestellung(long id) {
		// Beziehungsgeflecht zwischen Bestellung und Posten aufbauen
		final List<Posten> posten = new ArrayList<>();
		for (long i = 1; i <= id; i++) {
			final Posten post = new Posten();
			post.setAnzahl(id);
			post.setArtikel(Mock.findArtikelById(id));
			posten.add(post);
		}
		
		return posten;
	}

	public static Bestellung findBestellungById(Long id) {
		if (id > MAX_ID) {
			return null;
		}		
		final AbstractKunde kunde = findKundeById(id);  // andere ID fuer den Kunden
		final List<Posten> posten = findPostenByBestellung(id);	
		final Bestellung bestellung = new Bestellung();
		
		bestellung.setId(id);
		bestellung.setAusgeliefert(false);
		bestellung.setKunde(kunde);
		bestellung.setPosten(posten);
						
		return bestellung;
	}

	public static <T extends AbstractKunde> T createKunde(T kunde) {
		// Neue IDs fuer Kunde und zugehoerige Adresse
		// Ein neuer Kunde hat auch keine Bestellungen
		final String nachname = kunde.getNachname();
		kunde.setId(Long.valueOf(nachname.length()));
		final Adresse adresse = kunde.getAdresse();
		adresse.setId((Long.valueOf(nachname.length())) + 1);
		adresse.setKunde(kunde);
		kunde.setBestellungen(null);
		
		LOGGER.infof("(LOGGER) Neuer Kunde: %s", kunde);
		return kunde;
	}

	public static void updateKunde(AbstractKunde kunde) {
		LOGGER.infof("(LOGGER) Aktualisierter Kunde: %s", kunde);
	}

	public static void deleteKunde(Long kundeId) {
		LOGGER.infof("(LOGGER) Kunde mit ID=" + kundeId + " geloescht");
	}
	
	public static Artikel findArtikelById(Long id) {
		
		if (id > MAX_ID) {
			return null;
		}

        final Artikel artikel = new Artikel();
        artikel.setId(id);
        artikel.setBezeichnung("Artikel_" + id.toString());
        artikel.setPrice(artikel.getBezeichnung().length() * 2);
        
        return artikel;
	}

	public static Artikel createArtikel(Artikel artikel) {		
		
		final long id = artikel.getBezeichnung().length();
		artikel.setId(id);
		LOGGER.infof("(LOGGER) Neuer Artikel: %s", artikel);        
        return artikel;
	}

	public static void updateArtikel(Artikel artikel) {
		
		final long id = artikel.getBezeichnung().length();
		artikel.setId(id);
		LOGGER.infof("(LOGGER) Aktualisierter Artikel: " + artikel);
	}
		
	public static void updateBestellung(Bestellung bestellung) {
		final long id = 5;
		bestellung.setId(id);
		bestellung = findBestellungById(id);
		LOGGER.infof("(LOGGER) Aktualisierte Bestellung: " + bestellung);
	}

	public static void deleteKunde(AbstractKunde kunde) {
		LOGGER.infof("(LOGGER) Geloeschter Kunde: %s", kunde);
	}

	public static Bestellung createBestellung(Bestellung bestellung, AbstractKunde kunde) {
		bestellung.setId(kunde.getId());
		LOGGER.infof("(LOGGER) Neue Bestellung: %s fuer Kunde: %s", bestellung, kunde);
		return bestellung;
	}

	private Mock() { /**/ }
}