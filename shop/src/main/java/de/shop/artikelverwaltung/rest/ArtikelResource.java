package de.shop.artikelverwaltung.rest;

import static de.shop.util.Constants.SELF_LINK;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_XML;

import java.net.URI;

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

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.artikelverwaltung.service.ArtikelService;
import de.shop.util.interceptor.Log;
import de.shop.util.rest.UriHelper;

@Path("/artikel")
@Produces({ APPLICATION_JSON, APPLICATION_XML + ";qs=0.75", TEXT_XML + ";qs=0.5" })
@Consumes
@RequestScoped
@Log
public class ArtikelResource {
	@Inject
	private ArtikelService as;
	
	@Context
	private UriInfo uriInfo;
	
	@Inject
	private UriHelper uriHelper;
	
	@GET
	@Path("{id:[1-9][0-9]*}")
	  public Response findArtikelById(@PathParam("id") Long id, @Context UriInfo uriInfo) {
        
        final Artikel artikel = as.findArtikelById(id);        
        setStructuralLinks(artikel, uriInfo);
        // Link-Header setzen
        final Response response = Response.ok(artikel)
        						.links(getTransitionalLinks(artikel, uriInfo))
                                .build();
        return response;
	}

	public void setStructuralLinks(Artikel artikel, UriInfo uriInfo) {
        // URI fuer Artikel setzen
        final URI uri = getUriArtikel(artikel, uriInfo);
        artikel.setArtikelUri(uri);
	}

	public Link[] getTransitionalLinks(Artikel artikel, UriInfo uriInfo) {
        final Link self = Link.fromUri(getUriArtikel(artikel, uriInfo))
                          .rel(SELF_LINK)
                          .build();
        
        return new Link[] { self };
	}

	public URI getUriArtikel(Artikel artikel, UriInfo uriInfo) {
        return uriHelper.getUri(ArtikelResource.class, "findArtikelById", artikel.getId(), uriInfo);
	}

	@POST
	@Consumes({APPLICATION_JSON, APPLICATION_XML, TEXT_XML })
	@Produces
	public Response createArtikel(Artikel artikel) {
        artikel = as.createArtikel(artikel);
        return Response.created(getUriArtikel(artikel, uriInfo))
                           .build();
	}

	@PUT
	@Consumes({APPLICATION_JSON, APPLICATION_XML, TEXT_XML })
	@Produces
	public void updateArtikel(Artikel artikel) {
        as.updateArtikel(artikel);
	}
}