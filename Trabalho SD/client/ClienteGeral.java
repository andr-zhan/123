package client;

import server.BibliotecaService;
import server.Livro;

import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;

public class ClienteGeral {

    public static void main(String[] args) {
        try {
            BibliotecaService service = (BibliotecaService) Naming.lookup("rmi://localhost:1099/BibliotecaService");

            Scanner scanner = new Scanner(System.in);
            int opcao;

            do {
                System.out.println("\n====== CLIENTE GERAL ======");
                System.out.println("1 - Registar utilizador");
                System.out.println("2 - Registar livro");
                System.out.println("3 - Realizar empréstimo");
                System.out.println("4 - Listar livros disponíveis");
                System.out.println("5 - Consultar estado de um livro");
                System.out.println("6 - Consultar histórico de um livro");
                System.out.println("0 - Sair");
                System.out.print("Opção: ");
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        System.out.print("Nome: ");
                        String nome = scanner.nextLine();
                        System.out.print("Email: ");
                        String email = scanner.nextLine();
                        System.out.print("Tipo (geral/admin): ");
                        String tipo = scanner.nextLine();

                        boolean okU = service.registarUtilizador(nome, email, tipo);
                        System.out.println(okU ? "✅ Utilizador registado!" : "❌ Falha ao registar");
                        break;

                    case 2:
                        System.out.print("Título: ");
                        String titulo = scanner.nextLine();
                        System.out.print("Autor: ");
                        String autor = scanner.nextLine();
                        System.out.print("Categoria: ");
                        String categoria = scanner.nextLine();

                        boolean okL = service.registarLivro(titulo, autor, categoria);
                        System.out.println(okL ? "✅ Livro registado!" : "❌ Falha ao registar");
                        break;

                    case 3:
                        System.out.print("ID do utilizador: ");
                        int uID = Integer.parseInt(scanner.nextLine());
                        System.out.print("ID do livro: ");
                        int lID = Integer.parseInt(scanner.nextLine());

                        boolean okE = service.realizarEmprestimo(uID, lID);
                        System.out.println(okE ? "✅ Empréstimo realizado!" : "❌ Falha no empréstimo");
                        break;

                    case 4:
                        System.out.print("Categoria (enter para ignorar): ");
                        String cf = scanner.nextLine();
                        System.out.print("Autor (enter para ignorar): ");
                        String af = scanner.nextLine();

                        List<Livro> livros = service.listarLivrosDisponiveis(
                                cf.isBlank() ? null : cf,
                                af.isBlank() ? null : af
                        );

                        System.out.println("\n--- Livros disponíveis ---");
                        for (Livro lv : livros) {
                            System.out.println(lv);
                        }
                        break;

                    case 5:
                        System.out.print("ID do livro: ");
                        int idEstado = Integer.parseInt(scanner.nextLine());
                        System.out.println("Estado: " + service.consultarEstadoLivro(idEstado));
                        break;

                    case 6:
                        System.out.print("ID do livro: ");
                        int idHist = Integer.parseInt(scanner.nextLine());

                        List<String> hist = service.consultarHistoricoLivro(idHist);
                        System.out.println("\n--- Histórico ---");
                        hist.forEach(System.out::println);
                        break;
                }

            } while (opcao != 0);

            System.out.println("Cliente encerrado.");

        } catch (Exception e) {
            System.out.println("Erro no cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
