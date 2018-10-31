package com.renato.bohler.sd.webservices.WebServices.rn;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.renato.bohler.sd.webservices.WebServices.dao.FlightDao;
import com.renato.bohler.sd.webservices.WebServices.model.Flight;

@ApplicationScoped
public class FlightRn {

	@Inject
	private FlightDao flightDao;

	public List<Flight> listar() {
		return flightDao.listar();
	}

}
