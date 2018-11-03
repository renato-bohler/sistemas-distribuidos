package com.renato.bohler.sd.webservices.WebServices.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.renato.bohler.sd.webservices.WebServices.api.PackageApi;

@SuppressWarnings("serial")
@RequestScoped
public class PackageDao implements Serializable {

	@PersistenceContext
	private EntityManager em;

	public List<PackageApi> listar(String origem, String destino, Date dataIda, Date dataVolta, Long numeroQuartos,
			Long numeroPessoas) {
		// TODO: implementar
		/*
		 * TypedQuery<PackageApi> query = em.createQuery("select f from Flight f",
		 * PackageApi.class);
		 * 
		 * return query.getResultList();
		 */
		return new ArrayList<PackageApi>();
	}

}
