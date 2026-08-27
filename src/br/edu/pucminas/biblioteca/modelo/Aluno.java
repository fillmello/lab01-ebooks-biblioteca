package br.edu.pucminas.biblioteca.modelo;

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
        estante.adicionar(ebook, tipo);
        return false;
    }

    public String listarEbooks(){
        StringBuilder stringBuilder = new StringBuilder();
        for (ItemEstante itemEstante : estante.listar()){
            stringBuilder.append("Tipo: " + itemEstante.getTipo() + "\n");
            stringBuilder.append(itemEstante.getEBook());
        }
        return stringBuilder.toString();
    }
    public boolean removerEBook(EBook ebook) {
        // TODO: implementar na Sprint 3
        return false;
    }

    public boolean acessarEBook(EBook ebook) {
        // TODO: implementar na Sprint 3
        return false;
    }

    public String getMatricula() {
        return matricula;
    }

    public Estante getEstante() {
        return estante;
    }

    public boolean validiar(String senha, String matricula){
        System.err.println("TESTES MATRICULA: " + matricula + " " + this.matricula);
        System.err.println(matricula.equals(this.matricula));
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
