package com.renato.bohler.sd.webservices.WebServices.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.renato.bohler.sd.webservices.WebServices.model.Flight;
import com.renato.bohler.sd.webservices.WebServices.rn.FlightRn;

@Path("/flight")
@RequestScoped
public class FlightRest {

	@Inject
	private FlightRn flightRn;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Flight> listar() {
		return flightRn.listar();
	}
}