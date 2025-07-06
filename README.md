=========== Buscador de Livros ===========
Aplicação de consulta de livros desenvolvida em Java com Spring Boot que consome a API Gutendex e armazena os dados em um banco PostgreSQL.

🚀 Funcionalidades
🔍 Buscar livros por título

📖 Listar todos os livros registrados

✍️ Listar todos os autores registrados

🎂 Listar autores vivos em um determinado ano

🌍 Listar livros por idioma (es, en, fr, pt)

💾 Armazenamento em banco de dados PostgreSQL

🛠️ Tecnologias Utilizadas
Java 17

Spring Boot 3.2.0

Spring Data JPA

PostgreSQL

RestTemplate (para consumo da API Gutendex)

⚙️ Configuração
Pré-requisitos
Java 17 JDK instalado

PostgreSQL instalado e rodando

Maven instalado

Instalação
Clone o repositório:

bash
git clone https://github.com/seu-usuario/busca-livro-app.git
Configure o banco de dados:

Crie um banco chamado bookdb no PostgreSQL

Atualize as credenciais no arquivo src/main/resources/application.properties

Execute a aplicação:

bash
mvn spring-boot:run
🖥️ Como Usar
Ao iniciar a aplicação, um menu interativo será exibido no terminal:

text
Escolha o número da sua opção:
1- Buscar livro pelo titulo
2- Listar livros registrados
3- Listar autores registrados
4- Listar autores vivos em um determinado ano
5- Listar livros em um determinado idioma
6- Sair
Exemplo de Uso
Buscar livro por título:

Digite 1 e o título do livro

Exemplo de saída:

text
-----LIVRO-----
Titulo: Dom Quixote
Autor: Miguel de Cervantes Saavedra
Idioma: es
Número de downloads: 5000
----------------------
Listar livros por idioma:

Digite 5 e o código do idioma (ex: en)

A aplicação retornará todos os livros em inglês

📦 Estrutura do Projeto
text
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── br/
│   │           └── busca_livro_app/
│   │               ├── domain/       # Entidades JPA
│   │               ├── repository/   # Repositórios
│   │               ├── service/      # Lógica de negócio
│   │               └── BuscaLivroAppApplication.java
│   └── resources/
│       └── application.properties    # Configurações
📄 Licença
Este projeto está licenciado sob a Licença MIT - veja o arquivo LICENSE para detalhes.