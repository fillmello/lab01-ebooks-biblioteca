package br.edu.pucminas.biblioteca.modelo;

import java.io.Serializable;

/** eBook do acervo (UC05 - Cadastrar eBook). */
public class EBook implements Serializable {

    private String titulo;
    private String editora;
    private Formato formato;
    private Categoria categoria;
    private Licenca licenca;

    public EBook(String titulo, String editora, Formato formato, Categoria categoria, Licenca licenca) {
        this.titulo = titulo;
        this.editora = editora;
        this.formato = formato;
        this.categoria = categoria;
        this.licenca = licenca;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getEditora() {
        return editora;
    }

    public Formato getFormato() {
        return formato;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Licenca getLicenca() {
        return licenca;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Nome: " + this.titulo);
        stringBuilder.append("\nEditora: " + this.editora);
        stringBuilder.append("\nCategoria: " + this.categoria);
        stringBuilder.append("\nFormato arquivo: " + this.formato);
        //FIXME
        return stringBuilder.toString();
    }
}
