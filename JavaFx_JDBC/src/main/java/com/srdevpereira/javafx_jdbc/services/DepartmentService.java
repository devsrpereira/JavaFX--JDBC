package com.srdevpereira.javafx_jdbc.services;

import com.srdevpereira.javafx_jdbc.Department;
import com.srdevpereira.javafx_jdbc.dao.DaoFactory;
import com.srdevpereira.javafx_jdbc.dao.DepartmentDao;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {

    private DepartmentDao dao = DaoFactory.createDepartmentDao();

    public List<Department> findAll(){
        return dao.findAll();
    }

    public void saveOrUpdate(Department obj){
        if (obj.getId() == null) {
            dao.insert(obj);
        }
        else {
            dao.updade(obj);
        }
    }

    public void remove(Department obj){
        dao.deleteById(obj.getId());
    }
}
