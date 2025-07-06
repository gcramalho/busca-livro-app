package com.br.busca_livro_app.repositorio;

import com.br.busca_livro_app.domain.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutorRepositorio extends JpaRepository<Autor, Long> {
    List<Autor> findByNomeConntainingIgnoreCase(String nome);

    @Query("SELECT a FROM Autor a WHERE a.anoNascimento <= :ano AND (a.anoFalecimento IS NULL OR a.anoFalecimento >= :ano)")
    List<Autor> findAutoresVivosNoAno(@Param("ano") int ano);
}
