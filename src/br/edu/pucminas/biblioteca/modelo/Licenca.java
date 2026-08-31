package br.edu.pucminas.biblioteca.modelo;

/** Licenca de uso de um eBook (UC04 - Acessar eBook). */
public class Licenca {

    /** Teto de acessos simultaneos a um mesmo eBook, definido no enunciado. */
    public static final int LIMITE_MAXIMO_SIMULTANEOS = 60;

    private int limiteAcessosSimultaneos;
    private int acessosAtivos;

    public Licenca() {
        this(LIMITE_MAXIMO_SIMULTANEOS);
    }

    public Licenca(int limiteAcessosSimultaneos) {
        if (limiteAcessosSimultaneos < 1 || limiteAcessosSimultaneos > LIMITE_MAXIMO_SIMULTANEOS) {
            throw new IllegalArgumentException(
                    "O limite de acessos simultaneos deve ficar entre 1 e " + LIMITE_MAXIMO_SIMULTANEOS);
        }
        this.limiteAcessosSimultaneos = limiteAcessosSimultaneos;
        this.acessosAtivos = 0;
    }

    public boolean temVagaDisponivel() {
        return acessosAtivos < limiteAcessosSimultaneos;
    }

    /** Ocupa uma licenca de uso. Retorna false quando todas estao em uso. */
    public boolean ocuparAcesso() {
        if (!temVagaDisponivel()) {
            return false;
        }
        acessosAtivos++;
        return true;
    }

    /** Libera uma licenca em uso, desbloqueando um novo acesso ao eBook. */
    public void liberarAcesso() {
        if (acessosAtivos > 0) {
            acessosAtivos--;
        }
    }

    public int getAcessosAtivos() {
        return acessosAtivos;
    }

    public int getLimiteAcessosSimultaneos() {
        return limiteAcessosSimultaneos;
    }
}
