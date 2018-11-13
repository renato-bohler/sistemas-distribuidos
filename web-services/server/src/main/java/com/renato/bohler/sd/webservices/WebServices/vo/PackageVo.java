package com.renato.bohler.sd.webservices.WebServices.vo;

import com.renato.bohler.sd.webservices.WebServices.model.Accommodation;
import com.renato.bohler.sd.webservices.WebServices.model.Flight;

public class PackageVo {

	private AirfareVo passagem;
	private Accommodation hospedagem;

	public PackageVo(Flight ida, Flight volta, Accommodation hospedagem) {
		this.passagem = new AirfareVo(ida, volta);
		this.hospedagem = hospedagem;
	}

	public AirfareVo getPassagem() {
		return passagem;
	}

	public void setPassagem(AirfareVo passagem) {
		this.passagem = passagem;
	}

	public Accommodation getHospedagem() {
		return hospedagem;
	}

	public void setHospedagem(Accommodation hospedagem) {
		this.hospedagem = hospedagem;
	}

}
