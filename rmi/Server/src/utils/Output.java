package utils;

import java.util.List;

import resources.Airfare;
import resources.Flight;

public class Output {
	private static final String DIVISOR = "---------------------------";

	public static void imprimir(String message) {
		System.out.println(message);
	}

	public static void imprimir() {
		System.out.println();
	}

	public static void imprimirPassagens(List<Airfare> passagens) {
		if (passagens.isEmpty()) {
			imprimir("Nenhuma passagem encontrada");
			return;
		}

		for (Airfare passagem : passagens) {
			imprimir(DIVISOR);
			imprimir("Código:\t\t" + passagem.getIda().getId().toString()
					+ (passagem.getVolta() != null && passagem.getVolta().getId() != null
							? "-" + passagem.getVolta().getId().toString() : ""));
			imprimir("Origem:\t\t" + passagem.getIda().getOrigem());
			imprimir("Destino:\t" + passagem.getIda().getDestino());
			imprimir("Data ida:\t" + passagem.getIda().getData());
			imprimir("Data volta:\t" + (passagem.getVolta() != null && passagem.getVolta().getData() != null
					? passagem.getVolta().getData() : "-"));
			imprimir("N° passageiros:\t" + passagem.getNumeroPessoas());
			imprimir("Valor total:\tR$ " + passagem.getValorTotal() + ",00");
		}
		imprimir(DIVISOR);
	}

	public static void imprimirVoos(List<Flight> voos) {
		if (voos.isEmpty()) {
			imprimir("Nenhum vôo encontrado");
			return;
		}

		for (Flight voo : voos) {
			imprimir(DIVISOR);
			imprimir("Código:\t\t" + voo.getId().toString());
			imprimir("Origem:\t\t" + voo.getOrigem());
			imprimir("Destino:\t" + voo.getDestino());
			imprimir("Data:\t\t" + voo.getData());
			imprimir("N° vagas:\t" + voo.getVagas());
			imprimir("Preço unitário:\tR$ " + voo.getPrecoUnitario() + ",00");
		}
		imprimir(DIVISOR);
	}

	public static void imprimirMesmaLinha(String mensagem) {
		System.out.print(mensagem);
	}

	public static void imprimirMesmaLinha(Long valor) {
		System.out.print(valor.toString());
	}

	public static void limparTela() {
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
	}
}