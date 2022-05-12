package br.com.zup.edu.minhasfigurinhas.albuns;

import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NovaFigurinhaRequest {

    @NotBlank
    @Size(max = 2000)
    private String descricao;

    @URL
    @NotNull
    private String enderecoDaImagem;

    public NovaFigurinhaRequest(String descricao, String enderecoDaImagem) {
        this.descricao = descricao;
        this.enderecoDaImagem = enderecoDaImagem;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getEnderecoDaImagem() {
        return enderecoDaImagem;
    }
}
