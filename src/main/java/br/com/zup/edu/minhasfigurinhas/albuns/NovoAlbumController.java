package br.com.zup.edu.minhasfigurinhas.albuns;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
public class NovoAlbumController {

    @Autowired
    private AlbumRepository repository;

    @Transactional
    @PostMapping("/api/albuns")
    public ResponseEntity<?> cadastra(@RequestBody @Valid NovoAlbumRequest request,
                                      UriComponentsBuilder uriBuilder,
                                      @AuthenticationPrincipal Jwt user) {

        Album album = request.toModel(user.getClaim("preferred_username"));
        repository.save(album);

        URI location = uriBuilder.path("/api/albuns/{id}")
                .buildAndExpand(album.getId())
                .toUri();

        return ResponseEntity
                .created(location).build();
    }
}
