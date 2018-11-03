package com.renato.bohler.sd.webservices.WebServices.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.renato.bohler.sd.webservices.WebServices.model.Accommodation;

@SuppressWarnings("serial")
@RequestScoped
public class AccommodationDao implements Serializable {

	@PersistenceContext
	private EntityManager em;

	public List<Accommodation> listar(String cidade, Date dataEntrada, Date dataSaida, Long numeroQuartos,
			Long numeroPessoas) {
		StringBuilder sb = new StringBuilder();

		sb.append("select a from Accommodation a");
		sb.append(" where 1 = 1");

		if (cidade != null && !cidade.isEmpty()) {
			sb.append(" and a.cidade = :cidade");
		}

		if (dataEntrada != null) {
			sb.append(" and a.dataEntrada = :dataEntrada");
		}

		if (dataSaida != null) {
			sb.append(" and a.dataSaida = :dataSaida");
		}

		if (numeroQuartos != null) {
			sb.append(" and a.numeroQuartos >= :numeroQuartos");
		}

		if (numeroPessoas != null) {
			sb.append(" and a.numeroPessoas >= :numeroPessoas");
		}

		TypedQuery<Accommodation> query = em.createQuery(sb.toString(), Accommodation.class);

		if (cidade != null && !cidade.isEmpty()) {
			query.setParameter("cidade", cidade);
		}

		if (dataEntrada != null) {
			query.setParameter("dataEntrada", dataEntrada);
		}

		if (dataSaida != null) {
			query.setParameter("dataSaida", dataSaida);
		}

		if (numeroQuartos != null) {
			query.setParameter("numeroQuartos", numeroQuartos);
		}

		if (numeroPessoas != null) {
			query.setParameter("numeroPessoas", numeroPessoas);
		}

		return query.getResultList();
	}

}
