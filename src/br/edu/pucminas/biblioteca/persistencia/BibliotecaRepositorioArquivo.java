package br.edu.pucminas.biblioteca.persistencia;

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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Grava e le os dados do sistema em arquivos texto na pasta dados/, um registro por
 * linha e campos separados por ponto e virgula.
 */
public class BibliotecaRepositorioArquivo {

    private static final String PASTA = "dados";
    private static final String SEP = ";";

    /** O que o sistema precisa ter em memoria depois de ler os arquivos. */
    public record Dados(Catalogo catalogo, List<Usuario> usuarios, SistemaEstatisticas estatisticas) {
    }

    public void salvar(Catalogo catalogo, List<Usuario> usuarios, SistemaEstatisticas estatisticas)
            throws IOException {
        List<String> catalogoTxt = new ArrayList<>(List.of("SEMESTRE" + SEP + catalogo.getSemestre()));
        for (PeriodoAcesso periodo : catalogo.listarPeriodos()) {
            catalogoTxt.add("PERIODO" + SEP + periodo.getDataInicio() + SEP + periodo.getDataFim());
        }
        escrever("catalogo.txt", catalogoTxt);

        List<String> ebooksTxt = new ArrayList<>();
        for (EBook ebook : catalogo.listarEBooks()) {
            ebooksTxt.add(ebook.getTitulo() + SEP + ebook.getEditora() + SEP + ebook.getFormato()
                    + SEP + ebook.getCategoria() + SEP + ebook.getLicenca().getLimiteAcessosSimultaneos());
        }
        escrever("ebooks.txt", ebooksTxt);

        List<String> usuariosTxt = new ArrayList<>();
        List<String> estantesTxt = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            boolean ehAluno = usuario instanceof Aluno;
            String vinculo = ehAluno ? ((Aluno) usuario).getMatricula()
                    : ((Bibliotecario) usuario).getRegistroFuncional();
            usuariosTxt.add((ehAluno ? "ALUNO" : "BIBLIOTECARIO") + SEP + usuario.getId() + SEP
                    + usuario.getNome() + SEP + usuario.getSenha() + SEP + vinculo);
            if (usuario instanceof Aluno aluno) {
                for (ItemEstante item : aluno.getEstante().listar()) {
                    estantesTxt.add(aluno.getMatricula() + SEP + item.getEBook().getTitulo()
                            + SEP + item.getTipo() + SEP + item.getDataAdicao());
                }
            }
        }
        escrever("usuarios.txt", usuariosTxt);
        escrever("estantes.txt", estantesTxt);

        List<String> estatisticasTxt = new ArrayList<>();
        for (Map.Entry<String, Integer> adicao : estatisticas.listarAdicoes().entrySet()) {
            estatisticasTxt.add(adicao.getKey() + SEP + adicao.getValue());
        }
        escrever("estatisticas.txt", estatisticasTxt);
    }

    /** Devolve null quando ainda nao ha dados gravados, ou seja, na primeira execucao. */
    public Dados carregar() throws IOException {
        Catalogo catalogo = null;
        for (String[] campos : ler("catalogo.txt", 2)) {
            if ("SEMESTRE".equals(campos[0])) {
                catalogo = new Catalogo(campos[1]);
            } else if ("PERIODO".equals(campos[0]) && campos.length >= 3 && catalogo != null) {
                catalogo.adicionarPeriodo(
                        new PeriodoAcesso(LocalDate.parse(campos[1]), LocalDate.parse(campos[2])));
            }
        }
        List<Usuario> usuarios = new ArrayList<>();
        for (String[] campos : ler("usuarios.txt", 5)) {
            usuarios.add("ALUNO".equals(campos[0])
                    ? new Aluno(campos[1], campos[2], campos[3], campos[4])
                    : new Bibliotecario(campos[1], campos[2], campos[3], campos[4]));
        }
        if (catalogo == null || usuarios.isEmpty()) {
            return null;
        }

        for (String[] campos : ler("ebooks.txt", 5)) {
            catalogo.adicionarEBook(new EBook(campos[0], campos[1], Formato.valueOf(campos[2]),
                    Categoria.valueOf(campos[3]), new Licenca(Integer.parseInt(campos[4]))));
        }
        for (String[] campos : ler("estantes.txt", 4)) {
            Aluno aluno = buscarAluno(usuarios, campos[0]);
            EBook ebook = catalogo.buscarPorTitulo(campos[1]);
            if (aluno != null && ebook != null) {
                aluno.getEstante().restaurar(new ItemEstante(ebook,
                        TipoLeitura.valueOf(campos[2]), LocalDate.parse(campos[3])));
            }
        }
        SistemaEstatisticas estatisticas = new SistemaEstatisticas();
        for (String[] campos : ler("estatisticas.txt", 2)) {
            estatisticas.restaurarAdicoes(campos[0], Integer.parseInt(campos[1]));
        }
        return new Dados(catalogo, usuarios, estatisticas);
    }

    private void escrever(String nome, List<String> linhas) throws IOException {
        try (PrintWriter escritor = new PrintWriter(new FileWriter(arquivo(nome)))) {
            linhas.forEach(escritor::println);
        }
    }

    /** Le o arquivo e devolve as linhas ja separadas, descartando as incompletas. */
    private List<String[]> ler(String nome, int minimoDeCampos) throws IOException {
        List<String[]> registros = new ArrayList<>();
        File arquivo = arquivo(nome);
        if (!arquivo.exists()) {
            return registros;
        }
        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                String[] campos = linha.split(SEP);
                if (campos.length >= minimoDeCampos) {
                    registros.add(campos);
                } else {
                    System.out.println("Linha ignorada em " + nome + ": " + linha);
                }
            }
        }
        return registros;
    }

    /** Devolve o arquivo dentro da pasta de dados, criando a pasta se ainda nao existir. */
    private File arquivo(String nome) throws IOException {
        File pasta = new File(PASTA);
        if (!pasta.exists() && !pasta.mkdirs()) {
            throw new IOException("Nao foi possivel criar a pasta " + PASTA);
        }
        return new File(pasta, nome);
    }

    private Aluno buscarAluno(List<Usuario> usuarios, String matricula) {
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Aluno aluno && aluno.getMatricula().equals(matricula)) {
                return aluno;
            }
        }
        return null;
    }
}
