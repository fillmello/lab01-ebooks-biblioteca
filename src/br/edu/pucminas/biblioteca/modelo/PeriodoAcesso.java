package br.edu.pucminas.biblioteca.modelo;

import java.time.LocalDate;

/** Periodo de acesso do semestre, durante o qual a estante pode ser alterada. */
public class PeriodoAcesso {

    private LocalDate dataInicio;
    private LocalDate dataFim;

    public PeriodoAcesso(LocalDate dataInicio, LocalDate dataFim) {
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public boolean estaAberto(LocalDate data) {
        // TODO: implementar na Sprint 3
        return false;
    }

    public boolean jaEncerrou(LocalDate data) {
        // TODO: implementar na Sprint 3
        return false;
    }
}
