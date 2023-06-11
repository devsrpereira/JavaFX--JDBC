package com.srdevpereira.javafx_jdbc;

import com.srdevpereira.javafx_jdbc.services.DepartmentService;
import com.srdevpereira.javafx_jdbc.util.Alerts;
import com.srdevpereira.javafx_jdbc.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
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
    public void onBtNovoDptoAction(ActionEvent event){
        Stage parentStage = Utils.currentStage(event);
        createDialogForm("DepartmentForm.fxml",parentStage);
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

    private void createDialogForm(String absolutName, Stage parentStage){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
            Pane pane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Entre com os dados do Departamento");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false); //não permite dimencionar a janela
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL); //impede a transição de janela com ela aberta
            dialogStage.showAndWait();
        }
        catch (IOException e){
            Alerts.showAlerts("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

}
