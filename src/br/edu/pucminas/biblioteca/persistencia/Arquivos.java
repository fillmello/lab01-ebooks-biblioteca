package br.edu.pucminas.biblioteca.persistencia;

import java.io.File;
import java.io.IOException;

/** Utilitarios comuns aos repositorios em arquivo texto. */
class Arquivos {

    static final String PASTA = "dados";
    static final String SEPARADOR = ";";

    private Arquivos() {
    }

    /** Devolve o arquivo dentro da pasta de dados, criando a pasta se ainda nao existir. */
    static File preparar(String nome) throws IOException {
        File pasta = new File(PASTA);
        if (!pasta.exists() && !pasta.mkdirs()) {
            throw new IOException("Nao foi possivel criar a pasta " + PASTA);
        }
        return new File(pasta, nome);
    }
}
