package br.edu.pucminas.biblioteca.modelo;

import java.io.Serializable;

/** Classe-base dos usuarios do sistema (UC01 - Realizar login). */
public abstract class Usuario implements Serializable{

    private String id;
    private String nome;
    private String senha;

    public Usuario(String id, String nome, String senha) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
