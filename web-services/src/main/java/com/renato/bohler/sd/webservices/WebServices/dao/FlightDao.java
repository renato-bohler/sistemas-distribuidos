package com.renato.bohler.sd.webservices.WebServices.dao;

import java.util.List;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.renato.bohler.sd.webservices.WebServices.model.Flight;

@Stateful
public class FlightDao {

	@PersistenceContext
	private EntityManager em;

	public List<Flight> listar() {
		TypedQuery<Flight> query = em.createQuery("select f from Flight f", Flight.class);

		return query.getResultList();
	}

}
