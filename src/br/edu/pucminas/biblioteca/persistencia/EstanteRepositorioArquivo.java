package br.edu.pucminas.biblioteca.persistencia;

import br.edu.pucminas.biblioteca.modelo.Aluno;
import br.edu.pucminas.biblioteca.modelo.Catalogo;
import br.edu.pucminas.biblioteca.modelo.EBook;
import br.edu.pucminas.biblioteca.modelo.ItemEstante;
import br.edu.pucminas.biblioteca.modelo.TipoLeitura;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

/**
 * Grava e le o conteudo das estantes, um item por linha:
 * matriculaDoAluno;tituloDoEBook;tipoDeLeitura;dataDeAdicao.
 */
public class EstanteRepositorioArquivo {

    private static final String ARQUIVO = "estantes.txt";

    public void salvar(List<Aluno> alunos) throws IOException {
        File arquivo = Arquivos.preparar(ARQUIVO);
        try (PrintWriter escritor = new PrintWriter(new FileWriter(arquivo))) {
            for (Aluno aluno : alunos) {
                for (ItemEstante item : aluno.getEstante().listar()) {
                    escritor.println(aluno.getMatricula() + Arquivos.SEPARADOR
                            + item.getEBook().getTitulo() + Arquivos.SEPARADOR
                            + item.getTipo() + Arquivos.SEPARADOR
                            + item.getDataAdicao());
                }
            }
        }
    }

    /** Recoloca em cada estante os itens gravados, buscando o eBook pelo titulo no catalogo. */
    public void carregar(List<Aluno> alunos, Catalogo catalogo) throws IOException {
        File arquivo = Arquivos.preparar(ARQUIVO);
        if (!arquivo.exists()) {
            return;
        }
        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                String[] campos = linha.split(Arquivos.SEPARADOR);
                if (campos.length < 4) {
                    continue;
                }
                Aluno aluno = buscarPorMatricula(alunos, campos[0]);
                EBook ebook = catalogo.buscarPorTitulo(campos[1]);
                if (aluno == null || ebook == null) {
                    System.out.println("Linha ignorada em " + ARQUIVO + ": " + linha);
                    continue;
                }
                try {
                    aluno.getEstante().restaurar(new ItemEstante(ebook,
                            TipoLeitura.valueOf(campos[2]), LocalDate.parse(campos[3])));
                } catch (RuntimeException e) {
                    System.out.println("Linha ignorada em " + ARQUIVO + ": " + linha);
                }
            }
        }
    }

    private Aluno buscarPorMatricula(List<Aluno> alunos, String matricula) {
        for (Aluno aluno : alunos) {
            if (aluno.getMatricula().equals(matricula)) {
                return aluno;
            }
        }
        return null;
    }
}
