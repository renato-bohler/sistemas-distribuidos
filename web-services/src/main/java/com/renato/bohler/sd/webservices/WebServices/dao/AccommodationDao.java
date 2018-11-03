package com.renato.bohler.sd.webservices.WebServices.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.renato.bohler.sd.webservices.WebServices.model.Accommodation;

@Stateful
public class AccommodationDao {

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

	public Accommodation consultar(Long id) {
		StringBuilder sb = new StringBuilder();

		sb.append("select a from Accommodation a where a.id = :id");

		TypedQuery<Accommodation> query = em.createQuery(sb.toString(), Accommodation.class);
		query.setParameter("id", id);

		Accommodation hospedagem = null;
		try {
			hospedagem = query.getSingleResult();
		} catch (Exception e) {
		}
		return hospedagem;

	}

	public void atualizar(Accommodation hospedagem) {
		Accommodation persistida = em.find(Accommodation.class, hospedagem.getId());

		persistida.setCidade(hospedagem.getCidade());
		persistida.setDataEntrada(hospedagem.getDataEntrada());
		persistida.setDataSaida(hospedagem.getDataSaida());
		persistida.setNumeroPessoas(hospedagem.getNumeroPessoas());
		persistida.setNumeroQuartos(hospedagem.getNumeroQuartos());
		persistida.setPrecoPorPessoa(hospedagem.getPrecoPorPessoa());
		persistida.setPrecoPorQuarto(hospedagem.getPrecoPorQuarto());

		if (persistida.getNumeroPessoas() == 0L || persistida.getNumeroQuartos() == 0L) {
			em.remove(persistida);
		} else {
			em.merge(persistida);
		}
	}

}
