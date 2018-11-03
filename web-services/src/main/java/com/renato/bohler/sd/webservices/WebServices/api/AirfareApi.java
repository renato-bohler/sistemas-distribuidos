package com.renato.bohler.sd.webservices.WebServices.api;

public class AirfareApi {

	private Long numeroPessoas;
	private Long valorTotal;
	private FlightApi ida;
	private FlightApi volta;

	public Long getNumeroPessoas() {
		return numeroPessoas;
	}

	public void setNumeroPessoas(Long numeroPessoas) {
		this.numeroPessoas = numeroPessoas;
	}

	public Long getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Long valorTotal) {
		this.valorTotal = valorTotal;
	}

	public FlightApi getIda() {
		return ida;
	}

	public void setIda(FlightApi ida) {
		this.ida = ida;
	}

	public FlightApi getVolta() {
		return volta;
	}

	public void setVolta(FlightApi volta) {
		this.volta = volta;
	}

}
