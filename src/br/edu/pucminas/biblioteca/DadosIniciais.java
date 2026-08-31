package br.edu.pucminas.biblioteca;

import br.edu.pucminas.biblioteca.modelo.Aluno;
import br.edu.pucminas.biblioteca.modelo.Bibliotecario;
import br.edu.pucminas.biblioteca.modelo.Catalogo;
import br.edu.pucminas.biblioteca.modelo.Categoria;
import br.edu.pucminas.biblioteca.modelo.EBook;
import br.edu.pucminas.biblioteca.modelo.Formato;
import br.edu.pucminas.biblioteca.modelo.Licenca;
import br.edu.pucminas.biblioteca.modelo.PeriodoAcesso;
import br.edu.pucminas.biblioteca.modelo.Usuario;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Carga usada apenas na primeira execucao, quando a pasta dados/ ainda esta vazia.
 * A partir da segunda execucao os dados vem dos arquivos gravados.
 */
public final class DadosIniciais {

    private DadosIniciais() {
    }

    public static Catalogo criarCatalogo() {
        LocalDate hoje = LocalDate.now();
        Catalogo catalogo = new Catalogo(semestreDe(hoje));
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
        return catalogo;
    }

    public static List<Usuario> criarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Bibliotecario("carla", "Carla Bibliotecaria", "123", "BIB-001"));
        usuarios.add(new Aluno("bruno", "Bruno Alves", "123", "2026001"));
        usuarios.add(new Aluno("ana", "Ana Souza", "123", "2026002"));
        usuarios.add(new Aluno("lucas", "Lucas Pereira", "123", "2026003"));
        usuarios.add(new Aluno("mariana", "Mariana Dias", "123", "2026004"));
        return usuarios;
    }

    /** Primeiro semestre ate junho, segundo semestre de julho em diante. */
    private static String semestreDe(LocalDate data) {
        return data.getYear() + "/" + (data.getMonthValue() <= 6 ? 1 : 2);
    }
}
