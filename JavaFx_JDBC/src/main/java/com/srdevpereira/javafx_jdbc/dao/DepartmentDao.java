package com.srdevpereira.javafx_jdbc.dao;

import com.srdevpereira.javafx_jdbc.Department;

import java.util.List;

public interface DepartmentDao {
    void insert(Department obj);
    void updade(Department obj);
    void deleteById(Integer id);
    Department findById(Integer id);
    List<Department> findAll();
}
