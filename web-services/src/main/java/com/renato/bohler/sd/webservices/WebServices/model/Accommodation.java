package com.renato.bohler.sd.webservices.WebServices.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Accommodation {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private String cidade;

	@NotNull
	private Date dataEntrada;

	@NotNull
	private Date dataSaida;

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

	public Date getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public Date getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(Date dataSaida) {
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
