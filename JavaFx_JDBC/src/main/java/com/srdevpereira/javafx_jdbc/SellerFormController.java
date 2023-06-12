package com.srdevpereira.javafx_jdbc;

import com.srdevpereira.javafx_jdbc.db.DbException;
import com.srdevpereira.javafx_jdbc.exceptions.ValidationException;
import com.srdevpereira.javafx_jdbc.listeners.DataChangeListener;
import com.srdevpereira.javafx_jdbc.services.SellerService;
import com.srdevpereira.javafx_jdbc.util.Alerts;
import com.srdevpereira.javafx_jdbc.util.Constraints;
import com.srdevpereira.javafx_jdbc.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class SellerFormController implements Initializable {

    private Seller entity;
    private SellerService service;
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private DatePicker dpBirthDate;
    @FXML
    private TextField txtBaseSalary;
    @FXML
    private Label labelErrorName;
    @FXML
    private Label labelErrorEmail;
    @FXML
    private Label labelErrorBirthDate;
    @FXML
    private Label labelErrorBaseSalary;
    @FXML
    private Button btSave;
    @FXML
    private Button btCancel;

    public void setSeller(Seller entity) {
        this.entity = entity;
    }
    public void setSellerService(SellerService service) {
        this.service = service;
    }
    public void subscribeDataChangeListener(DataChangeListener listener){
        dataChangeListeners.add(listener);
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
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        }
        catch (ValidationException e){
            setErrorMessages(e.getErrors());
        }
        catch (DbException e){
            Alerts.showAlerts("Error não foi possivel salvar", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners){
            listener.onDataChanged();
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

    private Seller getFormData() {
        Seller obj = new Seller();

        ValidationException exception = new ValidationException("Erro de validação");

        obj.setId(Utils.tryParseToInt(txtId.getText()));

        if(txtName.getText() == null || txtName.getText().trim().equals("")){ //.trim elimina espaços vazio no inicio e no final
            exception.addErrors("name", "O campo não pode ser vazio" );
        }
        obj.setNome(txtName.getText());

        if(exception.getErrors().size() > 0){
            throw exception;
        }


        return obj;
    }

    private void initializeNodes(){
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 70);
        Constraints.setTextFieldDouble(txtBaseSalary);
        Constraints.setTextFieldMaxLength(txtEmail, 60);
        Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
    }

    public void updateFormData(){
        if(entity == null){
            throw new IllegalStateException("Entity was null");
        }
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getNome());
        txtEmail.setText(entity.getEmail());
        Locale.setDefault(Locale.US);
        txtBaseSalary.setText(String.format("%.2f",entity.getBaseSalary()));
        if(entity.getBirthDate() != null) {
            dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault())); //instacia a data considerando o fuso do computadr do usuario
        }
    }

    private void setErrorMessages(Map<String, String> error){
        Set<String> fields = error.keySet();
        if (fields.contains("name")){
            labelErrorName.setText(error.get("name"));
        }
    }
}
