package de.shop.bestellverwaltung.rest;

import static de.shop.util.Constants.SELF_LINK;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_XML;

import java.net.URI;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.shop.artikelverwaltung.rest.ArtikelResource;
import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.bestellverwaltung.domain.Posten;
import de.shop.bestellverwaltung.service.BestellungService;
import de.shop.bestellverwaltung.service.BestellungServiceImpl;
import de.shop.kundenverwaltung.domain.Kunde;
import de.shop.kundenverwaltung.rest.KundeResource;
import de.shop.util.interceptor.Log;
import de.shop.util.rest.UriHelper;

@Path("/bestellungen")
@Produces({ APPLICATION_JSON, APPLICATION_XML + ";qs=0.75", TEXT_XML + ";qs=0.5" })
@Consumes
@RequestScoped
@Log
public class BestellungResource {

	@Context
	private UriInfo uriInfo;
	
	@Inject
	private UriHelper uriHelper;
	
	@Inject
	private BestellungService bs;
	
	@Inject
	private BestellungServiceImpl bsi;
	
	@Inject
	private KundeResource kr;
	
	@Inject
	private ArtikelResource ar;
	
	@GET
	@Path("{id:[1-9][0-9]*}")
	public Response findBestellungById(@PathParam("id") Long id) {
		
		final Bestellung bestellung = bs.findBestellungById(id);
		setStructuralLinks(bestellung, uriInfo);
		// Link-Header setzen
		final Response response = Response.ok(bestellung)
                                          .links(getTransitionalLinks(bestellung, uriInfo))
                                          .build();
		return response;
	}
	
	public void setStructuralLinks(Bestellung bestellung, UriInfo uriInfo) {
		// URI fuer Kunde setzen
		final Kunde kunde = bestellung.getKunde();
		final List<Posten> posten = bestellung.getPosten();
		if (kunde != null) {
			final URI kundeUri = kr.getUriKunde(bestellung.getKunde(), uriInfo);
			bestellung.setKundeUri(kundeUri);
		}		
		// URI fuer Artikel setzen
		for (Posten p : posten) {
			if (p != null) {
				final URI artikelURI = ar.getUriArtikel(p.getArtikel(), uriInfo);
				p.getArtikel().setArtikelUri(artikelURI);
        	}
		}
	}
			
	public Link[] getTransitionalLinks(Bestellung bestellung, UriInfo uriInfo) {
		final Link self = Link.fromUri(getUriBestellung(bestellung, uriInfo))
                              .rel(SELF_LINK)
                              .build();
//		final Link add = Link.fromUri(uriHelper.getUri(BestellungResource.class, uriInfo))
//                             .rel(ADD_LINK)
//                             .build();
		return new Link[] { self };
	}
	
	public URI getUriBestellung(Bestellung bestellung, UriInfo uriInfo) {
		return uriHelper.getUri(BestellungResource.class, "findBestellungById", bestellung.getId(), uriInfo);
	}
	
	//TODO
//	@GET
//	@Path("{id:[1-9][0-9]*}/lieferungen")
//	public Response findLieferungenByBestellungId(@PathParam("id") Long id) {
//		// Diese Methode ist bewusst NICHT implementiert, um zu zeigen,
//		// wie man Methodensignaturen an der Schnittstelle fuer andere
//		// Teammitglieder schon mal bereitstellt, indem einfach ein "Internal
//		// Server Error (500)" produziert wird.
//		// Die Kolleg/inn/en koennen nun weiterarbeiten, waehrend man selbst
//		// gerade keine Zeit hat, weil andere Aufgaben Vorrang haben.
//		
//		// TODO findLieferungenByBestellungId noch nicht implementiert
//		return Response.status(INTERNAL_SERVER_ERROR)
//			       .entity("findLieferungenByBestellungId: NOT YET IMPLEMENTED")
//			       .type(TEXT_PLAIN)
//			       .build();
//	}
	
	@POST
	@Consumes({APPLICATION_JSON, APPLICATION_XML, TEXT_XML })
	@Produces
	public Response createBestellung(Bestellung bestellung) {
		final Kunde kunde = bestellung.getKunde();
		bestellung.setId((long)kunde.getNachname().length());
		bestellung = bsi.createBestellung(bestellung, kunde);
		return Response.created(getUriBestellung(bestellung, uriInfo))
			           .build();
	}
	
	@PUT
	@Consumes({APPLICATION_JSON, APPLICATION_XML, TEXT_XML })
	@Produces
	public void updateBestellung(Bestellung bestellung) {
		bsi.updateBestellung(bestellung);
	}
}