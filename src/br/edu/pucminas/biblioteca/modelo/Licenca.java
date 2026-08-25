package br.edu.pucminas.biblioteca.modelo;

import java.io.Serializable;

/** Licenca de uso de um eBook (UC04 - Acessar eBook). */
public class Licenca implements Serializable{

    private int limiteAcessosSimultaneos;
    private int acessosAtivos;

    public Licenca(int limiteAcessosSimultaneos) {
        this.limiteAcessosSimultaneos = limiteAcessosSimultaneos;
        this.acessosAtivos = 0;
    }

    public boolean temVagaDisponivel() {
        // TODO: implementar na Sprint 3
        return false;
    }

    public boolean ocuparAcesso() {
        // TODO: implementar na Sprint 3
        return false;
    }

    public void liberarAcesso() {
        // TODO: implementar na Sprint 3
    }

    public int getAcessosAtivos() {
        return acessosAtivos;
    }
}
