package br.edu.pucminas.biblioteca.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Estante pessoal do aluno (UC02 e UC03). */
public class Estante implements Serializable {

    private static final int LIMITE_OBRIGATORIOS = 4;
    private static final int LIMITE_LIVRES = 2;

    private List<ItemEstante> itens = new ArrayList<>();

    public Estante() {
    }

    /**
     * Adiciona um eBook a estante, respeitando o periodo de acesso e o limite
     * do tipo de leitura. Retorna false quando a adicao nao e permitida.
     */
    public boolean adicionar(EBook ebook, TipoLeitura tipo, PeriodoAcesso periodo, LocalDate data) {
        if (ebook == null || tipo == null || periodo == null || !periodo.estaAberto(data)) {
            return false;
        }
        if (contemEBook(ebook)) {
            return false;
        }
        if (contarPorTipo(tipo) >= limiteDe(tipo)) {
            return false;
        }
        itens.add(new ItemEstante(ebook, tipo, data));
        return true;
    }

    /** Remove um eBook da estante, permitido apenas com o periodo de acesso aberto. */
    public boolean remover(EBook ebook, PeriodoAcesso periodo, LocalDate data) {
        if (ebook == null || periodo == null || !periodo.estaAberto(data)) {
            return false;
        }
        for (int i = 0; i < itens.size(); i++) {
            if (itens.get(i).getEBook().equals(ebook)) {
                itens.remove(i);
                return true;
            }
        }
        return false;
    }

    /** Recoloca na estante um item ja gravado, usado ao recarregar os dados do arquivo. */
    public void restaurar(ItemEstante item) {
        if (item != null && !contemEBook(item.getEBook())) {
            itens.add(item);
        }
    }

    /**
     * Retira da estante os eBooks que sairam do catalogo na renovacao do semestre
     * e devolve quantos foram retirados (UC08).
     */
    public int descartarNaoLicenciados(List<EBook> licenciados) {
        int descartados = 0;
        for (int i = itens.size() - 1; i >= 0; i--) {
            if (!licenciados.contains(itens.get(i).getEBook())) {
                itens.remove(i);
                descartados++;
            }
        }
        return descartados;
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
        for (ItemEstante item : itens) {
            if (item.getEBook().equals(ebook)) {
                return true;
            }
        }
        return false;
    }

    public int vagasRestantes(TipoLeitura tipo) {
        return limiteDe(tipo) - contarPorTipo(tipo);
    }

    private int limiteDe(TipoLeitura tipo) {
        return tipo == TipoLeitura.OBRIGATORIA ? LIMITE_OBRIGATORIOS : LIMITE_LIVRES;
    }
}
