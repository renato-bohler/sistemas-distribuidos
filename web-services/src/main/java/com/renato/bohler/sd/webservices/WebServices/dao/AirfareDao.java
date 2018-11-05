package com.renato.bohler.sd.webservices.WebServices.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.renato.bohler.sd.webservices.WebServices.vo.AirfareVo;

@Stateful
public class AirfareDao {

	@PersistenceContext
	private EntityManager em;

	public List<AirfareVo> listar(String origem, String destino, Date dataIda, Date dataVolta, Long numeroPessoas) {
		StringBuilder sb = new StringBuilder();

		Boolean idaEVolta = dataVolta != null ? Boolean.TRUE : Boolean.FALSE;

		sb.append("select new com.renato.bohler.sd.webservices.WebServices.vo.AirfareVo(");
		sb.append(idaEVolta ? "ida, volta" : "ida");
		sb.append(")");

		sb.append(idaEVolta ? " from Flight ida, Flight volta" : " from Flight ida");
		sb.append(
				" where ida.origem = :origem and ida.destino = :destino and cast(ida.data as date) = :dataIda and ida.vagas >= :numeroPessoas");
		sb.append(idaEVolta
				? " and volta.origem = :destino and volta.destino = :origem and cast(volta.data as date) = :dataVolta and volta.vagas >= :numeroPessoas"
				: "");

		TypedQuery<AirfareVo> query = em.createQuery(sb.toString(), AirfareVo.class);

		query.setParameter("origem", origem);
		query.setParameter("destino", destino);
		query.setParameter("dataIda", dataIda);
		if (idaEVolta) {
			query.setParameter("dataVolta", dataVolta);
		}
		query.setParameter("numeroPessoas", numeroPessoas);

		return query.getResultList();
	}

}
