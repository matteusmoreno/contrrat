package br.com.matteusmoreno.contrrat.client;

public record ViaCepResponse(
        String cep,
        String logradouro,
        String bairro,
        String localidade,
        String uf) {
}
