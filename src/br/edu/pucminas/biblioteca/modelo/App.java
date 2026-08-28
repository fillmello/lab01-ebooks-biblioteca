package br.edu.pucminas.biblioteca.modelo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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

    public static void salvar(Catalogo catalogo, List<Bibliotecario> bibliotecarios, List<Aluno> alunos) {
        // public static void salvar(Catalogo catalogo) {
        try {
            FileOutputStream arquivoCatalogo = new FileOutputStream("catalogo.dat");
            FileOutputStream arquivoBibliotecarios = new FileOutputStream("bibliotecarios.dat");
            FileOutputStream arquivoAlunos = new FileOutputStream("alunos.dat");

            ObjectOutputStream objetoCatalogo = new ObjectOutputStream(arquivoCatalogo);
            ObjectOutputStream objetoBibliotecarios = new ObjectOutputStream(arquivoBibliotecarios);
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

            System.out.println("Catálogo, Aluno e bibliotecarios salvos com sucesso!");

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
        System.out.println("1 - Criar Aluno");
        System.out.println("2 - Criar Bibliotecario");
        System.out.println("3 - Login Aluno");
        System.out.println("4 - Login bibliotecario");
        System.out.println("5 - TESTES, log em tudo");

        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
        return lerInteiro();
    }

    public static int menuBibliotecario(Bibliotecario bibliotecario) {
        System.out.println("\n===== SISTEMA DE EBOOKS == " + bibliotecario.getNome() + " =====");
        System.out.println("1 - Cadastrar Ebook");
        System.out.println("2 - Consultar Aluno");
        System.out.println("3 - Renovar Catalogo");
        System.out.println("4 - Remover Ebook");

        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
        return lerInteiro();
    }

    public static void bibliotecarioConsultarAluno(List<Aluno> alunos) {
        if (alunos.isEmpty()) {
            System.err.println("Nenhum aluno cadastrado");
            return;
        }

        for (int i = 0; i < alunos.size(); i++) {
            System.out.println((i + 1) + " - " + alunos.get(i));
            System.out.println("---");
        }

        System.err.println("Qual aluno voce deseja conultar (numero)");
        int opcao = 0;
        do {
            opcao = lerInteiro();
            ;
            if (opcao < 1 || opcao > alunos.size()) {
                System.out.println("Opção inválida!");
                return;
            }
        } while (opcao < 1 || opcao > alunos.size());

        Aluno aluno = alunos.get(opcao - 1);

        System.out.println("Você selecionou:");
        System.out.println(aluno);
        System.out.println("Livros do(a) " + aluno.getNome());
        for (ItemEstante itemEstante : aluno.getEstante().listar()) {
            System.err.println(itemEstante);
            System.err.println("---");
        }
        System.out.println("Precione enter para sair");
        scanner.nextLine();
    }

    public static void bibliotecarioRemoverEbook(Catalogo catalogo) {
        if (catalogo.listarEBooks().isEmpty()) {
            System.err.println("Nenhum livro cadastrado");
            return;
        }
        for (int i = 0; i < catalogo.listarEBooks().size(); i++) {
            System.out.println((i + 1) + " - " + catalogo.listarEBooks().get(i));
            System.out.println("---");
        }

        System.err.println("Qual ebook voce deseja remover (numero)");
        int opcao = 0;
        do {
            opcao = lerInteiro();
            ;
            if (opcao < 1 || opcao > catalogo.listarEBooks().size()) {
                System.out.println("Opção inválida!");
                return;
            }
        } while (opcao < 1 || opcao > catalogo.listarEBooks().size());

        EBook ebook = catalogo.listarEBooks().get(opcao - 1);

        System.out.println("Você selecionou:");
        System.out.println(ebook);
        System.out.println("Digite 1 para remover livro, digite outro valor para cancelar");
        String valor = scanner.nextLine();
        if (valor.equals("1")) {
            catalogo.removerEBook(ebook);
        }

        // quando um bibliotecario remover um ebook deve excluir nos alunos?
    }

    public static void bibliotecarioAdicionaPeriodoDeAcesso(Catalogo catalogo) {
        
        Boolean correto = false;
        LocalDate dataInicio = null;
        LocalDate dataFim = null;
        
        while (!correto) {
            try {
                System.err.println("Data de inicio: (dd/MM/yyyy)");
                String dataTexto = scanner.nextLine();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                dataInicio = LocalDate.parse(dataTexto, formatter);

                System.err.println("Data de fim: (dd/MM/yyyy)");
                dataTexto = scanner.nextLine();
                formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                dataFim = LocalDate.parse(dataTexto, formatter);

                correto = true;
            } catch (Exception e){
                System.err.println("Um erro aconteceu com a data, tente novamente");
                System.err.println("Digite '1' para sair, outro valor para repetir");
                if (scanner.nextLine() == "1"){
                    return;
                }
            }
        }

        catalogo.adicionarPeriodoDeAcesso(dataInicio, dataFim);
        // quando um bibliotecario remover um ebook deve excluir nos alunos?
    }

    public static void funcoesBibliotecario(Bibliotecario bibliotecario, List<Aluno> alunos, Catalogo catalogo)
            throws Exception {
        int sair = 0;
        while (sair == 0) {
            try {
                switch (menuBibliotecario(bibliotecario)) {
                    case 1:
                        bibliotecario.cadastrarEBook(criarEBook(), catalogo);
                        break;

                    case 2:
                        bibliotecarioConsultarAluno(alunos);
                        break;

                    case 3:
                        catalogo.renovar(alunos);
                        System.err.println("Assim ficou os ebook da biblioteca");
                        for (EBook ebook : catalogo.listarEBooks()) {
                            System.err.println(ebook);
                            System.err.println("---");
                        }
                        break;

                    case 4:
                        bibliotecarioRemoverEbook(catalogo);
                        break;

                    case 5:
                        bibliotecarioAdicionaPeriodoDeAcesso(catalogo);
                        break;

                    default:
                        sair = 1;
                        break;
                }
            } catch (Exception e) {
                System.err.println("Um erro aconteceu");
                System.err.println(e.getMessage());
            }
        }
    }

    public static int menuAluno(Aluno aluno) {
        System.out.println("\n===== SISTEMA DE EBOOKS == " + aluno.getNome() + "=====");
        System.out.println("1 - Adicionar livros");
        System.out.println("2 - Listar seus livros");
        System.out.println("3 - Acessar Ebook");
        System.out.println("4 - Remover Ebook");

        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
        return lerInteiro();
    }

    public static void alunoListarLivros(Aluno aluno) {
        if (aluno.getEstante().listar().isEmpty()) {
            System.err.println("Voce nao possui livros na estante");
            return;
        }

        System.err.println("Livros de: " + aluno.getNome());
        System.err.println(aluno.listarEbooks());
    }

    public static void alunoAcessarEbook(Aluno aluno) {
        if (aluno.getEstante().listar().isEmpty()) {
            System.err.println("Voce nao possui livros na estante");
            return;
        }

        for (int i = 0; i < aluno.getEstante().listar().size(); i++) {
            System.out.println((i + 1) + " - " + aluno.getEstante().listar().get(i));
            System.out.println("---");
        }

        System.err.println("Qual ebook voce deseja selecionar (numero)");
        int opcao = 0;
        do {
            opcao = lerInteiro();
            ;
            if (opcao < 1 || opcao > aluno.getEstante().listar().size()) {
                System.out.println("Opção inválida!");
                return;
            }
        } while (opcao < 1 || opcao > aluno.getEstante().listar().size());

        ItemEstante itemEstanteSelecionado = aluno.getEstante().listar().get(opcao - 1);

        System.out.println("Você selecionou:");
        System.out.println(itemEstanteSelecionado);
        System.out.println("Precione enter para sair");
        scanner.nextLine();

    }

    public static void alunoRemoveEbook(Aluno aluno) {
        if (aluno.getEstante().listar().isEmpty()) {
            System.err.println("Voce nao possui livros na estante");
            return;
        }
        for (int i = 0; i < aluno.getEstante().listar().size(); i++) {
            System.out.println((i + 1) + " - " + aluno.getEstante().listar().get(i));
            System.out.println("---");
        }

        System.err.println("Qual ebook voce deseja remover (numero)");
        int opcao = 0;
        do {
            opcao = lerInteiro();
            ;
            if (opcao < 1 || opcao > aluno.getEstante().listar().size()) {
                System.out.println("Opção inválida!");
                return;
            }
        } while (opcao < 1 || opcao > aluno.getEstante().listar().size());

        ItemEstante itemEstanteSelecionado = aluno.getEstante().listar().get(opcao - 1);

        System.out.println("Você selecionou:");
        System.out.println(itemEstanteSelecionado);
        System.out.println("Digite 1 para remover livro, digite outro valor para cancelar");
        String valor = scanner.nextLine();
        if (valor.equals("1")) {
            aluno.removerEBook(itemEstanteSelecionado.getEBook());
        }

    }

    public static void alunoAddLivro(Aluno aluno, Catalogo catalogo) {
        for (int i = 0; i < catalogo.listarEBooks().size(); i++) {
            System.out.println((i + 1) + " - " + catalogo.listarEBooks().get(i));
            System.out.println("---");
        }

        System.err.println("Qual ebook voce deseja adicionar (numero)");
        int opcao = 0;
        do {
            opcao = lerInteiro();
            ;
            if (opcao < 1 || opcao > catalogo.listarEBooks().size()) {
                System.out.println("Opção inválida!");
                return;
            }
        } while (opcao < 1 || opcao > catalogo.listarEBooks().size());

        EBook ebookSelecionado = catalogo.listarEBooks().get(opcao - 1);

        System.out.println("Você selecionou:");
        System.out.println(ebookSelecionado);

        System.out.println("Selecione o tipo de leitura:");

        System.out.println("1 - Obrigatória");
        System.out.println("2 - Livre");

        opcao = 0;
        do {
            opcao = lerInteiro();
        } while (opcao < 1 || opcao > 2);

        aluno.adicionarEBook(ebookSelecionado, opcao == 1 ? TipoLeitura.OBRIGATORIA : TipoLeitura.LIVRE);
    }

    public static void funcoesAluno(Aluno aluno, Catalogo catalogo) {
        int sair = 0;
        while (sair == 0) {
            try {
                switch (menuAluno(aluno)) {
                    case 1:
                        alunoAddLivro(aluno, catalogo);
                        break;

                    case 2:
                        alunoListarLivros(aluno);
                        break;

                    case 3:
                        alunoAcessarEbook(aluno);
                        break;

                    case 4:
                        alunoRemoveEbook(aluno);
                        break;

                    default:
                        sair = 1;
                        break;
                }

            } catch (Exception e) {
                System.err.println("Um erro aconteceu");
                System.err.println(e.getMessage());
            }
        }
    }

    public static void loginBibliotecario(List<Bibliotecario> bibliotecarios, List<Aluno> alunos, Catalogo catalogo) {
        String registro = "";
        do {
            System.out.print("Digite sua matricula: ");
            registro = scanner.nextLine();
        } while (registro.isBlank());

        String senha = "";
        do {
            System.out.print("Digite sua senha: ");
            senha = scanner.nextLine();
        } while (senha.isBlank());

        final String registroFinal = registro;
        final String senhaFinal = senha;

        Optional<Bibliotecario> biblitecarioTentativa = bibliotecarios.stream()
                .filter(bibliotecatio -> bibliotecatio.validiar(senhaFinal, registroFinal))
                .findFirst();

        System.err.println("bibliotecario: " + biblitecarioTentativa);
        if (biblitecarioTentativa.isEmpty()) {
            System.err.println("Não foi possivel encontrar nenhum bibliotecario, tenta novamente");
            return;
        }

        try {
            funcoesBibliotecario(biblitecarioTentativa.get(), alunos, catalogo);
        } catch (Exception e) {
            System.err.println("Um erro inesperado aconteceu, tente novamnete");
            return;
        }
    }

    public static void loginAluno(List<Aluno> alunos, Catalogo catalogo) {
        String matricula = "";
        do {
            System.out.print("Digite sua matricula: ");
            matricula = scanner.nextLine();
        } while (matricula.isBlank());

        String senha = "";
        do {
            System.out.print("Digite sua senha: ");
            senha = scanner.nextLine();
        } while (senha.isBlank());

        final String matriculaFinal = matricula;
        final String senhaFinal = senha;

        Optional<Aluno> alunoTentativa = alunos.stream().filter(aluno -> aluno.validiar(senhaFinal, matriculaFinal))
                .findFirst();

        System.err.println("aluno: " + alunoTentativa);
        if (alunoTentativa.isEmpty()) {
            System.err.println("Não foi possivel encontrar nenhum aluno, tenta novamente");
            return;
        }

        try {
            funcoesAluno(alunoTentativa.get(), catalogo);
        } catch (Exception e) {
            System.err.println("Um erro inesperado aconteceu, tente novamnete");
            return;
        }
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
                    loginAluno(alunos, catalogo);
                    break;

                case 4:
                    loginBibliotecario(bibliotecarios, alunos, catalogo);
                    break;

                case 5:
                    for (Aluno aluno : alunos) {
                        System.err.println(aluno);
                    }
                    System.err.println("FIM ALUNOS ---");
                    for (Bibliotecario aluno : bibliotecarios) {
                        System.err.println(aluno);
                    }
                    System.err.println("FIM BIBLIO ---");
                    for (EBook aluno : catalogo.listarEBooks()) {
                        System.err.println(aluno);
                    }
                    System.err.println("FIM LIVROS ---");
                    break;

                default:
                    salvar(catalogo, bibliotecarios, alunos);
                    sair = 1;
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
