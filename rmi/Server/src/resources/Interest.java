package resources;

import java.io.Serializable;

import enums.EnumDesiredEvent;
import rmi.Client;

public class Interest implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Client cliente;
	private EnumDesiredEvent eventoDesejado;
	private String origem;
	private String destino;
	private Long numeroQuartos;
	private Long numeroPessoas;
	private Long precoMaximo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Client getCliente() {
		return cliente;
	}

	public void setCliente(Client cliente) {
		this.cliente = cliente;
	}

	public EnumDesiredEvent getEventoDesejado() {
		return eventoDesejado;
	}

	public void setEventoDesejado(EnumDesiredEvent eventoDesejado) {
		this.eventoDesejado = eventoDesejado;
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

	public Long getPrecoMaximo() {
		return precoMaximo;
	}

	public void setPrecoMaximo(Long precoMaximo) {
		this.precoMaximo = precoMaximo;
	}

}
