package br.edu.pucminas.biblioteca.persistencia;

import br.edu.pucminas.biblioteca.modelo.Catalogo;
import br.edu.pucminas.biblioteca.modelo.EBook;
import br.edu.pucminas.biblioteca.modelo.PeriodoAcesso;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

/**
 * Grava e le o semestre e os periodos de acesso do catalogo:
 * SEMESTRE;2026/2 na primeira linha e PERIODO;dataInicio;dataFim nas demais.
 * Os eBooks do catalogo ficam no {@link EBookRepositorioArquivo}.
 */
public class CatalogoRepositorioArquivo {

    private static final String ARQUIVO = "catalogo.txt";
    private static final String MARCA_SEMESTRE = "SEMESTRE";
    private static final String MARCA_PERIODO = "PERIODO";

    private final EBookRepositorioArquivo ebookRepositorio = new EBookRepositorioArquivo();

    public void salvar(Catalogo catalogo) throws IOException {
        File arquivo = Arquivos.preparar(ARQUIVO);
        try (PrintWriter escritor = new PrintWriter(new FileWriter(arquivo))) {
            escritor.println(MARCA_SEMESTRE + Arquivos.SEPARADOR + catalogo.getSemestre());
            for (PeriodoAcesso periodo : catalogo.listarPeriodos()) {
                escritor.println(MARCA_PERIODO + Arquivos.SEPARADOR
                        + periodo.getDataInicio() + Arquivos.SEPARADOR + periodo.getDataFim());
            }
        }
        ebookRepositorio.salvar(catalogo.listarEBooks());
    }

    /** Devolve o catalogo gravado, ou null quando ainda nao ha dados (primeira execucao). */
    public Catalogo carregar() throws IOException {
        File arquivo = Arquivos.preparar(ARQUIVO);
        if (!arquivo.exists()) {
            return null;
        }
        Catalogo catalogo = null;
        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                String[] campos = linha.split(Arquivos.SEPARADOR);
                if (MARCA_SEMESTRE.equals(campos[0]) && campos.length >= 2) {
                    catalogo = new Catalogo(campos[1]);
                } else if (MARCA_PERIODO.equals(campos[0]) && campos.length >= 3 && catalogo != null) {
                    try {
                        catalogo.adicionarPeriodo(new PeriodoAcesso(
                                LocalDate.parse(campos[1]), LocalDate.parse(campos[2])));
                    } catch (RuntimeException e) {
                        System.out.println("Linha ignorada em " + ARQUIVO + ": " + linha);
                    }
                }
            }
        }
        if (catalogo == null) {
            return null;
        }
        List<EBook> ebooks = ebookRepositorio.carregar();
        for (EBook ebook : ebooks) {
            catalogo.adicionarEBook(ebook);
        }
        return catalogo;
    }
}
