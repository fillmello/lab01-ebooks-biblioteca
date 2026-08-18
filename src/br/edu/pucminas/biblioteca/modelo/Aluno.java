package br.edu.pucminas.biblioteca.modelo;

import java.util.ArrayList;
import java.util.List;

/** Aluno da universidade (UC02, UC03 e UC04). */
public class Aluno extends Usuario {

    private String matricula;
    private Estante estante;
    private List<Disciplina> disciplinas = new ArrayList<>();

    public Aluno(String id, String nome, String senha, String matricula) {
        super(id, nome, senha);
        this.matricula = matricula;
        this.estante = new Estante();
    }

    /**
     * O tipo de leitura nao e informado pelo aluno: e derivado das disciplinas
     * em que ele esta matriculado, para que nao seja possivel declarar como
     * obrigatoria uma leitura que a disciplina nao indicou.
     */
    public boolean adicionarEBook(EBook ebook) {
        // TODO: implementar na Sprint 3
        return false;
    }

    public TipoLeitura classificarLeitura(EBook ebook) {
        for (Disciplina disciplina : disciplinas) {
            if (disciplina.indicou(ebook)) {
                return TipoLeitura.OBRIGATORIA;
            }
        }
        return TipoLeitura.LIVRE;
    }

    public void matricular(Disciplina disciplina) {
        // TODO: implementar na Sprint 3
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
