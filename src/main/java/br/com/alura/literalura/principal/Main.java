package br.com.alura.literalura.principal;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.com.alura.literalura.model.Author;
import br.com.alura.literalura.model.Book;
import br.com.alura.literalura.model.BookResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

    private List<Book> catalog = new ArrayList<>();
    private List<Author> authors = new ArrayList<>();

    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Consultar livro por título");
            System.out.println("2. Listar todos os livros");
            System.out.println("3. Listar autores");
            System.out.println("4. Listar autores vivos em um determinado ano");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Digite o título do livro: ");
                    String title = scanner.nextLine();
                    consultarLivroPorTitulo(title);
                    break;
                case 2:
                    listarTodosOsLivros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    System.out.print("Digite o ano para verificar os autores vivos: ");
                    int year = scanner.nextInt();
                    scanner.nextLine();  // Consume newline left-over
                    listarAutoresVivos(year);
                    break;
                case 5:
                    System.out.println("Saindo...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void consultarLivroPorTitulo(String title) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://gutendex.com/books/?search=" + title.replaceAll("\\s", "+")))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                BookResponse bookResponse = objectMapper.readValue(response.body(), BookResponse.class);

                if (!bookResponse.getResults().isEmpty()) {
                    Book book = bookResponse.getResults().get(0);
                    catalog.add(book);

                    // Assume only one author per book for simplicity
                    Author author = book.getAuthors().get(0);
                    authors.add(author);

                    System.out.println("Livro adicionado ao catálogo:");
                    System.out.println(book);
                } else {
                    System.out.println("Nenhum livro encontrado com o título: " + title);
                }
            } else {
                System.out.println("Erro HTTP: " + response.statusCode());
            }

        } catch (Exception e) {
            System.out.println("Erro ao consultar livro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void listarTodosOsLivros() {
        if (catalog.isEmpty()) {
            System.out.println("Nenhum livro no catálogo.");
        } else {
            System.out.println("Listagem de todos os livros:");
            for (Book book : catalog) {
                System.out.println(book);
            }
        }
    }

    private void listarAutores() {
        if (authors.isEmpty()) {
            System.out.println("Nenhum autor no catálogo.");
        } else {
            System.out.println("Listagem de todos os autores:");
            for (Author author : authors) {
                System.out.println(author);
            }
        }
    }

    private void listarAutoresVivos(int year) {
        if (authors.isEmpty()) {
            System.out.println("Nenhum autor no catálogo.");
        } else {
            System.out.println("Autores vivos em " + year + ":");
            for (Author author : authors) {
                if (author.isAlive(year)) {
                    System.out.println(author);
                }
            }
        }
    }

}
