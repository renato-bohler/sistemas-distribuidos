package com.renato.bohler.sd.webservices.WebServices.dao;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.renato.bohler.sd.webservices.WebServices.model.Flight;

@SuppressWarnings("serial")
@RequestScoped
public class FlightDao implements Serializable {

	@PersistenceContext
	private EntityManager em;

	public List<Flight> listar() {
		TypedQuery<Flight> query = em.createQuery("select f from Flight f", Flight.class);

		return query.getResultList();
	}

}
