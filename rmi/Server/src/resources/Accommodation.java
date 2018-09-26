package resources;

public class Accommodation {
	private Long id;
	private String destino;
	private String dataEntrada;
	private String dataSaida;
	private Long numeroQuartos;
	private Long numeroPessoas;
	private Long preco;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
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

	public Long getPreco() {
		return preco;
	}

	public void setPreco(Long preco) {
		this.preco = preco;
	}

}
