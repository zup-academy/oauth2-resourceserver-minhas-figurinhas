package br.com.zup.edu.minhasfigurinhas.albuns.listagem;

import base.SpringBootIntegrationTest;
import br.com.zup.edu.minhasfigurinhas.albuns.Album;
import br.com.zup.edu.minhasfigurinhas.albuns.AlbumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ListaAlbunsControllerTest extends SpringBootIntegrationTest {

    @Autowired
    private AlbumRepository repository;

    @BeforeEach
    private void setUp() {
        repository.deleteAll();
    }

    @Test
    public void deveListarTodosOsAlbuns() throws Exception {
        // cenário
        List.of(
            new Album("DBZ", "Album do DBZ", "rafael.ponte"),
            new Album("CDZ", "Album do CDZ", "jordi.silva"),
            new Album("Naruto", "Album do Naturo", "yuri.matheus")
        ).forEach(album -> {
            repository.save(album);
        });

        // ação e validação
        mockMvc.perform(GET("/api/albuns"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].titulo").value("CDZ"))
                .andExpect(jsonPath("$[1].titulo").value("DBZ"))
                .andExpect(jsonPath("$[2].titulo").value("Naruto"))
                ;
    }

    @Test
    public void naoDeveListarTodosOsAlbuns_quandoNaoHouverAlbunsCadastrados() throws Exception {
        // cenário
        repository.deleteAll();

        // ação e validação
        mockMvc.perform(GET("/api/albuns"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty())
                ;
    }

}