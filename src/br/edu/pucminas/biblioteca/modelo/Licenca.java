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
        return acessosAtivos < limiteAcessosSimultaneos;
    }

    public boolean ocuparAcesso() {
        if (temVagaDisponivel()){
            acessosAtivos++;
            return true;
        }
        return false;
    }

    public void liberarAcesso() {
        if (acessosAtivos > 0){
            acessosAtivos--;
        }
    }

    public int getAcessosAtivos() {
        return acessosAtivos;
    }
}
