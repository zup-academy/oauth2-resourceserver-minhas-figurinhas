package br.com.zup.edu.minhasfigurinhas.albuns;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.net.URI;

@Entity
public class Figurinha {

    @Id
    @GeneratedValue
    private Long id;

    private String descricao;
    private String enderecoDaImagem;

    @ManyToOne
    private Album album;

    @Deprecated
    public Figurinha(){}

    public Figurinha(String descricao, String enderecoDaImagem) {
        this.descricao = descricao;
        this.enderecoDaImagem = enderecoDaImagem;
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

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
