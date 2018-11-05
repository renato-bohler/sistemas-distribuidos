package com.renato.bohler.sd.webservices.WebServices.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.renato.bohler.sd.webservices.WebServices.api.AirfareApi;
import com.renato.bohler.sd.webservices.WebServices.rn.AirfareRn;
import com.renato.bohler.sd.webservices.WebServices.util.DateUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/airfare")
@Path("/airfare")
public class AirfareRest {

	@Inject
	private AirfareRn airfareRn;

	@Inject
	private DateUtil dateUtil;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(nickname = "listar-passagens", value = "Lista as passagens de acordo com os filtros", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	@ApiResponses(@ApiResponse(code = 201, message = "Passagens listadas", response = AirfareApi.class))
	public List<AirfareApi> listar(@QueryParam("origem") String origem, @QueryParam("destino") String destino,
			@QueryParam("dataIda") String dataIda, @QueryParam("dataVolta") String dataVolta,
			@QueryParam("numeroPessoas") Long numeroPessoas) {
		return airfareRn.listar(origem, destino, dateUtil.getDateFromString(dataIda),
				dateUtil.getDateFromString(dataVolta), numeroPessoas);
	}

}
