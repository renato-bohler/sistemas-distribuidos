package resources;

import enums.EnumDesiredEvent;
import rmi.Client;

public class Interest {
	private Long id;
	private Client cliente;
	private EnumDesiredEvent eventoDesejado;
	private String destinoDesejado;
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

	public String getDestinoDesejado() {
		return destinoDesejado;
	}

	public void setDestinoDesejado(String destinoDesejado) {
		this.destinoDesejado = destinoDesejado;
	}

	public Long getPrecoMaximo() {
		return precoMaximo;
	}

	public void setPrecoMaximo(Long precoMaximo) {
		this.precoMaximo = precoMaximo;
	}

}
