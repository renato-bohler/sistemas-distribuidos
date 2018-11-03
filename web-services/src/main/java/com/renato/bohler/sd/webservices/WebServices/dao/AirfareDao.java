package com.renato.bohler.sd.webservices.WebServices.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.renato.bohler.sd.webservices.WebServices.api.AirfareApi;

@Stateful
public class AirfareDao {

	@PersistenceContext
	private EntityManager em;

	public List<AirfareApi> listar(String origem, String destino, Date dataIda, Date dataVolta, Long numeroPessoas) {
		// TODO: implementar
		/*
		 * TypedQuery<Airfare> query = em.createQuery("select f from Flight f",
		 * Airfare.class);
		 * 
		 * return query.getResultList();
		 */
		return new ArrayList<AirfareApi>();
	}

}
