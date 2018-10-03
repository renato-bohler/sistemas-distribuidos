package resources;

import java.io.Serializable;

public class Flight implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String origem;
	private String destino;
	private String data;
	private Long vagas;
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

	public String getData() {
		return data;
	}

	public void setData(String data) {
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
