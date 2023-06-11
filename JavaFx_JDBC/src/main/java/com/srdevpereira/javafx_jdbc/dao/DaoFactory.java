package com.srdevpereira.javafx_jdbc.dao;

import com.srdevpereira.javafx_jdbc.db.DB;
import com.srdevpereira.javafx_jdbc.dao.impl.DepartmentDaoJDBC;
import com.srdevpereira.javafx_jdbc.dao.impl.SellerDaoJDBC;

public class DaoFactory {

    public static SellerDao createSellerDao(){
        return new SellerDaoJDBC(DB.getConeection());
    }

    public static DepartmentDao createDepartmentDao(){
        return (DepartmentDao) new DepartmentDaoJDBC(DB.getConeection());
    }

}
