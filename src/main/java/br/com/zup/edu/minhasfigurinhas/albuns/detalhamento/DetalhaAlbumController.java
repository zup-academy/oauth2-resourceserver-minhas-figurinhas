package br.com.zup.edu.minhasfigurinhas.albuns.detalhamento;

import br.com.zup.edu.minhasfigurinhas.albuns.Album;
import br.com.zup.edu.minhasfigurinhas.albuns.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@RestController
public class DetalhaAlbumController {

    @Autowired
    private AlbumRepository repository;

    @Transactional
    @GetMapping("/api/albuns/{id}")
    public ResponseEntity<?> detalha(@PathVariable Long id) {

        Album album = repository.findById(id).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "album não encontrado");
        });

        return ResponseEntity
                .ok(new DetalhesDoAlbumResponse(album));
    }
}
