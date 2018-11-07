package com.renato.bohler.sd.webservices.WebServices.rn;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.renato.bohler.sd.webservices.WebServices.api.PackageApi;
import com.renato.bohler.sd.webservices.WebServices.dao.PackageDao;
import com.renato.bohler.sd.webservices.WebServices.vo.PackageVo;

public class PackageRn {

	@Inject
	private PackageDao packageDao;

	@Inject
	private AirfareRn airfareRn;

	@Inject
	private AccommodationRn accommodationRn;

	public List<PackageApi> listar(String origem, String destino, Date dataIda, Date dataVolta, Long numeroQuartos,
			Long numeroPessoas) {
		return this.converterListaParaApi(
				packageDao.listar(origem, destino, dataIda, dataVolta, numeroQuartos, numeroPessoas), numeroQuartos,
				numeroPessoas);
	}

	private PackageApi converterParaApi(PackageVo vo, Long numeroQuartos, Long numeroPessoas) {
		PackageApi api = new PackageApi();

		api.setPassagem(airfareRn.converterParaApi(vo.getPassagem(), numeroPessoas));
		api.setHospedagem(accommodationRn.converterParaApi(vo.getHospedagem(), numeroQuartos, numeroPessoas));
		api.setValorTotal(numeroQuartos * vo.getHospedagem().getPrecoPorQuarto()
				+ numeroPessoas * (vo.getHospedagem().getPrecoPorPessoa() + vo.getPassagem().getIda().getPrecoUnitario()
						+ vo.getPassagem().getVolta().getPrecoUnitario()));

		return api;
	}

	private List<PackageApi> converterListaParaApi(List<PackageVo> vos, Long numeroQuartos, Long numeroPessoas) {
		List<PackageApi> apis = new ArrayList<PackageApi>();

		for (PackageVo vo : vos) {
			apis.add(this.converterParaApi(vo, numeroQuartos, numeroPessoas));
		}

		return apis;
	}

}
