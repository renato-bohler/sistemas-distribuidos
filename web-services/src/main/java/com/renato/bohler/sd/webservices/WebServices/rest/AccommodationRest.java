package com.renato.bohler.sd.webservices.WebServices.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.renato.bohler.sd.webservices.WebServices.api.AccommodationApi;
import com.renato.bohler.sd.webservices.WebServices.rn.AccommodationRn;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/accommodation")
@Path("/accommodation")
@RequestScoped
public class AccommodationRest {

	@Inject
	private AccommodationRn accommodationRn;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(nickname = "listar-hospedagem", value = "Lista todas as hospedagens", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	@ApiResponses(@ApiResponse(code = 201, message = "Hospedagens listadas", response = AccommodationApi.class))
	public List<AccommodationApi> listar() {
		return accommodationRn.listar();
	}
}
