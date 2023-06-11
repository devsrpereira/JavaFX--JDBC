package com.srdevpereira.javafx_jdbc;

import com.srdevpereira.javafx_jdbc.services.DepartmentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable {

    private DepartmentService service;
    private ObservableList<Department> obsList;

    @FXML
    private TableView tableViewDepartamentos;
    @FXML
    private TableColumn<Department, Integer> tableColumnId;
    @FXML
    private TableColumn<Department, String> tableColumnNome;
    @FXML
    private Button btNovoDpto;


    public void setDepartamentoService(DepartmentService service) {
        this.service = service;
    }

    public void updateTableView(){
        if(service == null){
            throw new IllegalStateException("Service está vazio");
        }
        List<Department> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewDepartamentos.setItems(obsList);
    }


    @FXML
    public void onBtNovoDptoAction(){
        System.out.println("onBtNovoDptoAction");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNode();
        
    }

    private void initializeNode() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("Nome"));

        Stage stage = (Stage) MainViewApp.getMainScene().getWindow();
        tableViewDepartamentos.prefHeightProperty().bind(stage.heightProperty());
    }
}
