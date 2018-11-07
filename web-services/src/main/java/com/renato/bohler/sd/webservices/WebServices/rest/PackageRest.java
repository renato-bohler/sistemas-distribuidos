package com.renato.bohler.sd.webservices.WebServices.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.renato.bohler.sd.webservices.WebServices.api.PackageApi;
import com.renato.bohler.sd.webservices.WebServices.rn.PackageRn;
import com.renato.bohler.sd.webservices.WebServices.util.DateUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/package")
@Path("/package")
public class PackageRest {

	@Inject
	private PackageRn packageRn;

	@Inject
	private DateUtil dateUtil;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(nickname = "listar-pacotes", value = "Lista os pacotes de acordo com os filtros", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	@ApiResponses(@ApiResponse(code = 201, message = "Passagens listadas", response = PackageApi.class))
	public List<PackageApi> listar(@QueryParam("origem") String origem, @QueryParam("destino") String destino,
			@QueryParam("dataIda") String dataIda, @QueryParam("dataVolta") String dataVolta,
			@QueryParam("numeroQuartos") Long numeroQuartos, @QueryParam("numeroPessoas") Long numeroPessoas) {
		return packageRn.listar(origem, destino, dateUtil.getDateFromString(dataIda),
				dateUtil.getDateFromString(dataVolta), numeroQuartos, numeroPessoas);
	}

	@POST
	@Path("/comprar")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(nickname = "comprar-pacote", value = "Compra um pacote", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	@ApiResponses({ @ApiResponse(code = 201, message = "Pacote comprado"),
			@ApiResponse(code = 400, message = "Pacote não disponível") })
	public Response comprar(PackageApi pacoteApi) {
		if (packageRn.comprar(pacoteApi)) {
			return Response.ok().build();
		}
		return Response.status(400).build();
	}

}
