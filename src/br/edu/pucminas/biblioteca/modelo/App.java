package br.edu.pucminas.biblioteca.modelo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class App {

    static Scanner scanner = new Scanner(System.in);

    public static Catalogo lerCatalogo() {
        try {
            ObjectInputStream in = new ObjectInputStream(
                    new FileInputStream("catalogo.dat"));

            Catalogo catalogo = (Catalogo) in.readObject();

            in.close();

            return catalogo;

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao ler: " + e.getMessage());
            return new Catalogo("1");
        }
    }

    public static List<Aluno> lerAlunos() {
        try {
            ObjectInputStream in = new ObjectInputStream(
                    new FileInputStream("alunos.dat"));

            List<Aluno> alunos = (List<Aluno>) in.readObject();

            in.close();

            return alunos;

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao ler: " + e.getMessage());
            return new ArrayList<>();
        }

    }

    public static List<Bibliotecario> lerBibliotecarios() {
        try {
            ObjectInputStream in = new ObjectInputStream(
                    new FileInputStream("bibliotecarios.dat"));

            List<Bibliotecario> bibliotecarios = (List<Bibliotecario>) in.readObject();

            in.close();

            return bibliotecarios;

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao ler: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // public static void salvar(Catalogo catalogo, List<Bibliotecario>
    // bibliotecarios, List<Aluno> alunos) {
    public static void salvar(Catalogo catalogo) {
        try {
            FileOutputStream arquivoCatalogo = new FileOutputStream("catalogo.dat");
            // FileOutputStream arquivoBibliotecarios = new
            // FileOutputStream("bibliotecarios.dat");
            // FileOutputStream arquivoAlunos = new FileOutputStream("alunos.dat");

            ObjectOutputStream objetoCatalogo = new ObjectOutputStream(arquivoCatalogo);
            // ObjectOutputStream objetoBibliotecarios = new
            // ObjectOutputStream(arquivoBibliotecarios);
            // ObjectOutputStream objetoAlunos = new ObjectOutputStream(arquivoAlunos);

            objetoCatalogo.writeObject(catalogo);
            // objetoBibliotecarios.writeObject(bibliotecarios);
            // objetoAlunos.writeObject(alunos);

            objetoCatalogo.close();
            // objetoBibliotecarios.close();
            // objetoAlunos.close();

            arquivoCatalogo.close();
            // arquivoBibliotecarios.close();
            // arquivoAlunos.close();

            System.out.println("Catálogo salvo com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao salvar catálogo: " + e.getMessage());
            System.err.println(e);

        }
    }

    public static int lerInteiro() {
        while (true) {
            try {
                int numero = Integer.parseInt(scanner.nextLine());
                return numero;
            } catch (Exception e) {
                System.err.println("Um erro aconteceu, tente novamente.");
            }
        }
    }

    public static int menu() {
        System.out.println("\n===== SISTEMA DE EBOOKS =====");
        System.out.println("1 - Adicionar eBook");
        System.out.println("2 - Remover eBook");
        System.out.println("3 - Listar eBooks");
        System.out.println("4 - Renovar catálogo");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
        return lerInteiro();
    }

    public static EBook criarEBook() {

        System.out.println("===== CRIAR EBOOK =====");

        System.out.print("Título: ");
        String titulo = scanner.nextLine();

        System.out.print("Editora: ");
        String editora = scanner.nextLine();

        System.out.println("\nFormato:");
        System.out.println("1 - PDF");
        System.out.println("2 - EPUB");
        System.out.print("Escolha: ");

        int opcaoFormato = lerInteiro();

        Formato formato;

        if (opcaoFormato == 1) {
            formato = Formato.PDF;
        } else {
            formato = Formato.EPUB;
        }

        System.out.println("\nCategoria:");
        System.out.println("1 - Literatura");
        System.out.println("2 - Técnico");
        System.out.println("3 - Periódico");
        System.out.print("Escolha: ");

        int opcaoCategoria = lerInteiro();

        Categoria categoria = Categoria.values()[opcaoCategoria - 1];

        

        System.out.print("\nQuantidade de licenças: ");
        int quantidadeLicencas = lerInteiro();

        Licenca licenca = new Licenca(quantidadeLicencas);

        return new EBook(
                titulo,
                editora,
                formato,
                categoria,
                licenca);
    }

    public static void main(String[] args) {
        Catalogo catalogo = lerCatalogo();
        List<Bibliotecario> bibliotecarios = lerBibliotecarios();
        List<Aluno> alunos = lerAlunos();

        while (true) {
            int sair = 0;
            switch (menu()) {
                case 1:
                    catalogo.adicionarEBook(criarEBook());
                    System.err.println("Ebook Adicionado");
                    break;

                case 3:
                    for (EBook ebook : catalogo.listarEBooks()){
                        System.err.println(ebook);
                    }
                    break;

                default:
                    salvar(catalogo);
                    // salvar(catalogo, bibliotecarios, alunos);
                    sair = 1;

                    break;
            }

            if (sair == 1) {
                break;
            }
        }
    }
}
