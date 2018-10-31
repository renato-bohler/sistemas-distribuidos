package com.renato.bohler.sd.webservices.WebServices.rn;

import java.util.ArrayList;
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

	public List<AccommodationApi> listar() {
		return this.converterListaParaApi(accommodationDao.listar());
	}

	private AccommodationApi converterParaApi(Accommodation entidade) {
		AccommodationApi api = new AccommodationApi();

		api.setId(entidade.getId());
		api.setCidade(entidade.getCidade());
		api.setDataEntrada(entidade.getDataEntrada());
		api.setDataSaida(entidade.getDataSaida());
		api.setNumeroQuartos(entidade.getNumeroQuartos());
		api.setNumeroPessoas(entidade.getNumeroPessoas());
		api.setPrecoPorQuarto(entidade.getPrecoPorQuarto());
		api.setPrecoPorPessoa(entidade.getPrecoPorPessoa());
		// TODO: calcular valor total
		api.setValorTotal(100L);

		return api;
	}

	private List<AccommodationApi> converterListaParaApi(List<Accommodation> entidades) {
		List<AccommodationApi> apis = new ArrayList<AccommodationApi>();

		for (Accommodation entidade : entidades) {
			apis.add(this.converterParaApi(entidade));
		}

		return apis;
	}
}
