package br.edu.pucminas.biblioteca.persistencia;

import br.edu.pucminas.biblioteca.modelo.Categoria;
import br.edu.pucminas.biblioteca.modelo.EBook;
import br.edu.pucminas.biblioteca.modelo.Formato;
import br.edu.pucminas.biblioteca.modelo.Licenca;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/** Grava e le os eBooks do catalogo, um por linha: titulo;editora;formato;categoria;limiteLicenca. */
public class EBookRepositorioArquivo {

    private static final String ARQUIVO = "ebooks.txt";

    public void salvar(List<EBook> ebooks) throws IOException {
        File arquivo = Arquivos.preparar(ARQUIVO);
        try (PrintWriter escritor = new PrintWriter(new FileWriter(arquivo))) {
            for (EBook ebook : ebooks) {
                escritor.println(ebook.getTitulo() + Arquivos.SEPARADOR
                        + ebook.getEditora() + Arquivos.SEPARADOR
                        + ebook.getFormato() + Arquivos.SEPARADOR
                        + ebook.getCategoria() + Arquivos.SEPARADOR
                        + ebook.getLicenca().getLimiteAcessosSimultaneos());
            }
        }
    }

    public List<EBook> carregar() throws IOException {
        List<EBook> ebooks = new ArrayList<>();
        File arquivo = Arquivos.preparar(ARQUIVO);
        if (!arquivo.exists()) {
            return ebooks;
        }
        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                String[] campos = linha.split(Arquivos.SEPARADOR);
                if (campos.length < 5) {
                    continue;
                }
                try {
                    ebooks.add(new EBook(campos[0], campos[1],
                            Formato.valueOf(campos[2]),
                            Categoria.valueOf(campos[3]),
                            new Licenca(Integer.parseInt(campos[4]))));
                } catch (IllegalArgumentException e) {
                    System.out.println("Linha ignorada em " + ARQUIVO + ": " + linha);
                }
            }
        }
        return ebooks;
    }
}
