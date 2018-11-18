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
import tables.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;

public class CustomerForm {
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
    private ObservableList infoCustomer;
    private Customer curCustomer;
    private int curIndexCustomer;

    @FXML
    private void initialize() {
        con = Main.con;
        editBut.setVisible(false);
        delBut.setVisible(false);
        dateOfBirthDate.setValue(LocalDate.now());
}

    public void delItem(ActionEvent actionEvent) {
        curCustomer.delNote(con, curIndexCustomer, infoCustomer);
        cancel(actionEvent);
    }

    public void editItem(ActionEvent actionEvent) {
        Customer tmp = new Customer(curCustomer.getIdCustomer() , surnameField.getText(), nameField.getText(), patronymicField.getText(),
                Date.valueOf(dateOfBirthDate.getValue()), Integer.parseInt(passportField.getText()), addressField.getText(),
                Long.parseLong(phoneField.getText()), emailField.getText());
        tmp.editNote(con,curIndexCustomer,infoCustomer);
        cancel(actionEvent);
    }

    public void cancel(ActionEvent actionEvent) {

        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    public void save(ActionEvent actionEvent) {
        Customer tmp = new Customer(0, surnameField.getText(), nameField.getText(), patronymicField.getText(),
                Date.valueOf(dateOfBirthDate.getValue()), Integer.parseInt(passportField.getText()), addressField.getText(),
                Long.parseLong(phoneField.getText()), emailField.getText());
        tmp.addRowToDataBase(con, infoCustomer);
        cancel(actionEvent);
    }

    public void initData(ObservableList infoCustomer, Customer customer, int curIndexCustomer, boolean mode) {
        this.infoCustomer = infoCustomer;
        this.curCustomer =customer;
        this.curIndexCustomer = curIndexCustomer;
        if (mode) {
            delBut.setVisible(true);
            editBut.setVisible(true);
            saveBut.setVisible(false);
            emailField.setText(customer.getEmail());
            phoneField.setText(String.valueOf(customer.getPhone()));
            nameField.setText(customer.getName());
            surnameField.setText(customer.getSurname());
            patronymicField.setText(customer.getPatronymic());
            addressField.setText(customer.getAddress());
            passportField.setText(String.valueOf(customer.getPassport()));
            dateOfBirthDate.setValue(customer.getDateOfBirth().toLocalDate());
        }
    }
}
