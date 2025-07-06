package com.br.busca_livro_app;

import com.br.busca_livro_app.domain.Autor;
import com.br.busca_livro_app.domain.Livro;
import com.br.busca_livro_app.servico.LivroServico;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class BuscaLivroAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuscaLivroAppApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(LivroServico livroServico) {
		return args -> {
			Scanner scanner = new Scanner(System.in);
			boolean running = true;

			while (running) {
				System.out.println("""
                        Escolha o número da sua opção:
                        1- Buscar livro pelo titulo
                        2- Listar livros registrados
                        3- Listar autores registrados
                        4- Listar autores vivos em um determinado ano
                        5- Listar livros em um determinado idioma
                        6- Sair
                        """);

				int opcao;

				try {
					opcao = Integer.parseInt(scanner.nextLine());
				} catch (NumberFormatException e) {
					System.out.println("Por favor, insira um número válido.");
					continue;
				}

				switch (opcao) {
					case 1 -> handleBuscaPorTitulo(livroServico, scanner);
					case 2 -> handleListaLivros(livroServico);
					case 3 -> handleListaAutores(livroServico);
					case 4 -> handleAutoresVivosNoAno(livroServico, scanner);
					case 5 -> handleLivrosPorLinguagem(livroServico, scanner);
					case 6 -> {
						System.out.println("Saindo do sistema...");
						running = false;
					}
					default -> System.out.println("Opção inválida. Por favor, escolha um número entre 1 a 6.");
				}
			}
			scanner.close();
		};
	}

	private static void handleBuscaPorTitulo(LivroServico livroServico, Scanner scanner) {
		System.out.println("Insira o nome do livro que você deseja procurar:");
		String titulo = scanner.nextLine();

		List<Livro> livros = livroServico.procuraLivroPorTitulo(titulo);

		if (livros.isEmpty()) {
			System.out.println("Nenhum livro encontrado com o título: " + titulo);
		} else {
			for (Livro livro : livros) {
				printLivroDetalhes(livro);
			}
		}
	}

	private static void handleListaLivros(LivroServico livroServico) {
		List<Livro> livros = livroServico.getAllLivros();
		if (livros.isEmpty()) {
			System.out.println("Nenhum livro registrado no banco de dados");
		} else {
			for (Livro livro : livros) {
				printLivroDetalhes(livro);
			}
		}
	}

	private static void handleListaAutores(LivroServico livroServico) {
		List<Autor> autores = livroServico.getAllAutores();
		if (autores.isEmpty()) {
			System.out.println("Nenhum autor registrado no banco de dados");
		} else {
			System.out.println("------ AUTORES REGISTRADOS ------");
			for (Autor autor : autores) {
				System.out.println("Nome: " + autor.getNome());
				System.out.println("Ano de nascimento: " +
						(autor.getAnoNascimento() != null ? autor.getAnoNascimento() : "Desconhecido"));
				System.out.println("Ano de falecimento: " +
						(autor.getAnoFalecimento() != null ? autor.getAnoFalecimento() : "Desconhecido"));
				System.out.println("------------------------");
			}
		}
	}

	private static void handleAutoresVivosNoAno(LivroServico livroServico, Scanner scanner) {
		System.out.println("Insira o ano para verificar quais autores estavam vivos:");
		int ano;

		try {
			ano = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("Por favor, insira um ano válido.");
			return;
		}

		List<Autor> autores = livroServico.getAutoresVivosNoAno(ano);

		if (autores.isEmpty()) {
			System.out.println("Nenhum autor registrado estava vivo no ano de " + ano);
		} else {
			System.out.println("------ AUTORES VIVOS EM " + ano + " ------");
			for (Autor autor : autores) {
				System.out.println("Nome: " + autor.getNome());
				System.out.println("Ano de nascimento: " + autor.getAnoNascimento());
				System.out.println("Ano de falecimento: " +
						(autor.getAnoFalecimento() != null ? autor.getAnoFalecimento() : "Presente"));
				System.out.println("------------------------------------");
			}
		}
	}

	private static void handleLivrosPorLinguagem(LivroServico livroServico, Scanner scanner) {
		System.out.println("""
                Insira um idioma para retornar a busca:
                es - espanhol
                en - ingles
                fr - frances
                pt - portugues
                """);
		String linguagem = scanner.nextLine().toLowerCase();

		List<Livro> livros = livroServico.getLivrosPorLinguagem(linguagem);
		if (livros.isEmpty()) {
			System.out.println("Nenhum livro encontrado no idioma: " + linguagem);
		} else {
			System.out.println("------ LIVROS EM " + linguagem.toUpperCase() + " ------");
			for (Livro livro : livros) {
				printLivroDetalhes(livro);
			}
		}
	}

	private static void printLivroDetalhes(Livro livro) {
		System.out.println("------LIVRO------");
		System.out.println("Titulo: " + livro.getTitulo());

		System.out.print("Autor(es): ");
		if (livro.getAutores().isEmpty()) {
			System.out.println("Desconhecido");
		} else {
			String autores = String.join(", ", livro.getAutores().stream().map(Autor::getNome).toList());
			System.out.println(autores);
		}

		System.out.println("Idioma: " + (livro.getLinguagem() != null ? livro.getLinguagem() : "Desconhecido"));
		System.out.println("Número de downloads: " +
				(livro.getDownloadCount() != null ? livro.getDownloadCount() : "Desconhecido"));
		System.out.println("------------------------");
	}
}