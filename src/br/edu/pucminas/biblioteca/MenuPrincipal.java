package br.edu.pucminas.biblioteca;

import br.edu.pucminas.biblioteca.modelo.Aluno;
import br.edu.pucminas.biblioteca.modelo.Bibliotecario;
import br.edu.pucminas.biblioteca.modelo.Catalogo;
import br.edu.pucminas.biblioteca.modelo.Categoria;
import br.edu.pucminas.biblioteca.modelo.EBook;
import br.edu.pucminas.biblioteca.modelo.Formato;
import br.edu.pucminas.biblioteca.modelo.ItemEstante;
import br.edu.pucminas.biblioteca.modelo.Licenca;
import br.edu.pucminas.biblioteca.modelo.PeriodoAcesso;
import br.edu.pucminas.biblioteca.modelo.SistemaEstatisticas;
import br.edu.pucminas.biblioteca.modelo.TipoLeitura;
import br.edu.pucminas.biblioteca.modelo.Usuario;
import br.edu.pucminas.biblioteca.persistencia.CatalogoRepositorioArquivo;
import br.edu.pucminas.biblioteca.persistencia.EstanteRepositorioArquivo;
import br.edu.pucminas.biblioteca.persistencia.EstatisticasRepositorioArquivo;
import br.edu.pucminas.biblioteca.persistencia.UsuarioRepositorioArquivo;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/** Interface de linha de comando do sistema de gestao de eBooks. */
public class MenuPrincipal {

    private static final int OPCAO_INVALIDA = -1;
    private static final int FIM_DE_ENTRADA = -2;

    private final Scanner leitor = new Scanner(System.in);
    private final CatalogoRepositorioArquivo catalogoRepositorio = new CatalogoRepositorioArquivo();
    private final UsuarioRepositorioArquivo usuarioRepositorio = new UsuarioRepositorioArquivo();
    private final EstanteRepositorioArquivo estanteRepositorio = new EstanteRepositorioArquivo();
    private final EstatisticasRepositorioArquivo estatisticasRepositorio = new EstatisticasRepositorioArquivo();

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

        boolean continuar = true;
        while (continuar) {
            Usuario usuario = realizarLogin();
            if (usuario == null) {
                continuar = false;
            } else if (usuario instanceof Aluno) {
                menuAluno((Aluno) usuario);
            } else {
                menuBibliotecario((Bibliotecario) usuario);
            }
        }
        System.out.println("Sistema encerrado.");
        leitor.close();
    }

    // ===== Carga e gravacao dos dados =====

    private void carregarDados() {
        try {
            catalogo = catalogoRepositorio.carregar();
            usuarios = usuarioRepositorio.carregar();
            estatisticas = estatisticasRepositorio.carregar();
            if (catalogo == null || usuarios.isEmpty()) {
                System.out.println("Primeira execucao: criando os dados iniciais em dados/.");
                catalogo = DadosIniciais.criarCatalogo();
                usuarios = DadosIniciais.criarUsuarios();
                salvarDados();
            } else {
                estanteRepositorio.carregar(alunos(), catalogo);
            }
        } catch (IOException e) {
            System.out.println("Nao foi possivel ler os dados gravados: " + e.getMessage());
            System.out.println("O sistema vai continuar com os dados iniciais, sem gravar em disco.");
            catalogo = DadosIniciais.criarCatalogo();
            usuarios = DadosIniciais.criarUsuarios();
        }
    }

    private void salvarDados() {
        try {
            catalogoRepositorio.salvar(catalogo);
            usuarioRepositorio.salvar(usuarios);
            estanteRepositorio.salvar(alunos());
            estatisticasRepositorio.salvar(estatisticas);
        } catch (IOException e) {
            System.out.println("Nao foi possivel gravar os dados: " + e.getMessage());
        }
    }

    // ===== UC01 - Realizar login =====

    private Usuario realizarLogin() {
        while (true) {
            System.out.println();
            System.out.print("Login (id do usuario, ou 0 para encerrar): ");
            String id = lerLinha();
            if (id == null || "0".equals(id.trim())) {
                return null;
            }
            System.out.print("Senha: ");
            String senha = lerLinha();
            if (senha == null) {
                return null;
            }
            Usuario usuario = buscarUsuario(id.trim());
            if (usuario != null && usuario.autenticar(senha)) {
                System.out.println("Acesso liberado. Bem-vindo(a), " + usuario.getNome() + ".");
                return usuario;
            }
            System.out.println("Id ou senha invalidos. Tente novamente.");
        }
    }

    // ===== Menu do aluno (UC02, UC03 e UC04) =====

    private void menuAluno(Aluno aluno) {
        List<EBook> acessosAbertos = new ArrayList<>();
        boolean continuar = true;
        while (continuar) {
            System.out.println();
            System.out.println("--- Menu do aluno: " + aluno.getNome() + " ---");
            System.out.println("1. Consultar catalogo do semestre");
            System.out.println("2. Adicionar eBook a estante");
            System.out.println("3. Remover eBook da estante");
            System.out.println("4. Consultar minha estante");
            System.out.println("5. Acessar eBook");
            System.out.println("6. Encerrar acesso a eBook");
            System.out.println("7. Sair (voltar ao login)");

            int opcao = lerOpcao();
            if (opcao == FIM_DE_ENTRADA) {
                continuar = false;
                continue;
            }
            if (opcao == OPCAO_INVALIDA) {
                continue;
            }
            try {
                switch (opcao) {
                    case 1:
                        consultarCatalogo();
                        break;
                    case 2:
                        adicionarEBookNaEstante(aluno);
                        break;
                    case 3:
                        removerEBookDaEstante(aluno);
                        break;
                    case 4:
                        consultarEstante(aluno);
                        break;
                    case 5:
                        acessarEBook(aluno, acessosAbertos);
                        break;
                    case 6:
                        encerrarAcesso(aluno, acessosAbertos);
                        break;
                    case 7:
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opcao invalida, tente novamente.");
                }
            } catch (IllegalStateException | IllegalArgumentException e) {
                System.out.println("Nao foi possivel concluir a acao: " + e.getMessage());
            }
        }
        liberarAcessosAbertos(aluno, acessosAbertos);
    }

    /** UC02 - Adicionar eBook a estante, que inclui UC07 - Registrar estatistica de uso. */
    private void adicionarEBookNaEstante(Aluno aluno) {
        LocalDate hoje = LocalDate.now();
        consultarCatalogo();
        System.out.print("Titulo do eBook a adicionar: ");
        String titulo = lerLinha();
        if (titulo == null) {
            return;
        }
        EBook ebook = catalogo.buscarPorTitulo(titulo);
        if (ebook == null) {
            System.out.println("eBook nao encontrado no catalogo do semestre.");
            return;
        }
        if (!catalogo.podeAdicionar(ebook, hoje)) {
            System.out.println("Fora do periodo de acesso: a estante nao pode ser alterada hoje.");
            return;
        }
        System.out.println("Tipo de leitura:");
        System.out.println("1. Obrigatoria, indicada pela disciplina ("
                + aluno.getEstante().vagasRestantes(TipoLeitura.OBRIGATORIA) + " vaga(s) livre(s))");
        System.out.println("2. Livre, de escolha do aluno ("
                + aluno.getEstante().vagasRestantes(TipoLeitura.LIVRE) + " vaga(s) livre(s))");
        int escolha = lerOpcao();
        TipoLeitura tipo = tipoDeLeitura(escolha);
        if (tipo == null) {
            System.out.println("Tipo de leitura invalido.");
            return;
        }
        PeriodoAcesso periodo = catalogo.periodoAbertoEm(hoje);
        if (aluno.adicionarEBook(ebook, tipo, periodo, hoje, estatisticas)) {
            System.out.println("eBook adicionado a estante. Estatistica de uso registrada: "
                    + estatisticas.consultarTotalAdicoes(ebook) + " adicao(oes) no total.");
            salvarDados();
        } else {
            System.out.println("Nao foi possivel adicionar: o eBook ja esta na estante ou o limite"
                    + " de leitura " + tipo.toString().toLowerCase() + " foi atingido.");
        }
    }

    /** UC03 - Remover eBook da estante. */
    private void removerEBookDaEstante(Aluno aluno) {
        LocalDate hoje = LocalDate.now();
        if (aluno.getEstante().listar().isEmpty()) {
            System.out.println("Sua estante esta vazia.");
            return;
        }
        consultarEstante(aluno);
        System.out.print("Titulo do eBook a remover: ");
        String titulo = lerLinha();
        if (titulo == null) {
            return;
        }
        EBook ebook = buscarNaEstante(aluno, titulo);
        if (ebook == null) {
            System.out.println("Esse eBook nao esta na sua estante.");
            return;
        }
        PeriodoAcesso periodo = catalogo.periodoAbertoEm(hoje);
        if (aluno.removerEBook(ebook, periodo, hoje)) {
            System.out.println("eBook removido da estante. A vaga do tipo correspondente esta livre.");
            salvarDados();
        } else {
            System.out.println("Fora do periodo de acesso: a estante nao pode ser alterada hoje.");
        }
    }

    private void consultarEstante(Aluno aluno) {
        List<ItemEstante> itens = aluno.getEstante().listar();
        System.out.println();
        System.out.println("Estante de " + aluno.getNome() + ", matricula " + aluno.getMatricula() + ":");
        if (itens.isEmpty()) {
            System.out.println("  (estante vazia)");
            return;
        }
        for (ItemEstante item : itens) {
            System.out.println("  - " + item.getEBook().getTitulo()
                    + " | leitura " + item.getTipo().toString().toLowerCase()
                    + " | adicionado em " + item.getDataAdicao());
        }
        System.out.println("  Obrigatorios: " + aluno.getEstante().contarPorTipo(TipoLeitura.OBRIGATORIA)
                + "/4, livres: " + aluno.getEstante().contarPorTipo(TipoLeitura.LIVRE) + "/2");
    }

    /**
     * UC04 - Acessar eBook, respeitando o limite de acessos simultaneos da licenca.
     * Cada acesso aberto ocupa uma licenca, entao o mesmo aluno pode ocupar mais de
     * uma ao abrir o eBook em dispositivos diferentes.
     */
    private void acessarEBook(Aluno aluno, List<EBook> acessosAbertos) {
        if (aluno.getEstante().listar().isEmpty()) {
            System.out.println("Sua estante esta vazia.");
            return;
        }
        consultarEstante(aluno);
        System.out.print("Titulo do eBook a acessar: ");
        String titulo = lerLinha();
        if (titulo == null) {
            return;
        }
        EBook ebook = buscarNaEstante(aluno, titulo);
        if (ebook == null) {
            System.out.println("Esse eBook nao esta na sua estante.");
            return;
        }
        Licenca licenca = ebook.getLicenca();
        if (aluno.acessarEBook(ebook)) {
            acessosAbertos.add(ebook);
            System.out.println("Acesso concedido. Licencas em uso: " + licenca.getAcessosAtivos()
                    + "/" + licenca.getLimiteAcessosSimultaneos() + ".");
        } else {
            System.out.println("Acesso bloqueado: as " + licenca.getLimiteAcessosSimultaneos()
                    + " licenca(s) simultanea(s) desse eBook estao em uso.");
        }
    }

    private void encerrarAcesso(Aluno aluno, List<EBook> acessosAbertos) {
        if (acessosAbertos.isEmpty()) {
            System.out.println("Voce nao tem acessos abertos nesta sessao.");
            return;
        }
        System.out.println("Acessos abertos:");
        for (EBook aberto : acessosAbertos) {
            System.out.println("  - " + aberto.getTitulo());
        }
        System.out.print("Titulo do eBook a encerrar: ");
        String titulo = lerLinha();
        if (titulo == null) {
            return;
        }
        EBook ebook = buscarNaEstante(aluno, titulo);
        if (ebook == null || !acessosAbertos.contains(ebook)) {
            System.out.println("Voce nao tem acesso aberto a esse eBook.");
            return;
        }
        if (aluno.encerrarAcessoEBook(ebook)) {
            acessosAbertos.remove(ebook);
            System.out.println("Acesso encerrado. Licencas em uso: "
                    + ebook.getLicenca().getAcessosAtivos()
                    + "/" + ebook.getLicenca().getLimiteAcessosSimultaneos() + ".");
        }
    }

    /** Ao sair, as licencas ocupadas pelo aluno na sessao voltam a ficar disponiveis. */
    private void liberarAcessosAbertos(Aluno aluno, List<EBook> acessosAbertos) {
        for (EBook ebook : acessosAbertos) {
            aluno.encerrarAcessoEBook(ebook);
        }
        acessosAbertos.clear();
    }

    // ===== Menu do bibliotecario (UC05, UC06, UC07 e UC08) =====

    private void menuBibliotecario(Bibliotecario bibliotecario) {
        boolean continuar = true;
        while (continuar) {
            System.out.println();
            System.out.println("--- Menu do bibliotecario: " + bibliotecario.getNome() + " ---");
            System.out.println("1. Cadastrar eBook no catalogo");
            System.out.println("2. Consultar catalogo do semestre");
            System.out.println("3. Cadastrar periodo de acesso");
            System.out.println("4. Consultar alunos com um eBook");
            System.out.println("5. Consultar estatisticas de uso");
            System.out.println("6. Renovar catalogo do semestre");
            System.out.println("7. Sair (voltar ao login)");

            int opcao = lerOpcao();
            if (opcao == FIM_DE_ENTRADA) {
                continuar = false;
                continue;
            }
            if (opcao == OPCAO_INVALIDA) {
                continue;
            }
            try {
                switch (opcao) {
                    case 1:
                        cadastrarEBook(bibliotecario);
                        break;
                    case 2:
                        consultarCatalogo();
                        break;
                    case 3:
                        cadastrarPeriodoAcesso();
                        break;
                    case 4:
                        consultarAlunosComEBook(bibliotecario);
                        break;
                    case 5:
                        consultarEstatisticas();
                        break;
                    case 6:
                        renovarCatalogo(bibliotecario);
                        break;
                    case 7:
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opcao invalida, tente novamente.");
                }
            } catch (IllegalStateException | IllegalArgumentException e) {
                System.out.println("Nao foi possivel concluir a acao: " + e.getMessage());
            }
        }
    }

    /** UC05 - Cadastrar eBook. */
    private void cadastrarEBook(Bibliotecario bibliotecario) {
        System.out.print("Titulo: ");
        String titulo = lerLinha();
        System.out.print("Editora: ");
        String editora = lerLinha();
        if (titulo == null || editora == null) {
            return;
        }
        System.out.println("Formato: 1. PDF  2. EPUB");
        Formato formato = formatoDe(lerOpcao());
        System.out.println("Categoria: 1. Literatura  2. Tecnico  3. Periodico");
        Categoria categoria = categoriaDe(lerOpcao());
        System.out.print("Limite de acessos simultaneos (enter para "
                + Licenca.LIMITE_MAXIMO_SIMULTANEOS + "): ");
        String limite = lerLinha();
        if (limite == null) {
            return;
        }
        Licenca licenca = limite.trim().isEmpty()
                ? new Licenca()
                : new Licenca(Integer.parseInt(limite.trim()));

        bibliotecario.cadastrarEBook(new EBook(titulo.trim(), editora.trim(), formato, categoria, licenca),
                catalogo);
        System.out.println("eBook cadastrado no catalogo do semestre " + catalogo.getSemestre() + ".");
        salvarDados();
    }

    private void cadastrarPeriodoAcesso() {
        System.out.print("Data de inicio (aaaa-mm-dd): ");
        String inicio = lerLinha();
        System.out.print("Data de fim (aaaa-mm-dd): ");
        String fim = lerLinha();
        if (inicio == null || fim == null) {
            return;
        }
        try {
            catalogo.adicionarPeriodo(new PeriodoAcesso(
                    LocalDate.parse(inicio.trim()), LocalDate.parse(fim.trim())));
        } catch (DateTimeParseException e) {
            System.out.println("Data invalida. Use o formato aaaa-mm-dd, por exemplo 2026-08-01.");
            return;
        }
        System.out.println("Periodo de acesso cadastrado.");
        salvarDados();
    }

    /** UC06 - Consultar alunos com um eBook. */
    private void consultarAlunosComEBook(Bibliotecario bibliotecario) {
        consultarCatalogo();
        System.out.print("Titulo do eBook a consultar: ");
        String titulo = lerLinha();
        if (titulo == null) {
            return;
        }
        EBook ebook = catalogo.buscarPorTitulo(titulo);
        if (ebook == null) {
            System.out.println("eBook nao encontrado no catalogo do semestre.");
            return;
        }
        List<Aluno> encontrados = bibliotecario.consultarAlunosComEBook(ebook, alunos());
        if (encontrados.isEmpty()) {
            System.out.println("Nenhum aluno tem esse eBook na estante.");
            return;
        }
        System.out.println("Alunos com \"" + ebook.getTitulo() + "\" na estante:");
        for (Aluno aluno : encontrados) {
            System.out.println("  - " + aluno.getNome() + ", matricula " + aluno.getMatricula());
        }
    }

    /** UC07 - Consulta das estatisticas de uso acumuladas. */
    private void consultarEstatisticas() {
        Map<String, Integer> adicoes = estatisticas.listarAdicoes();
        System.out.println();
        System.out.println("Estatisticas de uso, adicoes a estantes por titulo:");
        if (adicoes.isEmpty()) {
            System.out.println("  (nenhuma adicao registrada ate agora)");
            return;
        }
        for (Map.Entry<String, Integer> adicao : adicoes.entrySet()) {
            System.out.println("  - " + adicao.getKey() + ": " + adicao.getValue() + " adicao(oes)");
        }
    }

    /** UC08 - Renovar catalogo do semestre. */
    private void renovarCatalogo(Bibliotecario bibliotecario) {
        LocalDate hoje = LocalDate.now();
        if (!catalogo.periodoDeAcessoEncerrado(hoje)) {
            System.out.println("Atencao: o periodo de acesso ainda nao terminou.");
            System.out.println("A renovacao vai considerar as estantes como estao hoje.");
            System.out.print("Confirmar mesmo assim? (s/n): ");
            String resposta = lerLinha();
            if (resposta == null || !"s".equalsIgnoreCase(resposta.trim())) {
                System.out.println("Renovacao cancelada.");
                return;
            }
        }
        List<Aluno> alunos = alunos();
        List<EBook> anteriores = new ArrayList<>(catalogo.listarEBooks());
        Catalogo renovado = bibliotecario.renovarCatalogo(catalogo, alunos);

        System.out.println();
        System.out.println("Minimo para renovar a licenca: " + Catalogo.getMinimoAlunosRenovacao()
                + " aluno(s) com o titulo na estante.");
        for (EBook ebook : anteriores) {
            int total = catalogo.contarAlunosCom(ebook, alunos);
            String situacao = renovado.listarEBooks().contains(ebook)
                    ? "licenca renovada"
                    : "licenca nao renovada, titulo removido do catalogo";
            System.out.println("  - " + ebook.getTitulo() + ": " + total + " aluno(s) -> " + situacao);
        }
        catalogo = renovado;
        int descartados = 0;
        for (Aluno aluno : alunos) {
            descartados += aluno.getEstante().descartarNaoLicenciados(catalogo.listarEBooks());
        }
        salvarDados();
        if (descartados > 0) {
            System.out.println("Itens retirados das estantes por perda de licenca: " + descartados + ".");
        }
        System.out.println("Catalogo do semestre " + catalogo.getSemestre() + " gerado com "
                + catalogo.listarEBooks().size() + " titulo(s).");
        System.out.println("Cadastre o periodo de acesso do novo semestre para liberar as estantes.");
    }

    // ===== Apoio =====

    private void consultarCatalogo() {
        System.out.println();
        System.out.println("Catalogo do semestre " + catalogo.getSemestre() + ":");
        if (catalogo.listarEBooks().isEmpty()) {
            System.out.println("  (nenhum eBook licenciado)");
            return;
        }
        for (EBook ebook : catalogo.listarEBooks()) {
            System.out.println("  - " + ebook + ", licencas em uso: "
                    + ebook.getLicenca().getAcessosAtivos()
                    + "/" + ebook.getLicenca().getLimiteAcessosSimultaneos());
        }
        PeriodoAcesso periodo = catalogo.periodoAbertoEm(LocalDate.now());
        System.out.println(periodo == null
                ? "  Periodo de acesso fechado hoje."
                : "  Periodo de acesso aberto ate " + periodo.getDataFim() + ".");
    }

    private List<Aluno> alunos() {
        List<Aluno> lista = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Aluno) {
                lista.add((Aluno) usuario);
            }
        }
        return lista;
    }

    private Usuario buscarUsuario(String id) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId().equalsIgnoreCase(id)) {
                return usuario;
            }
        }
        return null;
    }

    private EBook buscarNaEstante(Aluno aluno, String titulo) {
        for (ItemEstante item : aluno.getEstante().listar()) {
            if (item.getEBook().getTitulo().equalsIgnoreCase(titulo.trim())) {
                return item.getEBook();
            }
        }
        return null;
    }

    private TipoLeitura tipoDeLeitura(int opcao) {
        if (opcao == 1) {
            return TipoLeitura.OBRIGATORIA;
        }
        return opcao == 2 ? TipoLeitura.LIVRE : null;
    }

    private Formato formatoDe(int opcao) {
        if (opcao == 1) {
            return Formato.PDF;
        }
        return opcao == 2 ? Formato.EPUB : null;
    }

    private Categoria categoriaDe(int opcao) {
        switch (opcao) {
            case 1:
                return Categoria.LITERATURA;
            case 2:
                return Categoria.TECNICO;
            case 3:
                return Categoria.PERIODICO;
            default:
                return null;
        }
    }

    private int lerOpcao() {
        System.out.print("Escolha uma opcao: ");
        String entrada = lerLinha();
        if (entrada == null) {
            return FIM_DE_ENTRADA;
        }
        try {
            return Integer.parseInt(entrada.trim());
        } catch (NumberFormatException e) {
            System.out.println("Digite um numero valido.");
            return OPCAO_INVALIDA;
        }
    }

    /** Le uma linha do teclado, devolvendo null quando a entrada termina. */
    private String lerLinha() {
        if (!leitor.hasNextLine()) {
            return null;
        }
        return leitor.nextLine();
    }
}
