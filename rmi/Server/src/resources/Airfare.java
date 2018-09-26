package resources;

import enums.EnumAirfareType;

public class Airfare {
	private Long id;
	private EnumAirfareType tipo;
	private String origem;
	private String destino;
	private String dataIda;
	private String dataVolta;
	private Long numeroPessoas;
	private Long preco;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EnumAirfareType getTipo() {
		return tipo;
	}

	public void setTipo(EnumAirfareType tipo) {
		this.tipo = tipo;
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

	public String getDataIda() {
		return dataIda;
	}

	public void setDataIda(String dataIda) {
		this.dataIda = dataIda;
	}

	public String getDataVolta() {
		return dataVolta;
	}

	public void setDataVolta(String dataVolta) {
		this.dataVolta = dataVolta;
	}

	public Long getNumeroPessoas() {
		return numeroPessoas;
	}

	public void setNumeroPessoas(Long numeroPessoas) {
		this.numeroPessoas = numeroPessoas;
	}

	public Long getPreco() {
		return preco;
	}

	public void setPreco(Long preco) {
		this.preco = preco;
	}

}
