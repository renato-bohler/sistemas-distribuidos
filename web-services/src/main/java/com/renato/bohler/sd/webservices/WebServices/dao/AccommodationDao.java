package com.renato.bohler.sd.webservices.WebServices.dao;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.renato.bohler.sd.webservices.WebServices.model.Accommodation;

@SuppressWarnings("serial")
@RequestScoped
public class AccommodationDao implements Serializable {

	@PersistenceContext
	private EntityManager em;

	public List<Accommodation> listar() {
		CriteriaQuery<Accommodation> criteria = em.getCriteriaBuilder().createQuery(Accommodation.class);
		Root<Accommodation> accommodation = criteria.from(Accommodation.class);
		criteria.select(accommodation);
		return em.createQuery(criteria).getResultList();
	}

}
