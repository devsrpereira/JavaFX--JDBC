package com.srdevpereira.javafx_jdbc;

import java.io.Serializable;
import java.util.Objects;

public class Department implements Serializable {

    private Integer id;
    private String nome;

    public Department(){}
    public Department(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }


    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Department that = (Department) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Departamento [id: " + id + ", nome: " + nome + "]";
    }
}
