package com.srdevpereira.javafx_jdbc;

import com.srdevpereira.javafx_jdbc.services.DepartamentoService;
import com.srdevpereira.javafx_jdbc.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class MainViewController implements Initializable {

    @FXML
    private MenuItem menuItemVendedor;
    @FXML
    private MenuItem menuItemDepartamento;
    @FXML
    private MenuItem menuItemSobre;


    @FXML
    public void onMenuItemVendedorAction(){
        System.out.println("onMenuItemVendedorAction");
    }

    @FXML
    public void onMenuItemDepartamentoAction(){
        loadView("DepartamentoList.fxml", (DepartamentoListController controller) ->{
            controller.setDepartamentoService(new DepartamentoService());
            controller.updateTableView();
        });
    }

    @FXML
    public void onMenuItemSobreAction(){
        loadView("SobreView.fxml", x->{});
    }


    @Override
    public void initialize(URL uri, ResourceBundle rb) {

    }

    private synchronized <T> void loadView(String absoluteName, Consumer<T> initializeAction){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            VBox newVBox = loader.load();

            Scene mainScene = MainViewApp.getMainScene();
            VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

            Node mainMenu = mainVBox.getChildren().get(0);
            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(newVBox.getChildren());

            T controller = loader.getController();
            initializeAction.accept(controller);


        } catch (IOException e) {
            Alerts.showAlerts("IO Exception", "Erro na pagina", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
//PARTE MANUAL DO MODULO LOAD2
//    private synchronized void loadView2(String absoluteName){
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
//            VBox newVBox = loader.load();
//
//            Scene mainScene = MainViewApp.getMainScene();
//            VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
//
//            Node mainMenu = mainVBox.getChildren().get(0);
//            mainVBox.getChildren().clear();
//            mainVBox.getChildren().add(mainMenu);
//            mainVBox.getChildren().addAll(newVBox.getChildren());
//
//            DepartamentoListController controller = loader.getController();
//            controller.setDepartamentoService(new DepartamentoService());
//            controller.updateTableView();
//
//        } catch (IOException e) {
//            Alerts.showAlerts("IO Exception", "Erro na pagina", e.getMessage(), Alert.AlertType.ERROR);
//        }
//    }
