package br.edu.pucminas.biblioteca.persistencia;

import br.edu.pucminas.biblioteca.modelo.SistemaEstatisticas;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/** Grava e le as estatisticas de uso, um titulo por linha: titulo;totalDeAdicoes. */
public class EstatisticasRepositorioArquivo {

    private static final String ARQUIVO = "estatisticas.txt";

    public void salvar(SistemaEstatisticas estatisticas) throws IOException {
        File arquivo = Arquivos.preparar(ARQUIVO);
        try (PrintWriter escritor = new PrintWriter(new FileWriter(arquivo))) {
            for (Map.Entry<String, Integer> adicao : estatisticas.listarAdicoes().entrySet()) {
                escritor.println(adicao.getKey() + Arquivos.SEPARADOR + adicao.getValue());
            }
        }
    }

    public SistemaEstatisticas carregar() throws IOException {
        SistemaEstatisticas estatisticas = new SistemaEstatisticas();
        File arquivo = Arquivos.preparar(ARQUIVO);
        if (!arquivo.exists()) {
            return estatisticas;
        }
        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                String[] campos = linha.split(Arquivos.SEPARADOR);
                if (campos.length < 2) {
                    continue;
                }
                try {
                    estatisticas.restaurarAdicoes(campos[0], Integer.parseInt(campos[1]));
                } catch (NumberFormatException e) {
                    System.out.println("Linha ignorada em " + ARQUIVO + ": " + linha);
                }
            }
        }
        return estatisticas;
    }
}
