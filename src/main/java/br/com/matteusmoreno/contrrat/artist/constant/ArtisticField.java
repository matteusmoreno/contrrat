package br.com.matteusmoreno.contrrat.artist.constant;


import lombok.Getter;

@Getter
public enum ArtisticField {
    BANDA("Banda"),
    CANTOR("Cantor(a)"),
    DJ("DJ"),
    MUSICO_INSTRUMENTISTA("Músico Instrumentista"),
    FOTOGRAFO("Fotógrafo(a)"),
    VIDEOMAKER("Videomaker"),
    DANCARINO("Dançarino(a)"),
    ATOR("Ator/Atriz"),
    COMEDIANTE("Comediante"),
    MAGICO("Mágico"),
    PALHACO("Palhaço"),
    ARTISTA_CIRCENSE("Artista Circense"),
    BARTENDER("Bartender"),
    CHEF_DE_COZINHA("Chef de Cozinha"),
    CARICATURISTA("Caricaturista"),
    PINTOR("Pintor(a)"),
    GRAFITEIRO("Grafiteiro(a)"),
    RECREADOR_INFANTIL("Recreador(a) Infantil"),
    PERSONAGEM_VIVO("Personagem Vivo"),
    CELEBRANTE_DE_CASAMENTO("Celebrante de Casamento"),
    MESTRE_DE_CERIMONIAS("Mestre de Cerimônias");

    private final String displayName;

    ArtisticField(String displayName) {
        this.displayName = displayName;
    }

}