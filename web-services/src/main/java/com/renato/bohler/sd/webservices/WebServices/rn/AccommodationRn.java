package com.renato.bohler.sd.webservices.WebServices.rn;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.renato.bohler.sd.webservices.WebServices.api.AccommodationApi;
import com.renato.bohler.sd.webservices.WebServices.dao.AccommodationDao;
import com.renato.bohler.sd.webservices.WebServices.model.Accommodation;

public class AccommodationRn {

	@Inject
	private AccommodationDao accommodationDao;

	public List<AccommodationApi> listar(String cidade, Date dataEntrada, Date dataSaida, Long numeroQuartos,
			Long numeroPessoas) {
		return this.converterListaParaApi(
				accommodationDao.listar(cidade, dataEntrada, dataSaida, numeroQuartos, numeroPessoas), numeroQuartos,
				numeroPessoas);
	}

	public Boolean comprar(AccommodationApi hospedagemApi) {
		Accommodation hospedagem = accommodationDao.consultar(hospedagemApi.getId());

		if (!this.validarHospedagem(hospedagem, hospedagemApi)) {
			return Boolean.FALSE;
		}

		hospedagem.setNumeroQuartos(hospedagem.getNumeroQuartos() - hospedagemApi.getNumeroQuartos());
		hospedagem.setNumeroPessoas(hospedagem.getNumeroPessoas() - hospedagemApi.getNumeroPessoas());

		accommodationDao.atualizar(hospedagem);

		return Boolean.TRUE;
	}

	private Boolean validarHospedagem(Accommodation hospedagem, AccommodationApi hospedagemApi) {
		if (hospedagem == null || hospedagem.getNumeroPessoas() < hospedagemApi.getNumeroPessoas()
				|| hospedagem.getNumeroQuartos() < hospedagemApi.getNumeroQuartos()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	private AccommodationApi converterParaApi(Accommodation entidade, Long numeroQuartos, Long numeroPessoas) {
		AccommodationApi api = new AccommodationApi();

		api.setId(entidade.getId());
		api.setCidade(entidade.getCidade());
		api.setDataEntrada(entidade.getDataEntrada());
		api.setDataSaida(entidade.getDataSaida());
		api.setNumeroQuartos(numeroQuartos != null ? numeroQuartos : entidade.getNumeroQuartos());
		api.setNumeroPessoas(numeroPessoas != null ? numeroPessoas : entidade.getNumeroPessoas());
		api.setPrecoPorQuarto(entidade.getPrecoPorQuarto());
		api.setPrecoPorPessoa(entidade.getPrecoPorPessoa());
		if (numeroQuartos != null && numeroPessoas != null) {
			api.setValorTotal(
					numeroQuartos * entidade.getPrecoPorQuarto() + numeroPessoas * entidade.getPrecoPorPessoa());
		}

		return api;
	}

	private List<AccommodationApi> converterListaParaApi(List<Accommodation> entidades, Long numeroQuartos,
			Long numeroPessoas) {
		List<AccommodationApi> apis = new ArrayList<AccommodationApi>();

		for (Accommodation entidade : entidades) {
			apis.add(this.converterParaApi(entidade, numeroQuartos, numeroPessoas));
		}

		return apis;
	}
}
