package com.br.busca_livro_app.servico;

import com.br.busca_livro_app.domain.Autor;
import com.br.busca_livro_app.domain.Livro;
import com.br.busca_livro_app.repositorio.AutorRepositorio;
import com.br.busca_livro_app.repositorio.LivroRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class LivroServico {
    private final LivroRepositorio livroRepositorio;
    private final AutorRepositorio autorRepositorio;
    private final RestTemplate restTemplate;
    private final String GUTENDEX_API = "https://gutendex.com/books/";

    @Autowired
    public LivroServico(LivroRepositorio livroRepositorio, AutorRepositorio autorRepositorio) {
        this.livroRepositorio = livroRepositorio;
        this.autorRepositorio = autorRepositorio;
        this.restTemplate = new RestTemplate();
    }

    public List<Livro> procuraLivroPorTitulo(String titulo) {
        // Primeiro busca no banco de dados
        List<Livro> localLivros = livroRepositorio.findByTituloContainingIgnoreCase(titulo);

        if (!localLivros.isEmpty()) {
            return localLivros;
        }

        // Se não encontrar localmente -> Gutendex API
        String url = GUTENDEX_API + "?search=" + titulo.replace(" ", "%20");
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response != null && response.containsKey("results")) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

            List<Livro> livros = new ArrayList<>();

            for (Map<String, Object> result : results) {
                Livro livro = convertApiResultEmLivro(result);
                livros.add(livro);
                saveLivroFromApi(livro);
            }
            return livros;
        }
        return Collections.emptyList();
    }

    public List<Livro> getAllLivros() {
        return livroRepositorio.findAll();
    }

    public List<Autor> getAllAutores() {
        return autorRepositorio.findAll();
    }

    public List<Autor> getAutoresVivosNoAno(int ano) { // Corrigido: retorna List<Autor>
        return autorRepositorio.findAutoresVivosNoAno(ano);
    }

    public List<Livro> getLivrosPorLinguagem(String linguagem) {
        return livroRepositorio.findByLinguagem(linguagem); // Corrigido: findByLinguagem
    }

    private Livro convertApiResultEmLivro(Map<String, Object> result) {
        Livro livro = new Livro();
        livro.setTitulo((String) result.get("title"));
        livro.setGutendexId(String.valueOf(result.get("id")));

        // Handle language (API retorna um array, usamos o primeiro elemento)
        List<String> linguagens = (List<String>) result.get("languages");
        if (linguagens != null && !linguagens.isEmpty()) {
            livro.setLinguagem(linguagens.get(0));
        }

        livro.setDownloadCount((Integer) result.get("download_count"));

        // Autores
        List<Map<String, Object>> autores = (List<Map<String, Object>>) result.get("authors");
        if (autores != null) {
            for (Map<String, Object> autorDados : autores) {
                Autor autor = new Autor();
                autor.setNome((String) autorDados.get("name"));

                // Tratamento ano de nascimento/falecimento
                if (autorDados.containsKey("birth_year") && autorDados.get("birth_year") != null) {
                    autor.setAnoNascimento((Integer) autorDados.get("birth_year"));
                }

                if (autorDados.containsKey("death_year") && autorDados.get("death_year") != null) {
                    autor.setAnoFalecimento((Integer) autorDados.get("death_year"));
                }

                livro.addAutor(autor);
            }
        }

        return livro;
    }

    private void saveLivroFromApi(Livro livro) {
        // Salva autor primeiro
        Set<Autor> autoresToSave = new HashSet<>();

        for (Autor autor : livro.getAutores()) {
            // Checa se autor já existe
            List<Autor> autoresExistentes = autorRepositorio.findByNomeConntainingIgnoreCase(autor.getNome());

            if (!autoresExistentes.isEmpty()) {
                autoresToSave.add(autoresExistentes.get(0));
            } else {
                autoresToSave.add(autor);
            }
        }

        // Checa se livro já existe
        List<Livro> livrosExistentes = livroRepositorio.findByTituloContainingIgnoreCase(livro.getTitulo());
        if (livrosExistentes.isEmpty()) {
            // Seta autores e salva
            livro.setAutores(autoresToSave);
            livroRepositorio.save(livro);
        }
    }
}