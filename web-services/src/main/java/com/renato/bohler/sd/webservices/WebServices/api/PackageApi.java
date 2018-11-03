package com.renato.bohler.sd.webservices.WebServices.api;

public class PackageApi {

	private AirfareApi passagem;
	private AccommodationApi hospedagem;
	private Long valorTotal;

	public AirfareApi getPassagem() {
		return passagem;
	}

	public void setPassagem(AirfareApi passagem) {
		this.passagem = passagem;
	}

	public AccommodationApi getHospedagem() {
		return hospedagem;
	}

	public void setHospedagem(AccommodationApi hospedagem) {
		this.hospedagem = hospedagem;
	}

	public Long getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Long valorTotal) {
		this.valorTotal = valorTotal;
	}

}
