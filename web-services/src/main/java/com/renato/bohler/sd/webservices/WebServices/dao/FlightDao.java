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

	public Flight consultar(Long id) {
		StringBuilder sb = new StringBuilder();

		sb.append("select f from Flight f where f.id = :id");

		TypedQuery<Flight> query = em.createQuery(sb.toString(), Flight.class);
		query.setParameter("id", id);

		Flight voo = null;
		try {
			voo = query.getSingleResult();
		} catch (Exception e) {
		}
		return voo;
	}

	public void atualizar(Flight voo) {
		Flight persistido = em.find(Flight.class, voo.getId());

		persistido.setData(voo.getData());
		persistido.setDestino(voo.getDestino());
		persistido.setOrigem(voo.getOrigem());
		persistido.setPrecoUnitario(voo.getPrecoUnitario());
		persistido.setVagas(voo.getVagas());

		if (persistido.getVagas() == 0L) {
			em.remove(persistido);
		} else {
			em.merge(persistido);
		}
	}
}
