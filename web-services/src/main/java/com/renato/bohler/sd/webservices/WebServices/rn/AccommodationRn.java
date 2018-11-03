package com.renato.bohler.sd.webservices.WebServices.rn;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.renato.bohler.sd.webservices.WebServices.api.AccommodationApi;
import com.renato.bohler.sd.webservices.WebServices.dao.AccommodationDao;
import com.renato.bohler.sd.webservices.WebServices.model.Accommodation;

@ApplicationScoped
public class AccommodationRn {

	@Inject
	private AccommodationDao accommodationDao;

	public List<AccommodationApi> listar(String cidade, Date dataEntrada, Date dataSaida, Long numeroQuartos,
			Long numeroPessoas) {
		return this.converterListaParaApi(
				accommodationDao.listar(cidade, dataEntrada, dataSaida, numeroQuartos, numeroPessoas), numeroQuartos,
				numeroPessoas);
	}

	private AccommodationApi converterParaApi(Accommodation entidade, Long numeroQuartos, Long numeroPessoas) {
		AccommodationApi api = new AccommodationApi();

		api.setId(entidade.getId());
		api.setCidade(entidade.getCidade());
		api.setDataEntrada(entidade.getDataEntrada());
		api.setDataSaida(entidade.getDataSaida());
		api.setNumeroQuartos(numeroQuartos);
		api.setNumeroPessoas(numeroPessoas);
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
