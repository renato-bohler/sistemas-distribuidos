package com.renato.bohler.sd.webservices.WebServices.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@SuppressWarnings("serial")
@Entity
public class Accommodation implements Serializable {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private String cidade;

	@NotNull
	private String dataEntrada;

	@NotNull
	private String dataSaida;

	@NotNull
	private Long numeroQuartos;

	@NotNull
	private Long numeroPessoas;

	@NotNull
	private Long precoPorQuarto;

	@NotNull
	private Long precoPorPessoa;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(String dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public String getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(String dataSaida) {
		this.dataSaida = dataSaida;
	}

	public Long getNumeroQuartos() {
		return numeroQuartos;
	}

	public void setNumeroQuartos(Long numeroQuartos) {
		this.numeroQuartos = numeroQuartos;
	}

	public Long getNumeroPessoas() {
		return numeroPessoas;
	}

	public void setNumeroPessoas(Long numeroPessoas) {
		this.numeroPessoas = numeroPessoas;
	}

	public Long getPrecoPorQuarto() {
		return precoPorQuarto;
	}

	public void setPrecoPorQuarto(Long precoPorQuarto) {
		this.precoPorQuarto = precoPorQuarto;
	}

	public Long getPrecoPorPessoa() {
		return precoPorPessoa;
	}

	public void setPrecoPorPessoa(Long precoPorPessoa) {
		this.precoPorPessoa = precoPorPessoa;
	}

}
