package com.srdevpereira.javafx_jdbc.dao;

import com.srdevpereira.javafx_jdbc.Department;
import com.srdevpereira.javafx_jdbc.Seller;

import java.util.List;

public interface SellerDao {
    void insert(Seller obj);
    void updade(Seller obj);
    void deleteById(Integer id);
    Seller findById(Integer id);
    List<Seller> findAll();
    List<Seller> findByDepartment(Department department);
}
