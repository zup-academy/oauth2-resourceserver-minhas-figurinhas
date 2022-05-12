package br.com.zup.edu.minhasfigurinhas.albuns.detalhamento;

import br.com.zup.edu.minhasfigurinhas.albuns.Figurinha;

public class DetalheDaFigurinhaResponse {

    private Long id;
    private String descricao;
    private String enderecoDaImagem;

    public DetalheDaFigurinhaResponse(Figurinha figurinha) {
        this.id = figurinha.getId();
        this.descricao = figurinha.getDescricao();
        this.enderecoDaImagem = figurinha.getEnderecoDaImagem();
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getEnderecoDaImagem() {
        return enderecoDaImagem;
    }
}
