package br.edu.pucminas.biblioteca.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Disciplina que indica leituras obrigatorias (UC09).
 * E a fonte de verdade de quais eBooks sao obrigatorios para um aluno:
 * o aluno nao declara o tipo, o sistema deriva a partir daqui.
 */
public class Disciplina {

    private String codigo;
    private String nome;
    private List<EBook> leiturasIndicadas = new ArrayList<>();

    public Disciplina(String codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public void indicarLeitura(EBook ebook) {
        // TODO: implementar na Sprint 3
    }

    public boolean indicou(EBook ebook) {
        return leiturasIndicadas.contains(ebook);
    }

    public List<EBook> listarIndicados() {
        return leiturasIndicadas;
    }

    public String getCodigo() {
        return codigo;
    }
}
