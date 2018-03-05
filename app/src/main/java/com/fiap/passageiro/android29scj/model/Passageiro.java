package com.fiap.passageiro.android29scj.model;

import java.io.Serializable;

/**
 * Created by marcelo on 18/02/2018.
 */

public class Passageiro implements Serializable {
    private int id;
    private String nome;
    private String sexo;
    private String uf;
    private boolean primeiraClasse;

    public Passageiro(int id, String nome, String sexo, String uf, boolean primeiraClasse){
        this.id = id;
        this.nome = nome;
        this.sexo = sexo;
        this.uf = uf;
        this.primeiraClasse = primeiraClasse;
    }

    public int getId(){ return this.id; }
    public String getNome(){ return this.nome; }
    public String getSexo(){ return this.sexo; }
    public boolean getPrimeiraClasse(){ return this.primeiraClasse; }
    public String getUf(){ return this.uf; }

    @Override
    public boolean equals(Object o){
        return this.id == ((Passageiro)o).id;
    }

    @Override
    public int hashCode(){
        return this.id;
    }
}
