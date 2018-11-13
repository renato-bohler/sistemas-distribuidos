package com.renato.bohler.sd.webservices.WebServices.vo;

import com.renato.bohler.sd.webservices.WebServices.model.Flight;

public class AirfareVo {

	private Flight ida;
	private Flight volta;

	public AirfareVo(Flight ida) {
		this.ida = ida;
		this.volta = null;
	}

	public AirfareVo(Flight ida, Flight volta) {
		this.ida = ida;
		this.volta = volta;
	}

	public Flight getIda() {
		return ida;
	}

	public void setIda(Flight ida) {
		this.ida = ida;
	}

	public Flight getVolta() {
		return volta;
	}

	public void setVolta(Flight volta) {
		this.volta = volta;
	}

}
