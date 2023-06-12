package com.srdevpereira.javafx_jdbc;

import com.srdevpereira.javafx_jdbc.db.DbException;
import com.srdevpereira.javafx_jdbc.exceptions.ValidationException;
import com.srdevpereira.javafx_jdbc.listeners.DataChangeListener;
import com.srdevpereira.javafx_jdbc.services.DepartmentService;
import com.srdevpereira.javafx_jdbc.services.SellerService;
import com.srdevpereira.javafx_jdbc.util.Alerts;
import com.srdevpereira.javafx_jdbc.util.Constraints;
import com.srdevpereira.javafx_jdbc.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class SellerFormController implements Initializable {

    private Seller entity;
    private SellerService service;
    private DepartmentService departmentService;
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
    private ComboBox<Department> comboBoxDepartment;
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

    private ObservableList<Department> obsList;

    public void setSeller(Seller entity) {
        this.entity = entity;
    }
    public void setServices(SellerService service, DepartmentService departmentService) {
        this.service = service;
        this.departmentService = departmentService;
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

        if(txtEmail.getText() == null || txtEmail.getText().trim().equals("")){
            exception.addErrors("email", "O campo não pode ser vazio");
        }
        obj.setEmail(txtEmail.getText());

        if (dpBirthDate.getValue() == null){
            exception.addErrors("birthDate", "O campo não pode ser vazio");
        }
        else {
            Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault())); // assim pegamos um valor do DatePicker
            obj.setBirthDate(Date.from(instant));
        }

        if(txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")){
            exception.addErrors("baseSalary", "O campo não pode ser vazio");
        }
        obj.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));

        obj.setDepartment(comboBoxDepartment.getValue());

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

        initializeComboBoxDepartment();
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
        if (entity.getDepartment() == null) {
            comboBoxDepartment.getSelectionModel().selectFirst();
        }
        else {
            comboBoxDepartment.setValue(entity.getDepartment());
        }
    }

    public void loadAssociatedObjects(){
        if(departmentService == null){
            throw new IllegalStateException("DepartmentService estava nulo");
        }
        List<Department> list = departmentService.findAll();
        obsList = FXCollections.observableArrayList(list);//coloca a lista de departamentos dentro do combobox
        comboBoxDepartment.setItems(obsList);
    }

    private void setErrorMessages(Map<String, String> error){
        Set<String> fields = error.keySet();
        labelErrorName.setText(fields.contains("name") ? error.get("name") : "");
        labelErrorEmail.setText(fields.contains("email") ? error.get("email") : "");
        labelErrorBaseSalary.setText(fields.contains("baseSalary") ? error.get("baseSalary") : "");
        labelErrorBirthDate.setText(fields.contains("birthDate") ? error.get("birthDate") : "");
    }

    private void initializeComboBoxDepartment() {
        Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getNome());
            }
        };
        comboBoxDepartment.setCellFactory(factory);
        comboBoxDepartment.setButtonCell(factory.call(null));
    }
}
