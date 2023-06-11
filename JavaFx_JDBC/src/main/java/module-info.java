module com.srdevpereira.javafx_jdbc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.srdevpereira.javafx_jdbc to javafx.fxml;
    exports com.srdevpereira.javafx_jdbc;
}