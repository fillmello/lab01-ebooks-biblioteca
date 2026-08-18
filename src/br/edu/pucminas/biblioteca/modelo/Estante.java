package br.edu.pucminas.biblioteca.modelo;

import java.util.ArrayList;
import java.util.List;

/** Estante pessoal do aluno (UC02 e UC03). */
public class Estante {

    private static final int LIMITE_OBRIGATORIOS = 4;
    private static final int LIMITE_LIVRES = 2;

    private List<ItemEstante> itens = new ArrayList<>();

    public Estante() {
    }

    public boolean adicionar(EBook ebook, TipoLeitura tipo) {
        // TODO: implementar na Sprint 3
        // Deve respeitar LIMITE_OBRIGATORIOS e LIMITE_LIVRES.
        return false;
    }

    public boolean remover(EBook ebook) {
        // TODO: implementar na Sprint 3
        return false;
    }

    public List<ItemEstante> listar() {
        return itens;
    }

    public int contarPorTipo(TipoLeitura tipo) {
        int total = 0;
        for (ItemEstante item : itens) {
            if (item.getTipo() == tipo) {
                total++;
            }
        }
        return total;
    }

    public boolean contemEBook(EBook ebook) {
        // TODO: implementar na Sprint 3
        return false;
    }
}
