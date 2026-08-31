package br.edu.pucminas.biblioteca;

import br.edu.pucminas.biblioteca.modelo.*;
import br.edu.pucminas.biblioteca.persistencia.BibliotecaRepositorioArquivo;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

/** Interface de linha de comando do sistema de gestao de eBooks. */
public class MenuPrincipal {

    static Scanner scanner = new Scanner(System.in);
    static BibliotecaRepositorioArquivo repositorio = new BibliotecaRepositorioArquivo();

    static Catalogo catalogo;
    static List<Usuario> usuarios;
    static SistemaEstatisticas estatisticas;

    public static void main(String[] args) {
        carregarDados();
        System.out.println("Semestre " + catalogo.getSemestre() + ", data de hoje: " + LocalDate.now());

        try {
            int sair = 0;
            while (sair == 0) {
                switch (menu()) {
                    case 1:
                        login();
                        break;

                    case 2:
                        criarLogin();
                        break;

                    case 0:
                        sair = 1;
                        break;

                    default:
                        System.out.println("Opcao invalida, tente novamente.");
                        break;
                }
            }
        } catch (NoSuchElementException e) {
            // A entrada acabou, o que acontece quando o programa e alimentado por um arquivo.
        }
        System.out.println("Sistema encerrado.");
        scanner.close();
    }

    public static int menu() {
        System.out.println("\n===== SISTEMA DE EBOOKS =====");
        System.out.println("1 - Entrar no sistema");
        System.out.println("2 - Criar login de aluno");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opcao: ");
        return lerInteiro();
    }

    // ===== Carga e gravacao dos dados =====

    public static void carregarDados() {
        try {
            aplicar(repositorio.carregar());
        } catch (RuntimeException | IOException e) {
            System.out.println("Nao foi possivel ler os dados gravados: " + e.getMessage());
            System.out.println("O sistema continua com os dados iniciais, sem gravar em disco.");
            aplicar(repositorio.dadosIniciais());
        }
    }

    public static void aplicar(BibliotecaRepositorioArquivo.Dados dados) {
        catalogo = dados.catalogo();
        usuarios = new ArrayList<>(dados.usuarios());
        estatisticas = dados.estatisticas();
    }

    public static void salvarDados() {
        try {
            repositorio.salvar(catalogo, usuarios, estatisticas);
        } catch (IOException e) {
            System.out.println("Nao foi possivel gravar os dados: " + e.getMessage());
        }
    }

    // ===== UC01 - Realizar login =====

    public static void login() {
        System.out.print("Id do usuario: ");
        String id = scanner.nextLine().trim();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        Usuario usuario = buscarUsuario(id);
        if (usuario == null || !usuario.autenticar(senha)) {
            System.out.println("Id ou senha invalidos.");
            return;
        }
        System.out.println("Acesso liberado. Bem-vindo(a), " + usuario.getNome() + ".");
        if (usuario instanceof Aluno aluno) {
            funcoesAluno(aluno);
        } else {
            funcoesBibliotecario((Bibliotecario) usuario);
        }
    }


    public static void criarLogin() {
        System.out.println("\n===== CRIAR LOGIN DE ALUNO =====");
        if (cadastrarUsuario("ALUNO")) {
            System.out.println("Login criado. Agora entre no sistema com o id informado.");
        }
    }

    /**
     * Pergunta os dados de um usuario novo e o acrescenta ao sistema. Devolve false
     * quando o id ou o vinculo ja estao em uso, ou quando algum campo tem o separador
     * dos arquivos de dados.
     */
    public static boolean cadastrarUsuario(String perfil) {
        boolean ehBibliotecario = "BIBLIOTECARIO".equals(perfil);
        String id = perguntar("Id de login");
        String nome = perguntar("Nome completo");
        String vinculo = perguntar(ehBibliotecario ? "Registro funcional" : "Matricula");
        String senha = perguntar("Senha");

        if (temSeparador(id, nome, vinculo, senha)) {
            return false;
        }
        if (buscarUsuario(id) != null) {
            System.out.println("Ja existe um usuario com o id \"" + id + "\".");
            return false;
        }
        if (vinculoEmUso(vinculo)) {
            System.out.println("Ja existe um usuario com "
                    + (ehBibliotecario ? "o registro funcional " : "a matricula ") + vinculo + ".");
            return false;
        }
        usuarios.add(ehBibliotecario ? new Bibliotecario(id, nome, senha, vinculo)
                : new Aluno(id, nome, senha, vinculo));
        salvarDados();
        return true;
    }

    /** A matricula do aluno e o registro funcional do bibliotecario nao podem se repetir. */
    public static boolean vinculoEmUso(String vinculo) {
        return usuarios.stream().anyMatch(u -> vinculo.equals(u instanceof Aluno aluno
                ? aluno.getMatricula() : ((Bibliotecario) u).getRegistroFuncional()));
    }

    // ===== Menu do aluno (UC02, UC03 e UC04) =====

    public static int menuAluno(Aluno aluno) {
        System.out.println("\n===== SISTEMA DE EBOOKS == " + aluno.getNome() + " =====");
        System.out.println("1 - Consultar catalogo do semestre");
        System.out.println("2 - Adicionar eBook a estante");
        System.out.println("3 - Remover eBook da estante");
        System.out.println("4 - Listar minha estante");
        System.out.println("5 - Acessar eBook");
        System.out.println("6 - Encerrar acesso a eBook");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opcao: ");
        return lerInteiro();
    }

    public static void funcoesAluno(Aluno aluno) {
        List<EBook> acessosAbertos = new ArrayList<>();
        int sair = 0;
        while (sair == 0) {
            try {
                switch (menuAluno(aluno)) {
                    case 1:
                        consultarCatalogo();
                        break;

                    case 2:
                        alunoAdicionaEBook(aluno);
                        break;

                    case 3:
                        alunoRemoveEBook(aluno);
                        break;

                    case 4:
                        alunoListaEstante(aluno);
                        break;

                    case 5:
                        alunoAcessaEBook(aluno, acessosAbertos);
                        break;

                    case 6:
                        alunoEncerraAcesso(aluno, acessosAbertos);
                        break;

                    case 0:
                        sair = 1;
                        break;

                    default:
                        System.out.println("Opcao invalida, tente novamente.");
                        break;
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Nao foi possivel concluir a acao: " + e.getMessage());
            }
        }
        acessosAbertos.forEach(aluno::encerrarAcessoEBook);
    }

    /** UC02 - Adicionar eBook a estante, que inclui UC07 - Registrar estatistica de uso. */
    public static void alunoAdicionaEBook(Aluno aluno) {
        LocalDate hoje = LocalDate.now();
        EBook ebook = selecionar("Qual eBook voce deseja adicionar", catalogo.listarEBooks());
        if (ebook == null) {
            return;
        }
        if (!catalogo.podeAdicionar(ebook, hoje)) {
            System.out.println("Fora do periodo de acesso: a estante nao pode ser alterada hoje.");
            return;
        }
        Estante estante = aluno.getEstante();
        System.out.println("\nTipo de leitura (" + estante.vagasRestantes(TipoLeitura.OBRIGATORIA)
                + " vaga(s) obrigatoria(s), " + estante.vagasRestantes(TipoLeitura.LIVRE) + " livre(s))");
        TipoLeitura tipo = selecionar("Escolha o tipo", List.of(TipoLeitura.values()));
        if (tipo == null) {
            return;
        }
        if (aluno.adicionarEBook(ebook, tipo, catalogo.periodoAbertoEm(hoje), hoje, estatisticas)) {
            System.out.println("eBook adicionado. Estatistica de uso registrada: "
                    + estatisticas.consultarTotalAdicoes(ebook) + " adicao(oes) no total.");
            salvarDados();
        } else {
            System.out.println("Nao foi possivel adicionar: o eBook ja esta na estante ou o limite de "
                    + "leitura " + tipo.toString().toLowerCase() + " foi atingido.");
        }
    }

    /** UC03 - Remover eBook da estante. */
    public static void alunoRemoveEBook(Aluno aluno) {
        LocalDate hoje = LocalDate.now();
        ItemEstante item = selecionar("Qual eBook voce deseja remover", aluno.getEstante().listar());
        if (item == null) {
            return;
        }
        if (aluno.removerEBook(item.getEBook(), catalogo.periodoAbertoEm(hoje), hoje)) {
            System.out.println("eBook removido. A vaga do tipo correspondente esta livre.");
            salvarDados();
        } else {
            System.out.println("Fora do periodo de acesso: a estante nao pode ser alterada hoje.");
        }
    }

    public static void alunoListaEstante(Aluno aluno) {
        Estante estante = aluno.getEstante();
        System.out.println("\nEstante de " + aluno.getNome() + ", matricula " + aluno.getMatricula());
        if (estante.listar().isEmpty()) {
            System.out.println("Voce nao possui livros na estante.");
            return;
        }
        for (ItemEstante item : estante.listar()) {
            System.out.println("- " + item);
        }
        System.out.println("Obrigatorios: " + estante.contarPorTipo(TipoLeitura.OBRIGATORIA)
                + "/4, livres: " + estante.contarPorTipo(TipoLeitura.LIVRE) + "/2");
    }

    /**
     * UC04 - Acessar eBook
     */
    public static void alunoAcessaEBook(Aluno aluno, List<EBook> acessosAbertos) {
        ItemEstante item = selecionar("Qual eBook voce deseja acessar", aluno.getEstante().listar());
        if (item == null) {
            return;
        }
        EBook ebook = item.getEBook();
        if (aluno.acessarEBook(ebook)) {
            acessosAbertos.add(ebook);
            System.out.println("Acesso concedido. Licencas em uso: " + situacaoLicenca(ebook) + ".");
        } else {
            System.out.println("Acesso bloqueado: as " + ebook.getLicenca().getLimiteAcessosSimultaneos()
                    + " licenca(s) simultanea(s) desse eBook estao em uso.");
        }
    }

    public static void alunoEncerraAcesso(Aluno aluno, List<EBook> acessosAbertos) {
        EBook ebook = selecionar("Qual acesso voce deseja encerrar", acessosAbertos);
        if (ebook == null) {
            return;
        }
        acessosAbertos.remove(ebook);
        aluno.encerrarAcessoEBook(ebook);
        System.out.println("Acesso encerrado. Licencas em uso: " + situacaoLicenca(ebook) + ".");
    }

    // ===== Menu do bibliotecario (UC05, UC06, UC07 e UC08) =====

    public static int menuBibliotecario(Bibliotecario bibliotecario) {
        System.out.println("\n===== SISTEMA DE EBOOKS == " + bibliotecario.getNome() + " =====");
        System.out.println("1 - Cadastrar eBook no catalogo");
        System.out.println("2 - Consultar catalogo do semestre");
        System.out.println("3 - Adicionar periodo de acesso");
        System.out.println("4 - Consultar alunos com um eBook");
        System.out.println("5 - Ver estatisticas de uso");
        System.out.println("6 - Renovar catalogo do semestre");
        System.out.println("7 - Cadastrar usuario");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opcao: ");
        return lerInteiro();
    }

    public static void funcoesBibliotecario(Bibliotecario bibliotecario) {
        int sair = 0;
        while (sair == 0) {
            try {
                switch (menuBibliotecario(bibliotecario)) {
                    case 1:
                        bibliotecarioCadastraEBook(bibliotecario);
                        break;

                    case 2:
                        consultarCatalogo();
                        break;

                    case 3:
                        bibliotecarioAdicionaPeriodoAcesso();
                        break;

                    case 4:
                        bibliotecarioConsultaAlunos(bibliotecario);
                        break;

                    case 5:
                        verEstatisticas();
                        break;

                    case 6:
                        bibliotecarioRenovaCatalogo(bibliotecario);
                        break;

                    case 7:
                        bibliotecarioCadastraUsuario();
                        break;

                    case 0:
                        sair = 1;
                        break;

                    default:
                        System.out.println("Opcao invalida, tente novamente.");
                        break;
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Nao foi possivel concluir a acao: " + e.getMessage());
            }
        }
    }

    /** UC05 - Cadastrar eBook. */
    public static void bibliotecarioCadastraEBook(Bibliotecario bibliotecario) {
        System.out.println("\n===== CADASTRAR EBOOK =====");
        String titulo = perguntar("Titulo");
        String editora = perguntar("Editora");
        if (temSeparador(titulo, editora)) {
            return;
        }
        Formato formato = selecionar("Formato", List.of(Formato.values()));
        Categoria categoria = selecionar("Categoria", List.of(Categoria.values()));
        if (formato == null || categoria == null) {
            return;
        }
        System.out.print("Limite de acessos simultaneos (ate "
                + Licenca.LIMITE_MAXIMO_SIMULTANEOS + "): ");
        Licenca licenca = new Licenca(lerInteiro());

        bibliotecario.cadastrarEBook(new EBook(titulo, editora, formato, categoria, licenca), catalogo);
        System.out.println("eBook cadastrado no catalogo do semestre " + catalogo.getSemestre() + ".");
        salvarDados();
    }

    public static void bibliotecarioAdicionaPeriodoAcesso() {
        try {
            catalogo.adicionarPeriodo(new PeriodoAcesso(
                    LocalDate.parse(perguntar("Data de inicio (aaaa-mm-dd)")),
                    LocalDate.parse(perguntar("Data de fim (aaaa-mm-dd)"))));
        } catch (DateTimeParseException e) {
            System.out.println("Data invalida. Use o formato aaaa-mm-dd, por exemplo 2026-08-01.");
            return;
        }
        System.out.println("Periodo de acesso adicionado.");
        salvarDados();
    }

    /** UC06 - Consultar alunos com um eBook. */
    public static void bibliotecarioConsultaAlunos(Bibliotecario bibliotecario) {
        EBook ebook = selecionar("Qual eBook voce deseja consultar", catalogo.listarEBooks());
        if (ebook == null) {
            return;
        }
        List<Aluno> encontrados = bibliotecario.consultarAlunosComEBook(ebook, alunos());
        if (encontrados.isEmpty()) {
            System.out.println("Nenhum aluno tem esse eBook na estante.");
            return;
        }
        System.out.println("Alunos com \"" + ebook.getTitulo() + "\" na estante:");
        encontrados.forEach(a -> System.out.println("- " + a.getNome() + ", matricula " + a.getMatricula()));
    }

    /** UC07 - Consulta das estatisticas de uso acumuladas. */
    public static void verEstatisticas() {
        Map<String, Integer> adicoes = estatisticas.listarAdicoes();
        System.out.println("\nEstatisticas de uso, adicoes a estantes por titulo:");
        if (adicoes.isEmpty()) {
            System.out.println("Nenhuma adicao registrada ate agora.");
            return;
        }
        adicoes.forEach((titulo, total) -> System.out.println("- " + titulo + ": " + total + " adicao(oes)"));
    }

    /** UC08 - Renovar catalogo do semestre. */
    public static void bibliotecarioRenovaCatalogo(Bibliotecario bibliotecario) {
        if (!catalogo.periodoDeAcessoEncerrado(LocalDate.now())) {
            System.out.println("Atencao: o periodo de acesso ainda nao terminou.");
            System.out.println("A renovacao vai considerar as estantes como estao hoje.");
            System.out.print("Digite 1 para confirmar, outro valor para cancelar: ");
            if (!"1".equals(scanner.nextLine().trim())) {
                System.out.println("Renovacao cancelada.");
                return;
            }
        }
        List<Aluno> alunos = alunos();
        List<EBook> anteriores = new ArrayList<>(catalogo.listarEBooks());
        Catalogo renovado = bibliotecario.renovarCatalogo(catalogo, alunos);

        System.out.println("\nMinimo para renovar a licenca: " + Catalogo.getMinimoAlunosRenovacao()
                + " aluno(s) com o titulo na estante.");
        for (EBook ebook : anteriores) {
            System.out.println("- " + ebook.getTitulo() + ": " + catalogo.contarAlunosCom(ebook, alunos)
                    + " aluno(s) -> " + (renovado.listarEBooks().contains(ebook) ? "licenca renovada"
                            : "licenca nao renovada, titulo removido do catalogo"));
        }

        catalogo = renovado;
        int descartados = alunos.stream()
                .mapToInt(a -> a.getEstante().descartarNaoLicenciados(catalogo.listarEBooks())).sum();
        salvarDados();
        System.out.println("Catalogo do semestre " + catalogo.getSemestre() + " gerado com "
                + catalogo.listarEBooks().size() + " titulo(s). Itens retirados das estantes: "
                + descartados + ".");
        System.out.println("Adicione o periodo de acesso do novo semestre para liberar as estantes.");
    }

    /**
     * Cadastro de aluno ou de bibliotecario, feito por quem ja esta logado como
     * bibliotecario. E o unico caminho para criar um bibliotecario novo: pelo
     * enunciado, quem mantem os dados dos usuarios e a equipe da biblioteca.
     */
    public static void bibliotecarioCadastraUsuario() {
        System.out.println("\n===== CADASTRAR USUARIO =====");
        String perfil = selecionar("Perfil do novo usuario", List.of("ALUNO", "BIBLIOTECARIO"));
        if (perfil == null) {
            return;
        }
        if (cadastrarUsuario(perfil)) {
            System.out.println(perfil + " cadastrado com sucesso.");
        }
    }

    public static void consultarCatalogo() {
        System.out.println("\nCatalogo do semestre " + catalogo.getSemestre());
        if (catalogo.listarEBooks().isEmpty()) {
            System.out.println("Nenhum eBook licenciado.");
            return;
        }
        for (EBook ebook : catalogo.listarEBooks()) {
            System.out.println("- " + ebook + ", licencas em uso: " + situacaoLicenca(ebook));
        }
        PeriodoAcesso periodo = catalogo.periodoAbertoEm(LocalDate.now());
        System.out.println(periodo == null ? "Periodo de acesso fechado hoje."
                : "Periodo de acesso aberto ate " + periodo.getDataFim() + ".");
    }

    // ===== Apoio =====

    /** Lista os itens numerados e devolve o escolhido, ou null se a lista estiver vazia ou o aluno cancelar. */
    public static <T> T selecionar(String titulo, List<T> itens) {
        if (itens.isEmpty()) {
            System.out.println("Nenhuma opcao disponivel.");
            return null;
        }
        System.out.println("\n" + titulo + ":");
        for (int i = 0; i < itens.size(); i++) {
            System.out.println((i + 1) + " - " + itens.get(i));
        }
        System.out.print("Escolha (numero, ou 0 para cancelar): ");
        int opcao = lerInteiro();
        if (opcao < 1 || opcao > itens.size()) {
            return null;
        }
        return itens.get(opcao - 1);
    }

    public static int lerInteiro() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Digite um numero valido: ");
            }
        }
    }

    /** Pergunta ate o usuario digitar algo, para nenhum campo obrigatorio ficar em branco. */
    public static String perguntar(String pergunta) {
        String resposta = "";
        while (resposta.isBlank()) {
            System.out.print(pergunta + ": ");
            resposta = scanner.nextLine().trim();
        }
        return resposta;
    }

    /** Os arquivos de dados separam os campos por ponto e virgula, entao ele nao pode ir nos valores. */
    public static boolean temSeparador(String... valores) {
        if (Arrays.stream(valores).anyMatch(valor -> valor.contains(";"))) {
            System.out.println("Nao use ponto e virgula: ele separa os campos no arquivo de dados.");
            return true;
        }
        return false;
    }

    public static Usuario buscarUsuario(String id) {
        return usuarios.stream().filter(u -> u.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public static List<Aluno> alunos() {
        return usuarios.stream().filter(Aluno.class::isInstance).map(Aluno.class::cast).toList();
    }

    public static String situacaoLicenca(EBook ebook) {
        return ebook.getLicenca().getAcessosAtivos() + "/" + ebook.getLicenca().getLimiteAcessosSimultaneos();
    }
}
