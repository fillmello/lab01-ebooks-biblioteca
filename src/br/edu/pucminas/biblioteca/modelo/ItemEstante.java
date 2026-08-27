package br.edu.pucminas.biblioteca.modelo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Vinculo entre uma estante e um eBook (UC02 - Adicionar eBook a estante).
 * O tipo de leitura fica aqui porque o mesmo eBook pode ser obrigatorio
 * para um aluno e livre para outro.
 */
public class ItemEstante implements Serializable {

    private EBook ebook;
    private TipoLeitura tipo;
    private LocalDate dataAdicao;

    public ItemEstante(EBook ebook, TipoLeitura tipo, LocalDate dataAdicao) {
        this.ebook = ebook;
        this.tipo = tipo;
        this.dataAdicao = dataAdicao;
    }

    public EBook getEBook() {
        return ebook;
    }

    public TipoLeitura getTipo() {
        return tipo;
    }

    public LocalDate getDataAdicao() {
        return dataAdicao;
    }

    @Override
    public String toString(){
        return "Item da estante\n" + "Tipo ebook: " + this.tipo + "\n" + ebook;
    }
}
