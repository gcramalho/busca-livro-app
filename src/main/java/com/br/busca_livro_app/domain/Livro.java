package com.br.busca_livro_app.domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String linguagem;
    private Integer downloadCount;
    private String gutendexId;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "livro_autor",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private Set<Autor> autores = new HashSet<>();

    // Construtor
    public Livro() {}

    public Livro(String titulo, String linguagem, Integer downloadCount, String gutendexId) {
        this.titulo = titulo;
        this.linguagem = linguagem;
        this.downloadCount = downloadCount;
        this.gutendexId = gutendexId;
    }

    // Getters e Setters (fora do construtor!)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getLinguagem() { return linguagem; }
    public void setLinguagem(String linguagem) { this.linguagem = linguagem; }
    public Integer getDownloadCount() { return downloadCount; }
    public void setDownloadCount(Integer downloadCount) { this.downloadCount = downloadCount; }
    public String getGutendexId() { return gutendexId; }
    public void setGutendexId(String gutendexId) { this.gutendexId = gutendexId; }

    // Métodos para manipulação de autores
    public void addAutor(Autor autor) {
        this.autores.add(autor);
        autor.getLivros().add(this); // Assume-se que o método em Autor se chama getLivros()
    }

    public void removerAutor(Autor autor) {
        this.autores.remove(autor); // Método correto: remove()
        autor.getLivros().remove(this);
    }

    // Getter para autores
    public Set<Autor> getAutores() {
        return autores;
    }

    public void setAutores(Set<Autor> autores) {
        this.autores = autores;
    }
}