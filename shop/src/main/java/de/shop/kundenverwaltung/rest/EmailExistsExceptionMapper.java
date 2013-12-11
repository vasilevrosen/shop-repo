package de.shop.kundenverwaltung.rest;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.Status.CREATED;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import de.shop.kundenverwaltung.service.EmailExistsException;
import de.shop.util.interceptor.Log;


@Provider
@ApplicationScoped
@Log
public class EmailExistsExceptionMapper implements ExceptionMapper<EmailExistsException> {
	@Context
	private HttpHeaders headers;
	
	@Override
	public Response toResponse(EmailExistsException e) {
//		final String msg = messages.getMessage(headers, e.getMessageKey(), e.getEmail());
		return Response.status(CREATED)
		               .type(TEXT_PLAIN)
//		               .entity(msg)
		               .build();
	}
}