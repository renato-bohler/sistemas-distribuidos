package com.renato.bohler.sd.webservices.WebServices.dao;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.renato.bohler.sd.webservices.WebServices.model.Flight;

@SuppressWarnings("serial")
@RequestScoped
public class FlightDao implements Serializable {

	@PersistenceContext
	private EntityManager em;

	public List<Flight> listar() {
		CriteriaQuery<Flight> criteria = em.getCriteriaBuilder().createQuery(Flight.class);
		Root<Flight> flight = criteria.from(Flight.class);
		criteria.select(flight);
		return em.createQuery(criteria).getResultList();
	}

}
