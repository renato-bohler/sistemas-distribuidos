package com.renato.bohler.sd.webservices.WebServices.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Flight {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private String origem;

	@NotNull
	private String destino;

	@NotNull
	private Date data;

	@NotNull
	private Long vagas;

	@NotNull
	private Long precoUnitario;

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

	public Long getPrecoUnitario() {
		return precoUnitario;
	}

	public void setPrecoUnitario(Long precoUnitario) {
		this.precoUnitario = precoUnitario;
	}

}
