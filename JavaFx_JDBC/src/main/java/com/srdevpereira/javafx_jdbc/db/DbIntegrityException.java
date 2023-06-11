package com.srdevpereira.javafx_jdbc.db;

public class DbIntegrityException extends RuntimeException{
    public DbIntegrityException(String msg){
        super(msg);
    }
}
