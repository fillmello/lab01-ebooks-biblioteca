package br.edu.pucminas.biblioteca;

import br.edu.pucminas.biblioteca.modelo.*;
import br.edu.pucminas.biblioteca.persistencia.BibliotecaRepositorioArquivo;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class MenuPrincipal {


    private record Opcao(String rotulo, Runnable acao) {
    }

    private final Scanner leitor = new Scanner(System.in);
    private final BibliotecaRepositorioArquivo repositorio = new BibliotecaRepositorioArquivo();

    private Catalogo catalogo;
    private List<Usuario> usuarios = new ArrayList<>();
    private SistemaEstatisticas estatisticas = new SistemaEstatisticas();

    public static void main(String[] args) {
        new MenuPrincipal().executar();
    }

    private void executar() {
        System.out.println("=== Sistema de Gestao de eBooks da Biblioteca Universitaria ===");
        carregarDados();
        System.out.println("Semestre " + catalogo.getSemestre() + ", data de hoje: " + LocalDate.now());
        try {
            for (Usuario usuario = realizarLogin(); usuario != null; usuario = realizarLogin()) {
                if (usuario instanceof Aluno aluno) {
                    menuAluno(aluno);
                } else {
                    menuBibliotecario((Bibliotecario) usuario);
                }
            }
        } catch (NoSuchElementException e) {
            // A entrada acabou, o que acontece quando o programa e alimentado por um arquivo.
        }
        System.out.println("Sistema encerrado.");
        leitor.close();
    }

    // ===== Carga e gravacao dos dados =====

    private void carregarDados() {
        try {
            aplicar(repositorio.carregar());
        } catch (RuntimeException | IOException e) {
            System.out.println("Nao foi possivel ler os dados gravados: " + e.getMessage());
            System.out.println("O sistema continua com os dados iniciais, sem gravar em disco.");
            aplicar(repositorio.dadosIniciais());
        }
    }

    private void aplicar(BibliotecaRepositorioArquivo.Dados dados) {
        catalogo = dados.catalogo();
        usuarios = dados.usuarios();
        estatisticas = dados.estatisticas();
    }

    private void salvarDados() {
        try {
            repositorio.salvar(catalogo, usuarios, estatisticas);
        } catch (IOException e) {
            System.out.println("Nao foi possivel gravar os dados: " + e.getMessage());
        }
    }

    // ===== UC01 - Realizar login =====

    private Usuario realizarLogin() {
        while (true) {
            String id = perguntar("\nLogin (id, 'novo' para criar um login, ou 0 para encerrar)").trim();
            if ("0".equals(id)) {
                return null;
            }
            if ("novo".equalsIgnoreCase(id)) {
                Aluno criado = criarLogin();
                if (criado != null) {
                    return criado;
                }
                continue;
            }
            String senha = perguntar("Senha");
            Usuario usuario = buscarUsuario(id);
            if (usuario != null && usuario.autenticar(senha)) {
                System.out.println("Acesso liberado. Bem-vindo(a), " + usuario.getNome() + ".");
                return usuario;
            }
            System.out.println("Id ou senha invalidos. Tente novamente.");
        }
    }

    /**
     * Cria um login de aluno para quem ainda nao tem cadastro e ja o deixa logado.
     * So cria aluno: pelo enunciado, quem mantem os dados dos bibliotecarios e a
     * propria equipe da biblioteca.
     */
    private Aluno criarLogin() {
        String id = perguntar("Escolha um id de login").trim();
        String nome = perguntar("Nome completo").trim();
        String matricula = perguntar("Matricula").trim();
        String senha = perguntar("Senha");

        if (id.isEmpty() || nome.isEmpty() || matricula.isEmpty() || senha.isEmpty()) {
            return recusar("Id, nome, matricula e senha sao obrigatorios.");
        }
        if (temSeparador(id, nome, matricula, senha)) {
            return null;
        }
        if (buscarUsuario(id) != null) {
            return recusar("Ja existe um usuario com o id \"" + id + "\". Escolha outro.");
        }
        if (alunos().stream().anyMatch(a -> a.getMatricula().equals(matricula))) {
            return recusar("Ja existe um aluno com a matricula " + matricula + ".");
        }
        Aluno aluno = new Aluno(id, nome, senha, matricula);
        usuarios.add(aluno);
        salvarDados();
        System.out.println("Login criado. Bem-vindo(a), " + nome + ".");
        return aluno;
    }

    // ===== Menus =====

    private void menuAluno(Aluno aluno) {
        List<EBook> acessosAbertos = new ArrayList<>();
        exibirMenu("Menu do aluno: " + aluno.getNome(), List.of(
                new Opcao("Consultar catalogo do semestre", this::consultarCatalogo),
                new Opcao("Adicionar eBook a estante", () -> adicionarEBook(aluno)),
                new Opcao("Remover eBook da estante", () -> removerEBook(aluno)),
                new Opcao("Consultar minha estante", () -> consultarEstante(aluno)),
                new Opcao("Acessar eBook", () -> acessarEBook(aluno, acessosAbertos)),
                new Opcao("Encerrar acesso a eBook", () -> encerrarAcesso(aluno, acessosAbertos))));

        // Ao sair, as licencas ocupadas pelo aluno na sessao voltam a ficar disponiveis.
        acessosAbertos.forEach(aluno::encerrarAcessoEBook);
    }

    private void menuBibliotecario(Bibliotecario bibliotecario) {
        exibirMenu("Menu do bibliotecario: " + bibliotecario.getNome(), List.of(
                new Opcao("Cadastrar eBook no catalogo", () -> cadastrarEBook(bibliotecario)),
                new Opcao("Consultar catalogo do semestre", this::consultarCatalogo),
                new Opcao("Cadastrar periodo de acesso", this::cadastrarPeriodoAcesso),
                new Opcao("Consultar alunos com um eBook", () -> consultarAlunosComEBook(bibliotecario)),
                new Opcao("Consultar estatisticas de uso", this::consultarEstatisticas),
                new Opcao("Renovar catalogo do semestre", () -> renovarCatalogo(bibliotecario))));
    }

    /** Mostra as opcoes em laco ate o usuario escolher sair. */
    private void exibirMenu(String titulo, List<Opcao> opcoes) {
        int sair = opcoes.size() + 1;
        while (true) {
            System.out.println("\n--- " + titulo + " ---");
            for (int i = 0; i < opcoes.size(); i++) {
                System.out.println((i + 1) + ". " + opcoes.get(i).rotulo());
            }
            System.out.println(sair + ". Sair (voltar ao login)");

            int escolha = lerOpcao();
            if (escolha == sair) {
                return;
            }
            if (escolha < 1 || escolha > opcoes.size()) {
                System.out.println("Opcao invalida: digite o numero de uma das opcoes acima.");
                continue;
            }
            try {
                opcoes.get(escolha - 1).acao().run();
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Nao foi possivel concluir a acao: " + e.getMessage());
            }
        }
    }

    // ===== Acoes do aluno (UC02, UC03 e UC04) =====

    /** UC02 - Adicionar eBook a estante, que inclui UC07 - Registrar estatistica de uso. */
    private void adicionarEBook(Aluno aluno) {
        LocalDate hoje = LocalDate.now();
        EBook ebook = pedirEBookDoCatalogo("Titulo do eBook a adicionar");
        if (ebook == null) {
            return;
        }
        if (!catalogo.podeAdicionar(ebook, hoje)) {
            System.out.println("Fora do periodo de acesso: a estante nao pode ser alterada hoje.");
            return;
        }
        Estante estante = aluno.getEstante();
        System.out.println("Vagas livres: " + estante.vagasRestantes(TipoLeitura.OBRIGATORIA)
                + " obrigatoria(s), " + estante.vagasRestantes(TipoLeitura.LIVRE) + " livre(s).");
        TipoLeitura tipo = escolher("Tipo de leitura", TipoLeitura.values());
        if (tipo == null) {
            System.out.println("Tipo de leitura invalido.");
        } else if (aluno.adicionarEBook(ebook, tipo, catalogo.periodoAbertoEm(hoje), hoje, estatisticas)) {
            System.out.println("eBook adicionado. Estatistica de uso registrada: "
                    + estatisticas.consultarTotalAdicoes(ebook) + " adicao(oes) no total.");
            salvarDados();
        } else {
            System.out.println("Nao foi possivel adicionar: o eBook ja esta na estante ou o limite de "
                    + "leitura " + minuscula(tipo) + " foi atingido.");
        }
    }

    /** UC03 - Remover eBook da estante. */
    private void removerEBook(Aluno aluno) {
        LocalDate hoje = LocalDate.now();
        EBook ebook = pedirEBookDaEstante(aluno, "Titulo do eBook a remover");
        if (ebook == null) {
            return;
        }
        if (aluno.removerEBook(ebook, catalogo.periodoAbertoEm(hoje), hoje)) {
            System.out.println("eBook removido. A vaga do tipo correspondente esta livre.");
            salvarDados();
        } else {
            System.out.println("Fora do periodo de acesso: a estante nao pode ser alterada hoje.");
        }
    }

    private void consultarEstante(Aluno aluno) {
        Estante estante = aluno.getEstante();
        System.out.println("\nEstante de " + aluno.getNome() + ", matricula " + aluno.getMatricula() + ":");
        if (estante.listar().isEmpty()) {
            System.out.println("  (estante vazia)");
            return;
        }
        for (ItemEstante item : estante.listar()) {
            System.out.println("  - " + item.getEBook().getTitulo() + " | leitura " + minuscula(item.getTipo())
                    + " | adicionado em " + item.getDataAdicao());
        }
        System.out.println("  Obrigatorios: " + estante.contarPorTipo(TipoLeitura.OBRIGATORIA)
                + "/4, livres: " + estante.contarPorTipo(TipoLeitura.LIVRE) + "/2");
    }

    /**
     * UC04 - Acessar eBook, respeitando o limite de acessos simultaneos da licenca.
     * Cada acesso aberto ocupa uma licenca, entao o mesmo aluno pode ocupar mais de
     * uma ao abrir o eBook em dispositivos diferentes.
     */
    private void acessarEBook(Aluno aluno, List<EBook> acessosAbertos) {
        EBook ebook = pedirEBookDaEstante(aluno, "Titulo do eBook a acessar");
        if (ebook == null) {
            return;
        }
        if (aluno.acessarEBook(ebook)) {
            acessosAbertos.add(ebook);
            System.out.println("Acesso concedido. Licencas em uso: " + situacaoLicenca(ebook) + ".");
        } else {
            System.out.println("Acesso bloqueado: as " + ebook.getLicenca().getLimiteAcessosSimultaneos()
                    + " licenca(s) simultanea(s) desse eBook estao em uso.");
        }
    }

    private void encerrarAcesso(Aluno aluno, List<EBook> acessosAbertos) {
        if (acessosAbertos.isEmpty()) {
            System.out.println("Voce nao tem acessos abertos nesta sessao.");
            return;
        }
        System.out.println("Acessos abertos:");
        acessosAbertos.forEach(aberto -> System.out.println("  - " + aberto.getTitulo()));

        EBook ebook = buscarNaEstante(aluno, perguntar("Titulo do eBook a encerrar"));
        if (ebook == null || !acessosAbertos.remove(ebook)) {
            System.out.println("Voce nao tem acesso aberto a esse eBook.");
            return;
        }
        aluno.encerrarAcessoEBook(ebook);
        System.out.println("Acesso encerrado. Licencas em uso: " + situacaoLicenca(ebook) + ".");
    }

    // ===== Acoes do bibliotecario (UC05, UC06, UC07 e UC08) =====

    /** UC05 - Cadastrar eBook. */
    private void cadastrarEBook(Bibliotecario bibliotecario) {
        String titulo = perguntar("Titulo").trim();
        String editora = perguntar("Editora").trim();
        if (temSeparador(titulo, editora)) {
            return;
        }
        Formato formato = escolher("Formato", Formato.values());
        Categoria categoria = escolher("Categoria", Categoria.values());
        String limite = perguntar("Limite de acessos simultaneos (enter para "
                + Licenca.LIMITE_MAXIMO_SIMULTANEOS + ")").trim();
        Licenca licenca = limite.isEmpty() ? new Licenca() : new Licenca(Integer.parseInt(limite));

        bibliotecario.cadastrarEBook(new EBook(titulo, editora, formato, categoria, licenca), catalogo);
        System.out.println("eBook cadastrado no catalogo do semestre " + catalogo.getSemestre() + ".");
        salvarDados();
    }

    private void cadastrarPeriodoAcesso() {
        try {
            catalogo.adicionarPeriodo(new PeriodoAcesso(
                    LocalDate.parse(perguntar("Data de inicio (aaaa-mm-dd)").trim()),
                    LocalDate.parse(perguntar("Data de fim (aaaa-mm-dd)").trim())));
        } catch (DateTimeParseException e) {
            System.out.println("Data invalida. Use o formato aaaa-mm-dd, por exemplo 2026-08-01.");
            return;
        }
        System.out.println("Periodo de acesso cadastrado.");
        salvarDados();
    }

    /** UC06 - Consultar alunos com um eBook. */
    private void consultarAlunosComEBook(Bibliotecario bibliotecario) {
        EBook ebook = pedirEBookDoCatalogo("Titulo do eBook a consultar");
        if (ebook == null) {
            return;
        }
        List<Aluno> encontrados = bibliotecario.consultarAlunosComEBook(ebook, alunos());
        if (encontrados.isEmpty()) {
            System.out.println("Nenhum aluno tem esse eBook na estante.");
            return;
        }
        System.out.println("Alunos com \"" + ebook.getTitulo() + "\" na estante:");
        encontrados.forEach(a -> System.out.println("  - " + a.getNome() + ", matricula " + a.getMatricula()));
    }

    /** UC07 - Consulta das estatisticas de uso acumuladas. */
    private void consultarEstatisticas() {
        Map<String, Integer> adicoes = estatisticas.listarAdicoes();
        System.out.println("\nEstatisticas de uso, adicoes a estantes por titulo:");
        if (adicoes.isEmpty()) {
            System.out.println("  (nenhuma adicao registrada ate agora)");
            return;
        }
        adicoes.forEach((titulo, total) -> System.out.println("  - " + titulo + ": " + total + " adicao(oes)"));
    }

    /** UC08 - Renovar catalogo do semestre. */
    private void renovarCatalogo(Bibliotecario bibliotecario) {
        if (!catalogo.periodoDeAcessoEncerrado(LocalDate.now())) {
            System.out.println("Atencao: o periodo de acesso ainda nao terminou.");
            System.out.println("A renovacao vai considerar as estantes como estao hoje.");
            if (!"s".equalsIgnoreCase(perguntar("Confirmar mesmo assim? (s/n)").trim())) {
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
            System.out.println("  - " + ebook.getTitulo() + ": " + catalogo.contarAlunosCom(ebook, alunos)
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
        System.out.println("Cadastre o periodo de acesso do novo semestre para liberar as estantes.");
    }

    private void consultarCatalogo() {
        System.out.println("\nCatalogo do semestre " + catalogo.getSemestre() + ":");
        if (catalogo.listarEBooks().isEmpty()) {
            System.out.println("  (nenhum eBook licenciado)");
            return;
        }
        for (EBook ebook : catalogo.listarEBooks()) {
            System.out.println("  - " + ebook + ", licencas em uso: " + situacaoLicenca(ebook));
        }
        PeriodoAcesso periodo = catalogo.periodoAbertoEm(LocalDate.now());
        System.out.println(periodo == null ? "  Periodo de acesso fechado hoje."
                : "  Periodo de acesso aberto ate " + periodo.getDataFim() + ".");
    }

    // ===== Apoio: perguntas ao usuario e buscas =====

    /** Mostra o catalogo, pergunta um titulo e devolve o eBook, ou null se nao existir. */
    private EBook pedirEBookDoCatalogo(String pergunta) {
        consultarCatalogo();
        EBook ebook = catalogo.buscarPorTitulo(perguntar(pergunta));
        if (ebook == null) {
            System.out.println("eBook nao encontrado no catalogo do semestre.");
        }
        return ebook;
    }

    /** Mostra a estante, pergunta um titulo e devolve o eBook, ou null se nao estiver la. */
    private EBook pedirEBookDaEstante(Aluno aluno, String pergunta) {
        if (aluno.getEstante().listar().isEmpty()) {
            System.out.println("Sua estante esta vazia.");
            return null;
        }
        consultarEstante(aluno);
        EBook ebook = buscarNaEstante(aluno, perguntar(pergunta));
        if (ebook == null) {
            System.out.println("Esse eBook nao esta na sua estante.");
        }
        return ebook;
    }

    private EBook buscarNaEstante(Aluno aluno, String titulo) {
        return aluno.getEstante().listar().stream().map(ItemEstante::getEBook)
                .filter(ebook -> ebook.getTitulo().equalsIgnoreCase(titulo.trim()))
                .findFirst().orElse(null);
    }

    private Usuario buscarUsuario(String id) {
        return usuarios.stream().filter(u -> u.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    private List<Aluno> alunos() {
        return usuarios.stream().filter(Aluno.class::isInstance).map(Aluno.class::cast).toList();
    }

    /** Os arquivos de dados separam os campos por ponto e virgula, entao ele nao pode ir nos valores. */
    private boolean temSeparador(String... valores) {
        if (Arrays.stream(valores).anyMatch(valor -> valor.contains(";"))) {
            System.out.println("Nao use ponto e virgula: ele separa os campos no arquivo de dados.");
            return true;
        }
        return false;
    }

    /** Mostra o motivo da recusa e devolve null, para encurtar as validacoes do cadastro. */
    private Aluno recusar(String motivo) {
        System.out.println(motivo);
        return null;
    }

    private String situacaoLicenca(EBook ebook) {
        return ebook.getLicenca().getAcessosAtivos() + "/" + ebook.getLicenca().getLimiteAcessosSimultaneos();
    }

    private String minuscula(Enum<?> valor) {
        return valor.toString().toLowerCase();
    }

    /** Lista os valores de um enum numerados e devolve o escolhido, ou null se invalido. */
    private <T extends Enum<T>> T escolher(String rotulo, T[] valores) {
        System.out.println(rotulo + ":");
        for (int i = 0; i < valores.length; i++) {
            System.out.println((i + 1) + ". " + valores[i]);
        }
        int escolha = lerOpcao();
        return escolha >= 1 && escolha <= valores.length ? valores[escolha - 1] : null;
    }

    /** Le a opcao escolhida, devolvendo zero quando o usuario digita algo que nao e numero. */
    private int lerOpcao() {
        try {
            return Integer.parseInt(perguntar("Escolha uma opcao").trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String perguntar(String pergunta) {
        System.out.print(pergunta + ": ");
        return leitor.nextLine();
    }
}
