package br.com.zup.edu.minhasfigurinhas.albuns.detalhamento;

import br.com.zup.edu.minhasfigurinhas.albuns.Album;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class DetalhesDoAlbumResponse {

    private Long id;
    private String titulo;
    private String descricao;
    private String dono;
    private List<DetalheDaFigurinhaResponse> figurinhas;

    public DetalhesDoAlbumResponse(Album album) {
        this.id = album.getId();
        this.titulo = album.getTitulo();
        this.descricao = album.getDescricao();
        this.dono = album.getDono();
        this.figurinhas = album.getFigurinhas().stream().map(figurinha -> {
           return new DetalheDaFigurinhaResponse(figurinha);
        }).collect(toList());
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getDono() {
        return dono;
    }

    public List<DetalheDaFigurinhaResponse> getFigurinhas() {
        return figurinhas;
    }
}
