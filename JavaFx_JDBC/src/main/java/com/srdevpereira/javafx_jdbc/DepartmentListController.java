package com.srdevpereira.javafx_jdbc;

import com.srdevpereira.javafx_jdbc.db.DbIntegrityException;
import com.srdevpereira.javafx_jdbc.listeners.DataChangeListener;
import com.srdevpereira.javafx_jdbc.services.DepartmentService;
import com.srdevpereira.javafx_jdbc.util.Alerts;
import com.srdevpereira.javafx_jdbc.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable, DataChangeListener {

    private DepartmentService service;
    private ObservableList<Department> obsList;

    @FXML
    private TableView tableViewDepartamentos;
    @FXML
    private TableColumn<Department, Integer> tableColumnId;
    @FXML
    private TableColumn<Department, String> tableColumnNome;
    @FXML
    private TableColumn<Department, Department> tableColumnEDIT;
    @FXML
    private TableColumn<Department, Department> tableColumnREMOVE;
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
        initEditButtons();
        initRemoveButtons();
    }


    @FXML
    public void onBtNovoDptoAction(ActionEvent event){
        Stage parentStage = Utils.currentStage(event);
        Department obj = new Department();
        createDialogForm(obj,"DepartmentForm.fxml",parentStage);
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

    private void createDialogForm(Department obj, String absolutName, Stage parentStage){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
            Pane pane = loader.load();

            DepartmentFormController controller = loader.getController();
            controller.setDepartment(obj);
            controller.setDepartmentService(new DepartmentService());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Entre com os dados do Departamento");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false); //não permite dimencionar a janela
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL); //impede a transição de janela com ela aberta
            dialogStage.showAndWait();
        }
        catch (IOException e){
            e.printStackTrace();
            Alerts.showAlerts("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }

    //cria um botão de atualizar departamentos do banco de dados
    private void initEditButtons() {
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {
            private final Button button = new Button("editar");
            @Override
            protected void updateItem(Department obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> createDialogForm(
                                obj, "DepartmentForm.fxml",Utils.currentStage(event)));
            }
        });
    }

    // cria botões para remover departamentos do banco de dados
    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Department, Department>() {
            private final Button button = new Button("remover");
            @Override
            protected void updateItem(Department obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> removeEntity(obj));
            }
        });
    }

    private void removeEntity(Department obj) {
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Realmente deseja excluir?");

        if(result.get() == ButtonType.OK){
            if(service == null){
                throw new IllegalStateException("Service estava vazio");
            }
            try{
                service.remove(obj);
                updateTableView();
            }
            catch (DbIntegrityException e){
                Alerts.showAlerts("Error ao excluir", null, e.getMessage(), Alert.AlertType.ERROR);
            }
        }

    }

}
