package com.renato.bohler.sd.webservices.WebServices.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.renato.bohler.sd.webservices.WebServices.model.Accommodation;
import com.renato.bohler.sd.webservices.WebServices.rn.AccommodationRn;

@Path("/accommodation")
@RequestScoped
public class AccommodationRest {

	@Inject
	private AccommodationRn accommodationRn;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Accommodation> listar() {
		return accommodationRn.listar();
	}
}
