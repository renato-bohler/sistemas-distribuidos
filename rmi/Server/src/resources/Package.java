package resources;

import java.io.Serializable;

public class Package implements Serializable {
	private static final long serialVersionUID = 1L;

	private Airfare passagem;
	private Accommodation hospedagem;

	public Airfare getPassagem() {
		return passagem;
	}

	public void setPassagem(Airfare passagem) {
		this.passagem = passagem;
	}

	public Accommodation getHospedagem() {
		return hospedagem;
	}

	public void setHospedagem(Accommodation hospedagem) {
		this.hospedagem = hospedagem;
	}

}