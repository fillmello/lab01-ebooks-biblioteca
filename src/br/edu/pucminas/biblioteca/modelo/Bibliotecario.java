package br.edu.pucminas.biblioteca.modelo;

import java.util.List;

/** Bibliotecario da universidade (UC05, UC06 e UC08). */
public class Bibliotecario extends Usuario {

    private String registroFuncional;

    public Bibliotecario(String id, String nome, String senha, String registroFuncional) {
        super(id, nome, senha);
        this.registroFuncional = registroFuncional;
    }

    public void cadastrarEBook(EBook ebook, Catalogo catalogo) {
        // TODO: implementar na Sprint 3
    }

    public List<Aluno> consultarAlunosComEBook(EBook ebook, List<Aluno> alunos) {
        // TODO: implementar na Sprint 3
        return null;
    }

    public Catalogo renovarCatalogo(Catalogo catalogo, List<Aluno> alunos) {
        // TODO: implementar na Sprint 3
        return null;
    }

    public String getRegistroFuncional() {
        return registroFuncional;
    }
}
