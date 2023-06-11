package com.srdevpereira.javafx_jdbc.services;

import com.srdevpereira.javafx_jdbc.Seller;
import com.srdevpereira.javafx_jdbc.dao.DaoFactory;
import com.srdevpereira.javafx_jdbc.dao.SellerDao;

import java.util.List;

public class SellerService {

    private SellerDao dao = DaoFactory.createSellerDao();

    public List<Seller> findAll(){
        return dao.findAll();
    }

    public void saveOrUpdate(Seller obj){
        if (obj.getId() == null) {
            dao.insert(obj);
        }
        else {
            dao.updade(obj);
        }
    }

    public void remove(Seller obj){
        dao.deleteById(obj.getId());
    }
}
