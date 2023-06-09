package com.srdevpereira.javafx_jdbc;

import com.srdevpereira.javafx_jdbc.model.entities.Departamento;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartamentoListController implements Initializable {

    @FXML
    private TableView tableViewDepartamentos;
    @FXML
    private TableColumn<Departamento, Integer> tableColumnId;
    @FXML
    private TableColumn<Departamento, String> tableColumnNome;
    @FXML
    private Button btNovoDpto;

    @FXML
    public void onBtNovoDptoAction(){
        System.out.println("onBtNovoDptoAction");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNode();
        
    }

    private void initializeNode() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        Stage stage = (Stage) MainViewApp.getMainScene().getWindow();
        tableViewDepartamentos.prefHeightProperty().bind(stage.heightProperty());
    }
}
