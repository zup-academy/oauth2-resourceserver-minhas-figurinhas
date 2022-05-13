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

    private static Album ALBUM;

    @BeforeEach
    private void setUp() {
        repository.deleteAll();
        this.ALBUM = new Album("DBZ", "Album do DBZ", "rafael.ponte");
        ALBUM.adiciona(
                new Figurinha("picollo", "http://animes.com/dbz/picollo.jpg")
        );
        repository.save(ALBUM);
    }

    @Test
    public void deveAdicionarNovaFigurinhaNoAlbum() throws Exception {
        // cenario
        NovaFigurinhaNoAlbumRequest novaFigurinha
                = new NovaFigurinhaNoAlbumRequest("gohan", "http://animes.com/dbz/gohan.jpg");

        // ação
        mockMvc.perform(POST(uri(ALBUM.getId()), novaFigurinha))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("**/api/albuns/*/figurinhas/**"))
        ;

        // validação
        Album encontrado = repository.findByIdWithFigurinhas(ALBUM.getId());
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
        mockMvc.perform(POST(uri(-2022L), figurinhaInvalida))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("album não encontrado"))
        ;
    }

    @Test
    public void naoDeveAdicionarNovaFigurinhaNoAlbum_quandoParametrosInvalidos() throws Exception {
        // cenario
        NovaFigurinhaNoAlbumRequest figurinhaInvalida
                = new NovaFigurinhaNoAlbumRequest("", "");

        // ação
        mockMvc.perform(POST(uri(ALBUM.getId()), figurinhaInvalida))
                .andExpect(status().isBadRequest())
        ;

        // validação
        Album encontrado = repository.findByIdWithFigurinhas(ALBUM.getId());
        assertThat(encontrado.getFigurinhas())
                .hasSize(1)
                .extracting("descricao")
                .doesNotContain("gohan")
        ;
    }

    private String uri(Long albumId) {
        String uri = "/api/albuns/{albumId}/figurinhas"
                .replace("{albumId}", albumId.toString());
        return uri;
    }

}