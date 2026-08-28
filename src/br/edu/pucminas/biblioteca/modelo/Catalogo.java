package br.edu.pucminas.biblioteca.modelo;

import java.io.EOFException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Catalogo licenciado de um semestre (UC05 e UC08). */
public class Catalogo implements Serializable {

    private static final int MINIMO_ALUNOS_RENOVACAO = 3;

    private String semestre;
    private List<EBook> ebooks = new ArrayList<>();
    private List<PeriodoAcesso> periodosAcesso = new ArrayList<>();

    public Catalogo(String semestre) {
        this.semestre = semestre;
    }

    public void adicionarPeriodoDeAcesso(LocalDate inicio, LocalDate fim){
        periodosAcesso.add(new PeriodoAcesso(inicio, fim));
    }

    public void removerPeriodoAcesso(Integer indice){
        periodosAcesso.remove(periodosAcesso.get(indice));
    }

    public boolean podeAdicionarRemoverEbook() throws Exception{
        if (periodosAcesso.isEmpty()){
            throw new Exception("Sem periodos de acesso, peça a um bibliotecario para adicionar");
        }
        for (PeriodoAcesso periodoAcesso : periodosAcesso){
            if (periodoAcesso.estaAberto()){
                return true;
            }
        }
        return false;
    }

    public void adicionarEBook(EBook ebook) throws Exception {
        // if (!podeAdicionarRemoverEbook()){
        //     throw new Exception("Não é possivel adicionar ebooks, fora do periodo de acesso");
        // }

        if (!ebooks.contains(ebook)) {
            ebooks.add(ebook);
        } else {
            throw new Exception("Ebook ja  existe");
        }
       
        
    }

    public void removerEBook(EBook ebook) {
        if (ebook == null) {
            throw new IllegalArgumentException("O eBook não pode ser nulo.");
        }

        if (!ebooks.remove(ebook)) {
            throw new IllegalArgumentException("eBook não encontrado no catálogo.");
        }
    }

    public List<EBook> listarEBooks() {
        return ebooks;
    }

    public Catalogo renovar(List<Aluno> alunos) {
        List<EBook> novosEbooks = new ArrayList<>();

        for (EBook ebook : ebooks) {
            int quantidadeAlunos = 0;

            for (Aluno aluno : alunos) {
                if (aluno.getEstante().contemEBook(ebook)) {
                    quantidadeAlunos++;
                }
            }

            if (quantidadeAlunos >= MINIMO_ALUNOS_RENOVACAO) {
                novosEbooks.add(ebook);
            }
        }

        ebooks = novosEbooks;
        return this;
    }

    public List<PeriodoAcesso> getPeriodoAcesso() {
        return periodosAcesso;
    }

    public String getSemestre() {
        return semestre;
    }
}
