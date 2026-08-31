package br.edu.pucminas.biblioteca.modelo;

import java.util.Optional;

/** Aluno da universidade (UC02, UC03 e UC04). */
public class Aluno extends Usuario {

    private String matricula;
    private Estante estante;

    public Aluno(String id, String nome, String senha, String matricula) {
        super(id, nome, senha);
        this.matricula = matricula.trim();
        this.estante = new Estante();
    }

    public boolean adicionarEBook(EBook ebook, TipoLeitura tipo) {
        if (ebook.getLicenca().temVagaDisponivel()){
            estante.adicionar(ebook, tipo);
            ebook.getLicenca().ocuparAcesso();
            return true;
        } 

        System.err.println("Ebook não possui vaga disponivel");

        
        return false;
    }

    public String listarEbooks(){
        StringBuilder stringBuilder = new StringBuilder();
        for (ItemEstante itemEstante : estante.listar()){
            stringBuilder.append(itemEstante);
        }
        return stringBuilder.toString();
    }
    public boolean removerEBook(EBook ebook) throws Exception {
        if (estante.remover(ebook)){
            System.err.println("Livro " + ebook.getTitulo() + " removido com sucesso");
            ebook.getLicenca().liberarAcesso();
            return true;
        }
        throw new Exception("Não foi possivel remover o livro " + ebook.getTitulo() + " tente novamente");
    }

    public boolean acessarEBook(EBook ebook) {
        Optional<ItemEstante> itemEstante = estante.listar().stream().filter(ebookAux -> ebookAux.getEBook().equals(ebook)).findFirst();
        if (itemEstante.isPresent()){
            ItemEstante itemEstanteAux = itemEstante.get();
            System.err.println("Acesso ao EBOOK\nTipo: " + itemEstanteAux.getTipo() + "\n" + itemEstanteAux.getEBook());
            return true;
        }
        System.err.println("Não foi possivel acessar o ebook, tente novamente");
        return false;
    }

    public String getMatricula() {
        return matricula;
    }

    public Estante getEstante() {
        return estante;
    }

    public boolean validiar(String senha, String matricula){
        // System.err.println("TESTES MATRICULA: " + matricula + " " + this.matricula);
        // System.err.println(matricula.equals(this.matricula));
        if (matricula.equals(this.matricula) && super.validaSenha(senha)){
            return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return "Nome " + this.getNome() + "\nMatricula: " + this.matricula;
    }
}
