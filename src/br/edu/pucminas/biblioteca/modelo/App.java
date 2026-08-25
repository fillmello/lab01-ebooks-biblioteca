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

    public static void salvar(Catalogo catalogo, List<Bibliotecario>
    bibliotecarios, List<Aluno> alunos) {
    // public static void salvar(Catalogo catalogo) {
        try {
            FileOutputStream arquivoCatalogo = new FileOutputStream("catalogo.dat");
            FileOutputStream arquivoBibliotecarios = new
            FileOutputStream("bibliotecarios.dat");
            FileOutputStream arquivoAlunos = new FileOutputStream("alunos.dat");

            ObjectOutputStream objetoCatalogo = new ObjectOutputStream(arquivoCatalogo);
            ObjectOutputStream objetoBibliotecarios = new
            ObjectOutputStream(arquivoBibliotecarios);
            ObjectOutputStream objetoAlunos = new ObjectOutputStream(arquivoAlunos);

            objetoCatalogo.writeObject(catalogo);
            objetoBibliotecarios.writeObject(bibliotecarios);
            objetoAlunos.writeObject(alunos);

            objetoCatalogo.close();
            objetoBibliotecarios.close();
            objetoAlunos.close();

            arquivoCatalogo.close();
            arquivoBibliotecarios.close();
            arquivoAlunos.close();

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
        System.out.println("1 - Login Aluno");
        System.out.println("2 - Login Bibliotecario");
        System.out.println("3 - Criar conta Aluno");
        System.out.println("4 - Criar conta bibliotecario");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
        return lerInteiro();
    }

    public static int menuBibliotecario(Bibliotecario bibliotecario) {
        System.out.println("\n===== SISTEMA DE EBOOKS "+ bibliotecario.getNome()+" =====");
        System.out.println("1 - Cadastrar Ebook");

        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
        return lerInteiro();
    }

    public static void funcoesBibliotecario(Bibliotecario bibliotecario, Catalogo catalogo){
        while (true) {
            switch (menuBibliotecario(bibliotecario)) {
                case 1:
                    bibliotecario.cadastrarEBook(criarEBook(), catalogo);
                    break;
            
                default:
                    break;
            }
        }
    }

    public static int menuAluno(Aluno aluno) {
        System.out.println("\n===== SISTEMA DE EBOOKS"+ aluno.getNome() +"=====");
        System.out.println("1 - ");
        
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
        return lerInteiro();
    }

    public static EBook criarEBook() {

        System.out.println("===== CRIAR EBOOK =====");

        String titulo = "";
        do {
            System.out.print("Titulo: ");
            titulo = scanner.nextLine();
        } while (titulo.isBlank());

        String editora = "";
        do {
            System.out.print("Editora: ");
            editora = scanner.nextLine();
        } while (editora.isBlank());

        int opcao = 0;
        do {
            System.out.println("\nFormato:");
            System.out.println("1 - PDF");
            System.out.println("2 - EPUB");
            System.out.print("Escolha: ");
            opcao = lerInteiro();
        } while (opcao > 2 || opcao < 1);
        Formato formato = Formato.values()[opcao - 1];

        opcao = 0;
        do {
            System.out.println("\nCategoria:");
            System.out.println("1 - Literatura");
            System.out.println("2 - Técnico");
            System.out.println("3 - Periódico");
            System.out.print("Escolha: ");
            opcao = lerInteiro();
        } while (opcao > 3 || opcao < 1);
        Categoria categoria = Categoria.values()[opcao - 1];

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

    public static Aluno criarAluno() {

        System.out.println("===== CADASTRAR ALUNO =====");

        // FIXME
        System.out.print("ID: ");
        String id = scanner.nextLine();

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        System.out.print("Matrícula: ");
        String matricula = scanner.nextLine();

        return new Aluno(
                id,
                nome,
                senha,
                matricula);
    }

    public static Bibliotecario criarBibliotecario() {

        System.out.println("===== CADASTRAR BIBLIOTECÁRIO =====");


        // FIXME
        System.out.print("ID: ");
        String id = scanner.nextLine();

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        System.out.print("Registro funcional: ");
        String registroFuncional = scanner.nextLine();

        return new Bibliotecario(
                id,
                nome,
                senha,
                registroFuncional);
    }

    public static void main(String[] args) {
        Catalogo catalogo = lerCatalogo();
        List<Bibliotecario> bibliotecarios = lerBibliotecarios();
        List<Aluno> alunos = lerAlunos();

        while (true) {
            int sair = 0;
            switch (menu()) {
                case 1:
                    alunos.add(criarAluno());
                    break;

                case 2:
                    bibliotecarios.add(criarBibliotecario());
                    break;

                case 3:
                    
                    break;

                case 4:
                    
                    break;

                default:
                    salvar(catalogo, bibliotecarios, alunos);
                    break;
            }

            if (sair == 1) {
                break;
            }
        }

        // while (true) {
        // int sair = 0;
        // switch (menu()) {
        // case 1:
        // catalogo.adicionarEBook(criarEBook());
        // System.err.println("Ebook Adicionado");
        // break;

        // case 3:
        // for (EBook ebook : catalogo.listarEBooks()){
        // System.err.println("---");
        // System.err.println(ebook);
        // }
        // break;

        // default:
        // salvar(catalogo);
        // // salvar(catalogo, bibliotecarios, alunos);
        // sair = 1;

        // break;
        // }

        // if (sair == 1) {
        // break;
        // }
        // }
    }
}
