package br.com.zup.edu.minhasfigurinhas.albuns.figurinhas;

import base.SpringBootIntegrationTest;
import br.com.zup.edu.minhasfigurinhas.albuns.Album;
import br.com.zup.edu.minhasfigurinhas.albuns.AlbumRepository;
import br.com.zup.edu.minhasfigurinhas.albuns.Figurinha;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
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
        mockMvc.perform(POST(uri(ALBUM.getId()), novaFigurinha)
                        .with(jwt()
                            .jwt(jwt -> {
                                jwt.claim("preferred_username", ALBUM.getDono());
                            })
                            .authorities(new SimpleGrantedAuthority("SCOPE_albuns:write"))
                        ))
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
        mockMvc.perform(POST(uri(-2022L), figurinhaInvalida)
                        .with(jwt()
                            .jwt(jwt -> {
                                jwt.claim("preferred_username", ALBUM.getDono());
                            })
                            .authorities(new SimpleGrantedAuthority("SCOPE_albuns:write"))
                        ))
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
        mockMvc.perform(POST(uri(ALBUM.getId()), figurinhaInvalida)
                        .with(jwt()
                            .jwt(jwt -> {
                                jwt.claim("preferred_username", ALBUM.getDono());
                            })
                            .authorities(new SimpleGrantedAuthority("SCOPE_albuns:write"))
                        ))
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

    /**
     * Cenário extra para garantir que não é possivel alterar album de outro dono (usuário)
     */
    @Test
    public void naoDeveAdicionarNovaFigurinhaNoAlbum_quandoAlbumForDeOutroUsuario() throws Exception {
        // cenario
        NovaFigurinhaNoAlbumRequest novaFigurinha
                = new NovaFigurinhaNoAlbumRequest("gohan", "http://animes.com/dbz/gohan.jpg");

        // ação
        mockMvc.perform(POST(uri(ALBUM.getId()), novaFigurinha)
                        .with(jwt()
                                .jwt(jwt -> {
                                    jwt.claim("preferred_username", "outro-usuario-qualquer");
                                })
                                .authorities(new SimpleGrantedAuthority("SCOPE_albuns:write"))
                        ))
                .andExpect(status().isForbidden())
                .andExpect(status().reason("album pertence a outro dono (usuário)"))
        ;

        // validação
        Album encontrado = repository.findByIdWithFigurinhas(ALBUM.getId());
        assertThat(encontrado.getFigurinhas())
                .hasSize(1)
                .extracting("descricao")
                .doesNotContain("gohan")
        ;
    }

    @Test
    public void deveAdicionarNovaFigurinhaNoAlbum_quandoAccessTokenNaoEnviado() throws Exception {
        // cenario
        NovaFigurinhaNoAlbumRequest novaFigurinha
                = new NovaFigurinhaNoAlbumRequest("gohan", "http://animes.com/dbz/gohan.jpg");

        // ação
        mockMvc.perform(POST(uri(ALBUM.getId()), novaFigurinha))
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    public void deveAdicionarNovaFigurinhaNoAlbum_quandoAccessTokenNaoPossuiScopeApropriado() throws Exception {
        // cenario
        NovaFigurinhaNoAlbumRequest novaFigurinha
                = new NovaFigurinhaNoAlbumRequest("gohan", "http://animes.com/dbz/gohan.jpg");

        // ação
        mockMvc.perform(POST(uri(ALBUM.getId()), novaFigurinha)
                    .with(jwt()))
                .andExpect(status().isForbidden())
        ;
    }

    private String uri(Long albumId) {
        String uri = "/api/albuns/{albumId}/figurinhas"
                .replace("{albumId}", albumId.toString());
        return uri;
    }

}