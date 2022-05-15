package br.com.zup.edu.minhasfigurinhas.albuns;

import base.SpringBootIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
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
        mockMvc.perform(POST("/api/albuns", novoAlbum)
                        .with(jwt()
                            .jwt(jwt -> {
                                jwt.claim("preferred_username", "rponte");
                            })
                            .authorities(new SimpleGrantedAuthority("SCOPE_albuns:write"))
                        ))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("**/api/albuns/*"))
                ;

        // validação
        assertEquals(1, repository.count(), "total de albuns");
        assertEquals("rponte", repository.findAll().get(0).getDono(), "dono do album");
    }

    @Test
    public void naoDeveCadastrarNovoAlbumComSuasFigurinhas_quandoParametrosInvalidos() throws Exception {
        // cenário
        NovoAlbumRequest albumInvalido = new NovoAlbumRequest("", "", null);

        // ação
        Jwt jwt = Jwt.withTokenValue("token")
                    .header("alg", "none")
                    .claim("sub", "user-id")
                    .claim("preferred_username", "rponte")
                    .claim("scope", "albuns:write")
                    .build();

        mockMvc.perform(POST("/api/albuns", albumInvalido)
                        .with(jwt().jwt(jwt)))
                .andExpect(status().isBadRequest())
        ;

        // validação
        assertEquals(0, repository.count(), "total de albuns");
    }

    @Test
    public void naoDeveCadastrarNovoAlbumComSuasFigurinhas_quandoAccessTokenNaoEnviado() throws Exception {
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
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    public void naoDeveCadastrarNovoAlbumComSuasFigurinhas_quandoAccessTokenNaoPossuiScopeApropriado() throws Exception {
        // cenário
        NovoAlbumRequest novoAlbum = new NovoAlbumRequest("CDZ",
                "Cavaleiros do Zodiaco",
                List.of(
                        new NovaFigurinhaRequest("Seya", "http://animes.com/cdz/seya.png"),
                        new NovaFigurinhaRequest("Hyoga", "http://animes.com/cdz/hyoga.png")
                )
        );

        // ação
        mockMvc.perform(POST("/api/albuns", novoAlbum)
                    .with(jwt()))
                .andExpect(status().isForbidden())
        ;
    }

}