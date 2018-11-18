package controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import sample.Main;
import sample.MyTextField;
import tables.Car;
import tables.Worker;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;

public class WorkerForm {
    public DatePicker dateOfBirthDate;
    public Button delBut;
    public Button editBut;
    public TextField emailField;
    public MyTextField phoneField;
    public TextField nameField;
    public TextField surnameField;
    public TextField patronymicField;
    public TextField addressField;
    public MyTextField passportField;
    public Button cancelBut;
    public Button saveBut;
    private Connection con;
    private ObservableList infoWorker;
    private Worker curWorker;
    private int curIndexWorker;

    @FXML
    private void initialize() {
        con = Main.con;
        editBut.setVisible(false);
        delBut.setVisible(false);
        dateOfBirthDate.setValue(LocalDate.now());
    }

    public void delItem(ActionEvent actionEvent) {
        curWorker.delNote(con, curIndexWorker, infoWorker);
        cancel(actionEvent);
    }

    public void editItem(ActionEvent actionEvent) {
        Worker tmp = new Worker(curWorker.getIdWorker() , surnameField.getText(), nameField.getText(), patronymicField.getText(),
                Date.valueOf(dateOfBirthDate.getValue()), Integer.parseInt(passportField.getText()), addressField.getText(),
                Long.parseLong(phoneField.getText()), emailField.getText());
        tmp.editNote(con,curIndexWorker,infoWorker);
        cancel(actionEvent);
    }

    public void cancel(ActionEvent actionEvent) {

        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    public void save(ActionEvent actionEvent) {
        Worker tmp = new Worker(0, surnameField.getText(), nameField.getText(), patronymicField.getText(),
                Date.valueOf(dateOfBirthDate.getValue()), Integer.parseInt(passportField.getText()), addressField.getText(),
                Integer.parseInt(phoneField.getText()), emailField.getText());
        tmp.addRowToDataBase(con, infoWorker);
        cancel(actionEvent);
    }

    public void initData(ObservableList infoWorker, Worker worker, int curIndexWorker, boolean mode) {
        this.infoWorker = infoWorker;
        this.curWorker = worker;
        this.curIndexWorker = curIndexWorker;
        if (mode) {
            delBut.setVisible(true);
            editBut.setVisible(true);
            saveBut.setVisible(false);
            emailField.setText(worker.getEmail());
            phoneField.setText(String.valueOf(worker.getPhone()));
            nameField.setText(worker.getName());
            surnameField.setText(worker.getSurname());
            patronymicField.setText(worker.getPatronymic());
            addressField.setText(worker.getAddress());
            passportField.setText(String.valueOf(worker.getPassport()));
            dateOfBirthDate.setValue(worker.getDateOfBirth().toLocalDate());
        }
    }
}
