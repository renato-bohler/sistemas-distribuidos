package com.renato.bohler.sd.webservices.WebServices.rn;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.renato.bohler.sd.webservices.WebServices.api.AccommodationApi;
import com.renato.bohler.sd.webservices.WebServices.api.AirfareApi;
import com.renato.bohler.sd.webservices.WebServices.api.PackageApi;
import com.renato.bohler.sd.webservices.WebServices.dao.AccommodationDao;
import com.renato.bohler.sd.webservices.WebServices.dao.FlightDao;
import com.renato.bohler.sd.webservices.WebServices.dao.PackageDao;
import com.renato.bohler.sd.webservices.WebServices.model.Accommodation;
import com.renato.bohler.sd.webservices.WebServices.model.Flight;
import com.renato.bohler.sd.webservices.WebServices.vo.PackageVo;

public class PackageRn {

	@Inject
	private PackageDao packageDao;

	@Inject
	private FlightDao flightDao;

	@Inject
	private AccommodationDao accommodationDao;

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

	public Boolean comprar(PackageApi pacoteApi) {
		// Validações passagem
		AirfareApi passagemApi = pacoteApi.getPassagem();

		Flight ida = flightDao.consultar(passagemApi.getIda().getId());
		Flight volta = flightDao.consultar(passagemApi.getVolta().getId());

		if (volta == null || !airfareRn.validarPassagem(ida, volta, passagemApi)) {
			return Boolean.FALSE;
		}

		// Validações hospedagem
		AccommodationApi hospedagemApi = pacoteApi.getHospedagem();

		Accommodation hospedagem = accommodationDao.consultar(hospedagemApi.getId());
		if (!accommodationRn.validarHospedagem(hospedagem, hospedagemApi)) {
			return Boolean.FALSE;
		}

		// Atualizações
		ida.setVagas(ida.getVagas() - passagemApi.getNumeroPessoas());
		volta.setVagas(volta.getVagas() - passagemApi.getNumeroPessoas());
		hospedagem.setNumeroQuartos(hospedagem.getNumeroQuartos() - hospedagemApi.getNumeroQuartos());
		hospedagem.setNumeroPessoas(hospedagem.getNumeroPessoas() - hospedagemApi.getNumeroPessoas());

		flightDao.atualizar(ida);
		flightDao.atualizar(volta);
		accommodationDao.atualizar(hospedagem);

		return Boolean.TRUE;
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
