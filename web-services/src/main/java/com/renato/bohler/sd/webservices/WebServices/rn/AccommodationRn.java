package com.renato.bohler.sd.webservices.WebServices.rn;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.renato.bohler.sd.webservices.WebServices.dao.AccommodationDao;
import com.renato.bohler.sd.webservices.WebServices.model.Accommodation;

@ApplicationScoped
public class AccommodationRn {

	@Inject
	private AccommodationDao accommodationDao;

	public List<Accommodation> listar() {
		return accommodationDao.listar();
	}

}
