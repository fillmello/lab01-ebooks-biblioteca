package br.edu.pucminas.biblioteca.modelo;

import java.io.Serializable;
import java.time.LocalDate;

/** Periodo de acesso do semestre, durante o qual a estante pode ser alterada. */
public class PeriodoAcesso implements Serializable{

    private LocalDate dataInicio;
    private LocalDate dataFim;

    public PeriodoAcesso(LocalDate dataInicio, LocalDate dataFim) {
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    // public boolean estaAberto(LocalDate data) {
    public boolean estaAberto() {
        LocalDate agora = LocalDate.now();
        return agora.isAfter(dataInicio) && agora.isBefore(dataFim);
    }

    // public boolean jaEncerrou(LocalDate data) {
    public boolean jaEncerrou() {
        return !estaAberto();
    }

    @Override
    public String toString(){
        return "Data inicial: " + dataInicio + "\nData Final: " + dataFim;
    }
}
