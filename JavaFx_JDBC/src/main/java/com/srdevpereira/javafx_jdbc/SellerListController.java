package com.srdevpereira.javafx_jdbc;

import com.srdevpereira.javafx_jdbc.db.DbIntegrityException;
import com.srdevpereira.javafx_jdbc.listeners.DataChangeListener;
import com.srdevpereira.javafx_jdbc.services.SellerService;
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

public class SellerListController implements Initializable, DataChangeListener {

    private SellerService service;
    private ObservableList<Seller> obsList;

    @FXML
    private TableView tableViewVendedores;
    @FXML
    private TableColumn<Seller, Integer> tableColumnId;
    @FXML
    private TableColumn<Seller, String> tableColumnNome;
    @FXML
    private TableColumn<Seller, Seller> tableColumnEDIT;
    @FXML
    private TableColumn<Seller, Seller> tableColumnREMOVE;
    @FXML
    private Button btNovoVendedor;


    public void setVendendorService(SellerService service) {
        this.service = service;
    }

    public void updateTableView(){
        if(service == null){
            throw new IllegalStateException("Service está vazio");
        }
        List<Seller> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewVendedores.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
    }


    @FXML
    public void onBtNovoVendedorAction(ActionEvent event){
        Stage parentStage = Utils.currentStage(event);
        Seller obj = new Seller();
        createDialogForm(obj,"SellerForm.fxml",parentStage);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNode();
     }

    private void initializeNode() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("Nome"));

        Stage stage = (Stage) MainViewApp.getMainScene().getWindow();
        tableViewVendedores.prefHeightProperty().bind(stage.heightProperty());
    }

    private void createDialogForm(Seller obj, String absolutName, Stage parentStage){
//        try{
//            FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
//            Pane pane = loader.load();
//
//            SellerFormController controller = loader.getController();
//            controller.setSeller(obj);
//            controller.setSellerService(new SellerService());
//            controller.subscribeDataChangeListener(this);
//            controller.updateFormData();
//
//            Stage dialogStage = new Stage();
//            dialogStage.setTitle("Entre com os dados do Vendedor");
//            dialogStage.setScene(new Scene(pane));
//            dialogStage.setResizable(false); //não permite dimencionar a janela
//            dialogStage.initOwner(parentStage);
//            dialogStage.initModality(Modality.WINDOW_MODAL); //impede a transição de janela com ela aberta
//            dialogStage.showAndWait();
//        }
//        catch (IOException e){
//            Alerts.showAlerts("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
//        }
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }

    //cria um botão de atualizar Vendedores do banco de dados
    private void initEditButtons() {
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
            private final Button button = new Button("editar");
            @Override
            protected void updateItem(Seller obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> createDialogForm(
                                obj, "SellerForm.fxml",Utils.currentStage(event)));
            }
        });
    }

    // cria botões para remover Vendedores do banco de dados
    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
            private final Button button = new Button("remover");
            @Override
            protected void updateItem(Seller obj, boolean empty) {
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

    private void removeEntity(Seller obj) {
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
