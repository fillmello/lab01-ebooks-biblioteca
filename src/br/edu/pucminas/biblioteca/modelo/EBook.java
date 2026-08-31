package br.edu.pucminas.biblioteca.modelo;

/** eBook do acervo (UC05 - Cadastrar eBook). */
public class EBook {

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

    /** O titulo identifica o eBook, inclusive nos arquivos de dados. */
    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (!(outro instanceof EBook)) {
            return false;
        }
        return titulo.equalsIgnoreCase(((EBook) outro).titulo);
    }

    @Override
    public int hashCode() {
        return titulo.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return titulo + " (" + editora + ", " + formato + ", " + categoria + ")";
    }
}
