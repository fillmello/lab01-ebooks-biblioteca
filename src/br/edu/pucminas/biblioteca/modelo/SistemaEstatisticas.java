package br.edu.pucminas.biblioteca.modelo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/** Sistema de estatisticas de uso, notificado a cada adicao (UC07). */
public class SistemaEstatisticas implements Serializable{

    private Map<String, Integer> adicoesPorTitulo = new HashMap<>();

    public SistemaEstatisticas() {
    }

    public void registrarAdicao(EBook ebook) {
        // TODO: implementar na Sprint 3
    }

    public int consultarTotalAdicoes(EBook ebook) {
        // TODO: implementar na Sprint 3
        return 0;
    }
}
