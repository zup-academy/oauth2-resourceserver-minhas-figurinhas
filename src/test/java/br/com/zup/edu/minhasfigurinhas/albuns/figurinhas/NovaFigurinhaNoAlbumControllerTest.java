package br.com.zup.edu.minhasfigurinhas.albuns.figurinhas;

import base.SpringBootIntegrationTest;
import br.com.zup.edu.minhasfigurinhas.albuns.Album;
import br.com.zup.edu.minhasfigurinhas.albuns.AlbumRepository;
import br.com.zup.edu.minhasfigurinhas.albuns.Figurinha;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NovaFigurinhaNoAlbumControllerTest extends SpringBootIntegrationTest {

    @Autowired
    private AlbumRepository repository;

    @BeforeEach
    private void setUp() {
        repository.deleteAll();
    }

    @Test
    public void deveAdicionarNovaFigurinhaNoAlbum() throws Exception {
        // cenario
        Album album = new Album("DBZ", "Album do DBZ", "rafael.ponte");
        album.adiciona(
            new Figurinha("picollo", "http://animes.com/dbz/picollo.jpg")
        );
        repository.save(album);

        NovaFigurinhaNoAlbumRequest novaFigurinha
                = new NovaFigurinhaNoAlbumRequest("gohan", "http://animes.com/dbz/gohan.jpg");

        // ação
        String uri = "/api/albuns/{albumId}/figurinhas"
                .replace("{albumId}", album.getId().toString());
        mockMvc.perform(POST(uri, novaFigurinha))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("**/api/albuns/*/figurinhas/**"))
        ;

        // validação
        Album encontrado = repository.findByIdWithFigurinhas(album.getId());
        assertThat(encontrado.getFigurinhas())
                .hasSize(2)
                .extracting("descricao")
                .containsExactly("picollo", "gohan")
        ;
    }

    @Test
    public void naoDeveAdicionarNovaFigurinhaNoAlbum_quandoAlbumNaoEncontrado() throws Exception {
        // cenario
        NovaFigurinhaNoAlbumRequest figurinhaInvalida
                = new NovaFigurinhaNoAlbumRequest("gohan", "http://animes.com/dbz/gohan.jpg");

        // ação
        String uri = "/api/albuns/{albumId}/figurinhas"
                .replace("{albumId}", "-2022");
        mockMvc.perform(POST(uri, figurinhaInvalida))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("album não encontrado"))
        ;
    }

    @Test
    public void naoDeveAdicionarNovaFigurinhaNoAlbum_quandoParametrosInvalidos() throws Exception {
        // cenario
        Album album = new Album("DBZ", "Album do DBZ", "rafael.ponte");
        album.adiciona(
                new Figurinha("picollo", "http://animes.com/dbz/picollo.jpg")
        );
        repository.save(album);

        NovaFigurinhaNoAlbumRequest figurinhaInvalida
                = new NovaFigurinhaNoAlbumRequest("", "");

        // ação
        String uri = "/api/albuns/{albumId}/figurinhas"
                .replace("{albumId}", album.getId().toString());
        mockMvc.perform(POST(uri, figurinhaInvalida))
                .andExpect(status().isBadRequest())
        ;

        // validação
        Album encontrado = repository.findByIdWithFigurinhas(album.getId());
        assertThat(encontrado.getFigurinhas())
                .hasSize(1)
                .extracting("descricao")
                .doesNotContain("gohan")
        ;
    }


}