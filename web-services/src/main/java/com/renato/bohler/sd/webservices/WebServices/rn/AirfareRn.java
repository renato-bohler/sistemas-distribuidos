package com.renato.bohler.sd.webservices.WebServices.rn;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.renato.bohler.sd.webservices.WebServices.api.AirfareApi;
import com.renato.bohler.sd.webservices.WebServices.dao.AirfareDao;
import com.renato.bohler.sd.webservices.WebServices.dao.FlightDao;
import com.renato.bohler.sd.webservices.WebServices.model.Flight;
import com.renato.bohler.sd.webservices.WebServices.vo.AirfareVo;

public class AirfareRn {

	@Inject
	private AirfareDao airfareDao;

	@Inject
	private FlightDao flightDao;

	@Inject
	private FlightRn flightRn;

	public List<AirfareApi> listar(String origem, String destino, Date dataIda, Date dataVolta, Long numeroPessoas) {
		return this.converterParaListaApi(airfareDao.listar(origem, destino, dataIda, dataVolta, numeroPessoas),
				numeroPessoas);
	}

	public Boolean comprar(AirfareApi passagemApi) {
		Flight ida = flightDao.consultar(passagemApi.getIda().getId());

		Flight volta = null;
		if (passagemApi.getVolta() != null) {
			volta = flightDao.consultar(passagemApi.getVolta().getId());
			if (volta == null) {
				return Boolean.FALSE;
			}
		}

		if (!this.validarPassagem(ida, volta, passagemApi)) {
			return Boolean.FALSE;
		}

		ida.setVagas(ida.getVagas() - passagemApi.getNumeroPessoas());
		if (volta != null) {
			volta.setVagas(volta.getVagas() - passagemApi.getNumeroPessoas());
		}

		flightDao.atualizar(ida);
		if (volta != null) {
			flightDao.atualizar(volta);
		}

		return Boolean.TRUE;
	}

	private Boolean validarPassagem(Flight ida, Flight volta, AirfareApi passagemApi) {
		if ((ida != null && ida.getVagas() >= passagemApi.getNumeroPessoas())
				&& (volta == null || volta.getVagas() >= passagemApi.getNumeroPessoas())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public AirfareApi converterParaApi(AirfareVo vo, Long numeroPessoas) {
		AirfareApi api = new AirfareApi();

		api.setNumeroPessoas(numeroPessoas);
		api.setValorTotal(0L);

		api.setIda(flightRn.converterParaApi(vo.getIda()));
		api.setValorTotal(api.getValorTotal() + vo.getIda().getPrecoUnitario() * numeroPessoas);

		if (vo.getVolta() != null) {
			api.setVolta(flightRn.converterParaApi(vo.getVolta()));
			api.setValorTotal(api.getValorTotal() + vo.getVolta().getPrecoUnitario() * numeroPessoas);
		}

		return api;
	}

	private List<AirfareApi> converterParaListaApi(List<AirfareVo> vos, Long numeroPessoas) {
		List<AirfareApi> apis = new ArrayList<AirfareApi>();

		for (AirfareVo vo : vos) {
			apis.add(this.converterParaApi(vo, numeroPessoas));
		}

		return apis;
	}

}
