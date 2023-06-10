package com.srdevpereira.javafx_jdbc.services;

import com.srdevpereira.javafx_jdbc.Departamento;

import java.util.ArrayList;
import java.util.List;

public class DepartamentoService {

    public List<Departamento> findAll(){
        List<Departamento> list = new ArrayList<>();
        list.add(new Departamento(1,"Games"));
        list.add(new Departamento(2, "Livros"));
        list.add(new Departamento(3, "Culinária"));
        list.add(new Departamento(4, "Computadores"));
        list.add(new Departamento(5, "Ferramentas"));
        return list;
    }
}
