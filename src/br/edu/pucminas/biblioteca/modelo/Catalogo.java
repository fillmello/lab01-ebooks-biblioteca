package br.edu.pucminas.biblioteca.modelo;

import java.time.LocalDate;
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
        if (ebook != null && !ebooks.contains(ebook)) {
            ebooks.add(ebook);
        }
    }

    public void removerEBook(EBook ebook) {
        ebooks.remove(ebook);
    }

    public List<EBook> listarEBooks() {
        return ebooks;
    }

    public EBook buscarPorTitulo(String titulo) {
        if (titulo == null) {
            return null;
        }
        for (EBook ebook : ebooks) {
            if (ebook.getTitulo().equalsIgnoreCase(titulo.trim())) {
                return ebook;
            }
        }
        return null;
    }

    public void adicionarPeriodo(PeriodoAcesso periodo) {
        if (periodo != null) {
            periodosAcesso.add(periodo);
        }
    }

    public List<PeriodoAcesso> listarPeriodos() {
        return periodosAcesso;
    }

    /** Periodo de acesso aberto na data informada, ou null quando nenhum esta aberto. */
    public PeriodoAcesso periodoAbertoEm(LocalDate data) {
        for (PeriodoAcesso periodo : periodosAcesso) {
            if (periodo.estaAberto(data)) {
                return periodo;
            }
        }
        return null;
    }

    /** O eBook so pode ir para uma estante se estiver no catalogo e o periodo estiver aberto. */
    public boolean podeAdicionar(EBook ebook, LocalDate data) {
        return ebook != null && ebooks.contains(ebook) && periodoAbertoEm(data) != null;
    }

    /** Verdadeiro quando todos os periodos de acesso do semestre ja terminaram. */
    public boolean periodoDeAcessoEncerrado(LocalDate data) {
        if (periodosAcesso.isEmpty()) {
            return false;
        }
        for (PeriodoAcesso periodo : periodosAcesso) {
            if (!periodo.jaEncerrou(data)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gera o catalogo do semestre seguinte, mantendo apenas os eBooks presentes
     * na estante de MINIMO_ALUNOS_RENOVACAO ou mais alunos.
     */
    public Catalogo renovar(List<Aluno> alunos) {
        Catalogo proximo = new Catalogo(proximoSemestre());
        for (EBook ebook : ebooks) {
            if (contarAlunosCom(ebook, alunos) >= MINIMO_ALUNOS_RENOVACAO) {
                proximo.adicionarEBook(ebook);
            }
        }
        return proximo;
    }

    public int contarAlunosCom(EBook ebook, List<Aluno> alunos) {
        int total = 0;
        if (alunos == null) {
            return total;
        }
        for (Aluno aluno : alunos) {
            if (aluno.getEstante().contemEBook(ebook)) {
                total++;
            }
        }
        return total;
    }

    public String getSemestre() {
        return semestre;
    }

    public static int getMinimoAlunosRenovacao() {
        return MINIMO_ALUNOS_RENOVACAO;
    }

    /** Converte "2026/1" em "2026/2" e "2026/2" em "2027/1". */
    private String proximoSemestre() {
        String[] partes = semestre.split("/");
        if (partes.length == 2) {
            try {
                int ano = Integer.parseInt(partes[0].trim());
                int periodo = Integer.parseInt(partes[1].trim());
                return periodo == 1 ? ano + "/2" : (ano + 1) + "/1";
            } catch (NumberFormatException e) {
                return semestre;
            }
        }
        return semestre;
    }
}
