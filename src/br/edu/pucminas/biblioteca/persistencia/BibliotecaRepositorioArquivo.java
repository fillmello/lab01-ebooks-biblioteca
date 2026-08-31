package br.edu.pucminas.biblioteca.persistencia;

import br.edu.pucminas.biblioteca.modelo.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Grava e le os dados do sistema no arquivo dados/biblioteca.dat.
 * O Java grava os objetos inteiros, entao nao e preciso montar nem separar texto.
 */
public class BibliotecaRepositorioArquivo {

    private static final String PASTA = "dados";
    private static final String ARQUIVO = "dados/biblioteca.dat";

    /** As tres coisas que o sistema grava e le de uma vez so. */
    public static class Dados {
        public Catalogo catalogo;
        public List<Usuario> usuarios;
        public SistemaEstatisticas estatisticas;
    }

    public void salvar(Catalogo catalogo, List<Usuario> usuarios, SistemaEstatisticas estatisticas) {
        try {
            new File(PASTA).mkdirs();

            ObjectOutputStream saida = new ObjectOutputStream(new FileOutputStream(ARQUIVO));
            saida.writeObject(catalogo);
            saida.writeObject(usuarios);
            saida.writeObject(estatisticas);
            saida.close();

        } catch (Exception e) {
            System.out.println("Erro ao salvar: " + e.getMessage());
        }
    }

    /** Le o arquivo. Se ele ainda nao existe, cria o acervo de exemplo e grava. */
    @SuppressWarnings("unchecked")
    public Dados carregar() {
        try {
            ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ARQUIVO));

            Dados dados = new Dados();
            dados.catalogo = (Catalogo) entrada.readObject();
            dados.usuarios = (List<Usuario>) entrada.readObject();
            dados.estatisticas = (SistemaEstatisticas) entrada.readObject();
            entrada.close();

            return dados;

        } catch (Exception e) {
            System.out.println("Primeira execucao: criando os dados iniciais em " + ARQUIVO + ".");
            Dados dados = dadosIniciais();
            salvar(dados.catalogo, dados.usuarios, dados.estatisticas);
            return dados;
        }
    }

    /** Acervo de exemplo, para o sistema abrir com conteudo na primeira execucao. */
    public Dados dadosIniciais() {
        LocalDate hoje = LocalDate.now();

        Catalogo catalogo = new Catalogo(hoje.getYear() + "/" + (hoje.getMonthValue() <= 6 ? 1 : 2));
        catalogo.adicionarPeriodo(new PeriodoAcesso(hoje.minusDays(30), hoje.plusDays(30)));
        catalogo.adicionarEBook(new EBook("Engenharia de Software", "Pearson",
                Formato.PDF, Categoria.TECNICO, new Licenca()));
        catalogo.adicionarEBook(new EBook("Padroes de Projeto", "Bookman",
                Formato.EPUB, Categoria.TECNICO, new Licenca()));
        catalogo.adicionarEBook(new EBook("Banco de Dados", "Elsevier",
                Formato.PDF, Categoria.TECNICO, new Licenca()));
        catalogo.adicionarEBook(new EBook("Grande Sertao Veredas", "Companhia das Letras",
                Formato.EPUB, Categoria.LITERATURA, new Licenca()));
        catalogo.adicionarEBook(new EBook("Memorias Postumas de Bras Cubas", "Penguin",
                Formato.EPUB, Categoria.LITERATURA, new Licenca()));
        // Licenca com uma unica vaga, para demonstrar o bloqueio por acesso simultaneo.
        catalogo.adicionarEBook(new EBook("Revista Brasileira de Computacao", "SBC",
                Formato.PDF, Categoria.PERIODICO, new Licenca(1)));

        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Bibliotecario("carla", "Carla Bibliotecaria", "123", "BIB-001"));
        usuarios.add(new Aluno("bruno", "Bruno Alves", "123", "2026001"));
        usuarios.add(new Aluno("ana", "Ana Souza", "123", "2026002"));
        usuarios.add(new Aluno("lucas", "Lucas Pereira", "123", "2026003"));
        usuarios.add(new Aluno("mariana", "Mariana Dias", "123", "2026004"));

        Dados dados = new Dados();
        dados.catalogo = catalogo;
        dados.usuarios = usuarios;
        dados.estatisticas = new SistemaEstatisticas();
        return dados;
    }
}
