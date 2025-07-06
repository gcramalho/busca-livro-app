package com.br.busca_livro_app.repositorio;

import com.br.busca_livro_app.domain.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LivroRepositorio extends JpaRepository<Livro, Long> {
    List<Livro> findByTituloContainingIgnoreCase(String titulo);
    List<Livro> findByLinguagem(String linguagem);

    @Query("SELECT l from Livro l JOIN l.autores a WHERE a.id = :autorId")
    List<Livro> findByAutorId(@Param("autorId") Long autorId);
}
