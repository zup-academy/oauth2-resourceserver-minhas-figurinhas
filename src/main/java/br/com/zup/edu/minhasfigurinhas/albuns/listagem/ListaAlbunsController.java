package br.com.zup.edu.minhasfigurinhas.albuns.listagem;

import br.com.zup.edu.minhasfigurinhas.albuns.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.ASC;

@RestController
public class ListaAlbunsController {

    @Autowired
    private AlbumRepository repository;

    @GetMapping("/api/albuns")
    public ResponseEntity<?> lista() {

        List<AlbumResponse> albuns = repository.findAll(Sort.by(ASC, "titulo")).stream().map(album -> {
            return new AlbumResponse(album);
        }).collect(toList());

        return ResponseEntity.ok(albuns);
    }

}
