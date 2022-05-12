package br.com.zup.edu.minhasfigurinhas.albuns.listagem;

import br.com.zup.edu.minhasfigurinhas.albuns.Album;

public class AlbumResponse {

    private Long id;
    private String titulo;
    private String descricao;
    private String dono;

    public AlbumResponse(Album album) {
        this.id = album.getId();
        this.titulo = album.getTitulo();
        this.descricao = album.getDescricao();
        this.dono = album.getDono();
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
}
