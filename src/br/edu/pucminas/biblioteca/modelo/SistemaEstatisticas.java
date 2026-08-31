package br.edu.pucminas.biblioteca.modelo;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/** Sistema de estatisticas de uso, notificado a cada adicao (UC07). */
public class SistemaEstatisticas {

    private Map<String, Integer> adicoesPorTitulo = new TreeMap<>();

    public SistemaEstatisticas() {
    }

    /** Registra mais uma adicao do eBook a estante de um aluno. */
    public void registrarAdicao(EBook ebook) {
        if (ebook == null) {
            return;
        }
        restaurarAdicoes(ebook.getTitulo(), consultarTotalAdicoes(ebook) + 1);
    }

    public int consultarTotalAdicoes(EBook ebook) {
        if (ebook == null) {
            return 0;
        }
        return adicoesPorTitulo.getOrDefault(ebook.getTitulo(), 0);
    }

    /** Recoloca um total ja gravado, usado ao recarregar os dados do arquivo. */
    public void restaurarAdicoes(String titulo, int total) {
        if (titulo != null && total > 0) {
            adicoesPorTitulo.put(titulo, total);
        }
    }

    public Map<String, Integer> listarAdicoes() {
        return Collections.unmodifiableMap(adicoesPorTitulo);
    }
}
