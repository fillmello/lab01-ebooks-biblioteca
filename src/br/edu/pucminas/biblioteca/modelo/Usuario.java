package br.edu.pucminas.biblioteca.modelo;

/** Classe-base dos usuarios do sistema (UC01 - Realizar login). */
public abstract class Usuario {

    private String id;
    private String nome;
    private String senha;

    public Usuario(String id, String nome, String senha) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
    }

    public boolean autenticar(String senhaDigitada) {
        // TODO: implementar na Sprint 3
        return false;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
