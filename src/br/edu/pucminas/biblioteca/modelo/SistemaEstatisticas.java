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
        adicoesPorTitulo.put(ebook.getTitulo(), adicoesPorTitulo.size() + 1);
    }

    public int consultarTotalAdicoes(EBook ebook) {
        if (ebook == null || ebook.getTitulo() == null) {
            return 0;
        }
        return adicoesPorTitulo.getOrDefault(ebook.getTitulo(), 0);
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Integer> entrada : adicoesPorTitulo.entrySet()) {
            stringBuilder.append("Título: " + entrada.getKey());
        }
        return stringBuilder.toString();
    }
}
