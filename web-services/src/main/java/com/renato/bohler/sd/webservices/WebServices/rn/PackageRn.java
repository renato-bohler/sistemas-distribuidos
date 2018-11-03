package com.renato.bohler.sd.webservices.WebServices.rn;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.renato.bohler.sd.webservices.WebServices.api.PackageApi;
import com.renato.bohler.sd.webservices.WebServices.dao.PackageDao;

public class PackageRn {

	@Inject
	private PackageDao packageDao;

	public List<PackageApi> listar(String origem, String destino, Date dataIda, Date dataVolta, Long numeroQuartos,
			Long numeroPessoas) {
		return packageDao.listar(origem, destino, dataIda, dataVolta, numeroQuartos, numeroPessoas);
	}

}
