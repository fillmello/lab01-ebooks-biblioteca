package br.edu.pucminas.biblioteca.modelo;

import java.util.ArrayList;
import java.util.List;

/** Catalogo licenciado de um semestre (UC05 e UC08). */
public class Catalogo {

    private static final int MINIMO_ALUNOS_RENOVACAO = 3;

    private String semestre;
    private List<EBook> ebooks = new ArrayList<>();
    private List<PeriodoAcesso> periodosAcesso = new ArrayList<>();

    public Catalogo(String semestre) {
        this.semestre = semestre;
    }

    public void adicionarEBook(EBook ebook) {
        // TODO: implementar na Sprint 3
    }

    public void removerEBook(EBook ebook) {
        // TODO: implementar na Sprint 3
    }

    public List<EBook> listarEBooks() {
        return ebooks;
    }

    public Catalogo renovar(List<Aluno> alunos) {
        // TODO: implementar na Sprint 3
        // Mantem apenas os eBooks presentes na estante de
        // MINIMO_ALUNOS_RENOVACAO ou mais alunos.
        return null;
    }

    public String getSemestre() {
        return semestre;
    }
}
