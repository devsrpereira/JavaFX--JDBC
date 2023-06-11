package com.srdevpereira.javafx_jdbc;

import com.srdevpereira.javafx_jdbc.db.DbException;
import com.srdevpereira.javafx_jdbc.services.DepartmentService;
import com.srdevpereira.javafx_jdbc.util.Alerts;
import com.srdevpereira.javafx_jdbc.util.Constraints;
import com.srdevpereira.javafx_jdbc.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

    private Department entity;
    private DepartmentService service;

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private Label labelErrorName;
    @FXML
    private Button btSave;
    @FXML
    private Button btCancel;

    public void setDepartment(Department entity) {
        this.entity = entity;
    }
    public void setDepartmentService(DepartmentService service) {
        this.service = service;
    }

    @FXML
    public void onBtSaveAction(ActionEvent event){
        if (entity == null){
            throw new IllegalStateException("Entity estava nulo");
        }
        if (service == null){
            throw new IllegalStateException("Service estava nulo");
        }
        try {
            entity = getFormData();
            service.saveOrUpdate(entity);
            Utils.currentStage(event).close();
        }
        catch (DbException e){
            Alerts.showAlerts("Error não foi possivel salvar", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onBtCancelAction(ActionEvent event){
        Utils.currentStage(event).close();
    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    private Department getFormData() {
        Department obj = new Department();

        obj.setId(Utils.tryParseToInt(txtId.getText()));
        obj.setNome(txtName.getText());

        return obj;
    }

    private void initializeNodes(){
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
    }

    public void updateFormData(){
        if(entity == null){
            throw new IllegalStateException("Entity was null");
        }
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getNome());
    }


}
