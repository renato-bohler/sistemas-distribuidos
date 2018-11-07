package com.renato.bohler.sd.webservices.WebServices.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.renato.bohler.sd.webservices.WebServices.vo.PackageVo;

@Stateful
public class PackageDao {

	@PersistenceContext
	private EntityManager em;

	public List<PackageVo> listar(String origem, String destino, Date dataIda, Date dataVolta, Long numeroQuartos,
			Long numeroPessoas) {
		StringBuilder sb = new StringBuilder();

		sb.append("select new com.renato.bohler.sd.webservices.WebServices.vo.PackageVo(ida, volta, hospedagem)");

		sb.append(" from Flight ida, Flight volta, Accommodation hospedagem where 1=1");

		sb.append(
				" and ida.origem = :origem and ida.destino = :destino and cast(ida.data as date) = :dataIda and ida.vagas >= :numeroPessoas");
		sb.append(
				" and volta.origem = :destino and volta.destino = :origem and cast(volta.data as date) = :dataVolta and volta.vagas >= :numeroPessoas");

		sb.append(" and hospedagem.cidade = :destino");
		sb.append(" and cast(hospedagem.dataEntrada as date) = :dataIda");
		sb.append(" and cast(hospedagem.dataSaida as date) = :dataVolta");
		sb.append(" and hospedagem.numeroQuartos >= :numeroQuartos");
		sb.append(" and hospedagem.numeroPessoas >= :numeroPessoas");

		TypedQuery<PackageVo> query = em.createQuery(sb.toString(), PackageVo.class);

		query.setParameter("origem", origem);
		query.setParameter("destino", destino);
		query.setParameter("dataIda", dataIda);
		query.setParameter("dataVolta", dataVolta);
		query.setParameter("numeroQuartos", numeroQuartos);
		query.setParameter("numeroPessoas", numeroPessoas);

		return query.getResultList();
	}

}
