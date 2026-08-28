package br.edu.pucminas.biblioteca.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Estante pessoal do aluno (UC02 e UC03). */
public class Estante implements Serializable {

    private static final int LIMITE_OBRIGATORIOS = 4;
    private static final int LIMITE_LIVRES = 2;

    private List<ItemEstante> itens = new ArrayList<>();

    public Estante() {
    }

    public boolean adicionar(EBook ebook, TipoLeitura tipo) {

        // FIXME
        if (tipo == TipoLeitura.LIVRE) {
            if (itens.stream().filter(Auxebook -> Auxebook.getTipo() == TipoLeitura.LIVRE).count() < LIMITE_LIVRES) {
                itens.add(new ItemEstante(ebook, tipo, LocalDate.now()));
                System.err.println(ebook.getTitulo() + " adicionado com sucesso");
                return true;
            } else {
                System.err.println("Não foi possivel adicionar ebook limite livres ultrapassou");
                return false;
            }
        } else {
            if (itens.stream().filter(Auxebook -> Auxebook.getTipo() == TipoLeitura.OBRIGATORIA)
                    .count() < LIMITE_OBRIGATORIOS) {
                itens.add(new ItemEstante(ebook, tipo, LocalDate.now()));
                System.err.println(ebook.getTitulo() + " adicionado com sucesso");
                return true;
            } else {
                System.err.println("Não foi possivel adicionar ebook obrigadorios livres ultrapassou");
                return false;
            }
        }

        // Deve respeitar LIMITE_OBRIGATORIOS e LIMITE_LIVRES.
        // return false;
    }

    public boolean remover(EBook ebook) throws Exception {
        // if (periodoAcesso.estaAberto()) {
            Optional<ItemEstante> itemComEbook = itens.stream().filter(item -> item.getEBook().equals(ebook))
                    .findFirst();
            if (itemComEbook.isPresent()) {
                itens.remove(itemComEbook.get());
                return true;
            }
        // } else {
        //     throw new Exception("Fora do periodo de acesso");
        // }
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
        Optional<ItemEstante> itemComEbook = itens.stream().filter(item -> item.getEBook().equals(ebook)).findFirst();
        if (itemComEbook != null) {
            return true;
        }
        return false;
    }
}
