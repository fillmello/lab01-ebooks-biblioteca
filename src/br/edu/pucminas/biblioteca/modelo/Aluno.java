package br.edu.pucminas.biblioteca.modelo;

/** Aluno da universidade (UC02, UC03 e UC04). */
public class Aluno extends Usuario {

    private String matricula;
    private Estante estante;

    public Aluno(String id, String nome, String senha, String matricula) {
        super(id, nome, senha);
        this.matricula = matricula;
        this.estante = new Estante();
    }

    public boolean adicionarEBook(EBook ebook, TipoLeitura tipo) {
        // TODO: implementar na Sprint 3
        return false;
    }

    public boolean removerEBook(EBook ebook) {
        // TODO: implementar na Sprint 3
        return false;
    }

    public boolean acessarEBook(EBook ebook) {
        // TODO: implementar na Sprint 3
        return false;
    }

    public String getMatricula() {
        return matricula;
    }

    public Estante getEstante() {
        return estante;
    }
}
