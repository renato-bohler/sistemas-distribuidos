package com.renato.bohler.sd.webservices.WebServices.rest;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.renato.bohler.sd.webservices.WebServices.api.AirfareApi;
import com.renato.bohler.sd.webservices.WebServices.api.PackageApi;
import com.renato.bohler.sd.webservices.WebServices.rn.PackageRn;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/package")
@Path("/package")
public class PackageRest {

	@Inject
	private PackageRn packageRn;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(nickname = "listar-pacotes", value = "Lista os pacotes de acordo com os filtros", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	@ApiResponses(@ApiResponse(code = 201, message = "Passagens listadas", response = AirfareApi.class))
	public List<PackageApi> listar(@QueryParam("origem") String origem, @QueryParam("destino") String destino,
			@QueryParam("dataIda") Date dataIda, @QueryParam("dataVolta") Date dataVolta,
			@QueryParam("numeroQuartos") Long numeroQuartos, @QueryParam("numeroPessoas") Long numeroPessoas) {
		return packageRn.listar(origem, destino, dataIda, dataVolta, numeroQuartos, numeroPessoas);
	}

}
