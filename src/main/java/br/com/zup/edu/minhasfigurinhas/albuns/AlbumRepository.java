package br.com.zup.edu.minhasfigurinhas.albuns;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    @Query("select a from Album a join fetch a.figurinhas where a.id = :id")
    public Album findByIdWithFigurinhas(Long id);

}
