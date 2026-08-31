package br.edu.pucminas.biblioteca.persistencia;

import br.edu.pucminas.biblioteca.modelo.Aluno;
import br.edu.pucminas.biblioteca.modelo.Bibliotecario;
import br.edu.pucminas.biblioteca.modelo.Usuario;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Grava e le os usuarios do sistema, um por linha:
 * ALUNO;id;nome;senha;matricula ou BIBLIOTECARIO;id;nome;senha;registroFuncional.
 */
public class UsuarioRepositorioArquivo {

    private static final String ARQUIVO = "usuarios.txt";
    private static final String TIPO_ALUNO = "ALUNO";
    private static final String TIPO_BIBLIOTECARIO = "BIBLIOTECARIO";

    public void salvar(List<Usuario> usuarios) throws IOException {
        File arquivo = Arquivos.preparar(ARQUIVO);
        try (PrintWriter escritor = new PrintWriter(new FileWriter(arquivo))) {
            for (Usuario usuario : usuarios) {
                String tipo = usuario instanceof Aluno ? TIPO_ALUNO : TIPO_BIBLIOTECARIO;
                String vinculo = usuario instanceof Aluno
                        ? ((Aluno) usuario).getMatricula()
                        : ((Bibliotecario) usuario).getRegistroFuncional();
                escritor.println(tipo + Arquivos.SEPARADOR
                        + usuario.getId() + Arquivos.SEPARADOR
                        + usuario.getNome() + Arquivos.SEPARADOR
                        + usuario.getSenha() + Arquivos.SEPARADOR
                        + vinculo);
            }
        }
    }

    public List<Usuario> carregar() throws IOException {
        List<Usuario> usuarios = new ArrayList<>();
        File arquivo = Arquivos.preparar(ARQUIVO);
        if (!arquivo.exists()) {
            return usuarios;
        }
        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                String[] campos = linha.split(Arquivos.SEPARADOR);
                if (campos.length < 5) {
                    continue;
                }
                if (TIPO_ALUNO.equals(campos[0])) {
                    usuarios.add(new Aluno(campos[1], campos[2], campos[3], campos[4]));
                } else if (TIPO_BIBLIOTECARIO.equals(campos[0])) {
                    usuarios.add(new Bibliotecario(campos[1], campos[2], campos[3], campos[4]));
                } else {
                    System.out.println("Linha ignorada em " + ARQUIVO + ": " + linha);
                }
            }
        }
        return usuarios;
    }
}
