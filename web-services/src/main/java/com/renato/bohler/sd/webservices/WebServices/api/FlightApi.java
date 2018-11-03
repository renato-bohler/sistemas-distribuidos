package com.renato.bohler.sd.webservices.WebServices.api;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class FlightApi implements Serializable {

	private Long id;
	private String origem;
	private String destino;
	private Date data;
	private Long vagas;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Long getVagas() {
		return vagas;
	}

	public void setVagas(Long vagas) {
		this.vagas = vagas;
	}

}
