package com.renato.bohler.sd.webservices.WebServices.dao;

import java.io.Serializable;
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

	public List<Accommodation> listar() {
		TypedQuery<Accommodation> query = em.createQuery("select a from Accommodation a", Accommodation.class);

		return query.getResultList();
	}

}
