package com.renato.bohler.sd.webservices.WebServices.api;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AccommodationApi implements Serializable {

	private Long id;
	private String cidade;
	private String dataEntrada;
	private String dataSaida;
	private Long numeroQuartos;
	private Long numeroPessoas;
	private Long precoPorQuarto;
	private Long precoPorPessoa;
	private Long valorTotal;

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

	public Long getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Long valorTotal) {
		this.valorTotal = valorTotal;
	}

}
