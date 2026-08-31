package br.edu.pucminas.biblioteca.modelo;

import java.io.Serializable;
import java.time.LocalDate;

/** Periodo de acesso do semestre, durante o qual a estante pode ser alterada. */
public class PeriodoAcesso implements Serializable {

    private LocalDate dataInicio;
    private LocalDate dataFim;

    public PeriodoAcesso(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null || dataFim.isBefore(dataInicio)) {
            throw new IllegalArgumentException("A data de fim deve ser igual ou posterior a de inicio");
        }
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public boolean estaAberto(LocalDate data) {
        return data != null && !data.isBefore(dataInicio) && !data.isAfter(dataFim);
    }

    public boolean jaEncerrou(LocalDate data) {
        return data != null && data.isAfter(dataFim);
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }
}
