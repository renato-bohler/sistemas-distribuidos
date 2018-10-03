package resources;

import java.io.Serializable;

public class Airfare implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long numeroPessoas;
	private Long valorTotal;
	private Flight ida;
	private Flight volta;

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

	public Flight getIda() {
		return ida;
	}

	public void setIda(Flight ida) {
		this.ida = ida;
	}

	public Flight getVolta() {
		return volta;
	}

	public void setVolta(Flight volta) {
		this.volta = volta;
	}

}
