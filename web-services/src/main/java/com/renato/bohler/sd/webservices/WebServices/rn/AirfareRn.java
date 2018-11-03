package com.renato.bohler.sd.webservices.WebServices.rn;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.renato.bohler.sd.webservices.WebServices.api.AirfareApi;
import com.renato.bohler.sd.webservices.WebServices.dao.AirfareDao;

@ApplicationScoped
public class AirfareRn {

	@Inject
	private AirfareDao airfareDao;

	public List<AirfareApi> listar(String origem, String destino, Date dataIda, Date dataVolta, Long numeroPessoas) {
		return airfareDao.listar(origem, destino, dataIda, dataVolta, numeroPessoas);
	}

}
