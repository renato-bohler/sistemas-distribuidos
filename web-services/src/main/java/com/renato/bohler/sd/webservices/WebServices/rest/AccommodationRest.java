package com.renato.bohler.sd.webservices.WebServices.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.renato.bohler.sd.webservices.WebServices.api.AccommodationApi;
import com.renato.bohler.sd.webservices.WebServices.rn.AccommodationRn;
import com.renato.bohler.sd.webservices.WebServices.util.DateUtil;

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

	@Inject
	private DateUtil dateUtil;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(nickname = "listar-hospedagem", value = "Lista as hospedagens de acordo com os filtros", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	@ApiResponses(@ApiResponse(code = 201, message = "Hospedagens listadas", response = AccommodationApi.class))
	public List<AccommodationApi> listar(@QueryParam("cidade") String cidade,
			@QueryParam("dataEntrada") String dataEntrada, @QueryParam("dataSaida") String dataSaida,
			@QueryParam("numeroQuartos") Long numeroQuartos, @QueryParam("numeroPessoas") Long numeroPessoas) {
		return accommodationRn.listar(cidade, dateUtil.getDateFromString(dataEntrada),
				dateUtil.getDateFromString(dataSaida), numeroQuartos, numeroPessoas);
	}

	@POST
	@Path("/comprar")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(nickname = "comprar-hospedagem", value = "Compra uma hospedagem pelo seu ID", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	@ApiResponses({ @ApiResponse(code = 201, message = "Hospedagem comprada"),
			@ApiResponse(code = 400, message = "Hospedagem não disponível") })
	public Response comprar(AccommodationApi hospedagemApi) {
		if (accommodationRn.comprar(hospedagemApi)) {
			return Response.ok().build();
		}
		return Response.status(400).build();
	}

}
