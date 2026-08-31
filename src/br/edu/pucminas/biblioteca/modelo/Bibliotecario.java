package br.edu.pucminas.biblioteca.modelo;

import java.util.ArrayList;
import java.util.List;

/** Bibliotecario da universidade (UC05, UC06 e UC08). */
public class Bibliotecario extends Usuario {

    private String registroFuncional;

    public Bibliotecario(String id, String nome, String senha, String registroFuncional) {
        super(id, nome, senha);
        this.registroFuncional = registroFuncional;
    }

    /** Publica o eBook no catalogo do semestre, exigindo os dados obrigatorios (UC05). */
    public void cadastrarEBook(EBook ebook, Catalogo catalogo) {
        if (ebook == null || catalogo == null) {
            throw new IllegalArgumentException("eBook e catalogo sao obrigatorios");
        }
        if (vazio(ebook.getTitulo()) || vazio(ebook.getEditora())
                || ebook.getFormato() == null || ebook.getCategoria() == null) {
            throw new IllegalArgumentException("Titulo, editora, formato e categoria sao obrigatorios");
        }
        if (catalogo.buscarPorTitulo(ebook.getTitulo()) != null) {
            throw new IllegalArgumentException("Ja existe um eBook com esse titulo no catalogo");
        }
        catalogo.adicionarEBook(ebook);
    }

    /** Lista os alunos que tem o eBook consultado na estante (UC06). */
    public List<Aluno> consultarAlunosComEBook(EBook ebook, List<Aluno> alunos) {
        List<Aluno> encontrados = new ArrayList<>();
        if (ebook == null || alunos == null) {
            return encontrados;
        }
        for (Aluno aluno : alunos) {
            if (aluno.getEstante().contemEBook(ebook)) {
                encontrados.add(aluno);
            }
        }
        return encontrados;
    }

    /** Gera o catalogo do semestre seguinte com os titulos que renovaram a licenca (UC08). */
    public Catalogo renovarCatalogo(Catalogo catalogo, List<Aluno> alunos) {
        if (catalogo == null) {
            throw new IllegalArgumentException("O catalogo a renovar e obrigatorio");
        }
        return catalogo.renovar(alunos);
    }

    public String getRegistroFuncional() {
        return registroFuncional;
    }

    private boolean vazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
