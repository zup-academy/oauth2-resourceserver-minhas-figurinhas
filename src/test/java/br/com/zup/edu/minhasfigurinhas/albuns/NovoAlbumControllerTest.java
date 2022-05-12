package br.com.zup.edu.minhasfigurinhas.albuns;

import base.SpringBootIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NovoAlbumControllerTest extends SpringBootIntegrationTest {

    @Autowired
    private AlbumRepository repository;

    @BeforeEach
    private void setUp() {
        repository.deleteAll();
    }

    @Test
    public void deveCadastrarNovoAlbumComSuasFigurinhas() throws Exception {
        // cenário
        NovoAlbumRequest novoAlbum = new NovoAlbumRequest("CDZ",
                "Cavaleiros do Zodiaco",
                List.of(
                        new NovaFigurinhaRequest("Seya", "http://animes.com/cdz/seya.png"),
                        new NovaFigurinhaRequest("Hyoga", "http://animes.com/cdz/hyoga.png")
                )
        );

        // ação
        mockMvc.perform(POST("/api/albuns", novoAlbum))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("**/api/albuns/*"))
                ;

        // validação
        assertEquals(1, repository.count(), "total de albuns");
    }

    @Test
    public void naoDeveCadastrarNovoAlbumComSuasFigurinhas_quandoParametrosInvalidos() throws Exception {
        // cenário
        NovoAlbumRequest albumInvalido = new NovoAlbumRequest("", "", null);

        // ação
        mockMvc.perform(POST("/api/albuns", albumInvalido))
                .andExpect(status().isBadRequest())
        ;

        // validação
        assertEquals(0, repository.count(), "total de albuns");
    }

}