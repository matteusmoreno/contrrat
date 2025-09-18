package br.com.matteusmoreno.contrrat.contract.constant;

public enum ContractStatus {
    PENDING_CONFIRMATION, // Aguardando aprovação do artista
    CONFIRMED,            // Artista aceitou
    REJECTED,             // Artista recusou
    CANCELED,             // Cancelado por uma das partes
    COMPLETED             // Evento já aconteceu
}
