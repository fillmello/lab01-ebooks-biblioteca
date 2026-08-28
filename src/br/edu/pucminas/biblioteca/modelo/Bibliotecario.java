package br.edu.pucminas.biblioteca.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** Bibliotecario da universidade (UC05, UC06 e UC08). */
public class Bibliotecario extends Usuario {

    private String registroFuncional;

    public Bibliotecario(String id, String nome, String senha, String registroFuncional) {
        super(id, nome, senha);
        this.registroFuncional = registroFuncional;
    }

    public void cadastrarEBook(EBook ebook, Catalogo catalogo) throws Exception {
        catalogo.adicionarEBook(ebook);
    }

    public List<Aluno> consultarAlunosComEBook(EBook ebook, List<Aluno> alunos) {
        List<Aluno> alunosComEbook = new ArrayList<>();
        for (Aluno aluno : alunos){
            if (aluno.getEstante().contemEBook(ebook)){
                alunosComEbook.add(aluno);
            }
        }

        return alunosComEbook;
    }

    public Catalogo renovarCatalogo(Catalogo catalogo, List<Aluno> alunos) {
        return catalogo.renovar(alunos);
    }

    public String getRegistroFuncional() {
        return registroFuncional;
    }

    public boolean validiar(String senha, String registro){
        if (registroFuncional.equals(registro)  && super.validaSenha(senha)){
            return true;
        }
        return false;
    }
}
