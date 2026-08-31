package br.edu.pucminas.biblioteca.modelo;

import java.time.LocalDate;

/** Aluno da universidade (UC02, UC03 e UC04). */
public class Aluno extends Usuario {

    private String matricula;
    private Estante estante;

    public Aluno(String id, String nome, String senha, String matricula) {
        super(id, nome, senha);
        this.matricula = matricula;
        this.estante = new Estante();
    }

    /**
     * Adiciona o eBook a estante e, quando a adicao ocorre, notifica o sistema
     * de estatisticas de uso (UC02 inclui UC07).
     */
    public boolean adicionarEBook(EBook ebook, TipoLeitura tipo, PeriodoAcesso periodo, LocalDate data,
            SistemaEstatisticas estatisticas) {
        boolean adicionado = estante.adicionar(ebook, tipo, periodo, data);
        if (adicionado && estatisticas != null) {
            estatisticas.registrarAdicao(ebook);
        }
        return adicionado;
    }

    public boolean removerEBook(EBook ebook, PeriodoAcesso periodo, LocalDate data) {
        return estante.remover(ebook, periodo, data);
    }

    /** Ocupa uma licenca do eBook, desde que ele esteja na estante do aluno. */
    public boolean acessarEBook(EBook ebook) {
        if (ebook == null || !estante.contemEBook(ebook)) {
            return false;
        }
        return ebook.getLicenca().ocuparAcesso();
    }

    /** Libera a licenca ocupada pelo aluno, desbloqueando o acesso para outro. */
    public boolean encerrarAcessoEBook(EBook ebook) {
        if (ebook == null || !estante.contemEBook(ebook)) {
            return false;
        }
        ebook.getLicenca().liberarAcesso();
        return true;
    }

    public String getMatricula() {
        return matricula;
    }

    public Estante getEstante() {
        return estante;
    }
}
