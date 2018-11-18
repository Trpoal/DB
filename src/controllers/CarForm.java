package controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import sample.Main;
import sample.MyTextField;
import tables.Car;
import tables.Details;

import java.sql.Connection;

public class CarForm {
    public Button saveBut;
    public Button cancelBut;
    public TextField stateNumberField;
    public TextField modelField;
    public ChoiceBox<String> brandChoice;
    public ChoiceBox<String> transmissionChoice;
    public ChoiceBox<String> bodyChoice;
    public ChoiceBox<String> engineChoice;
    public MyTextField yearField;
    public MyTextField mileageField;
    public MyTextField priceField;
    public Button editBut;
    public Button delBut;
    Connection con;
    private ObservableList infoCar;
    private Car curCar;
    private int curIndexCar;

    private int brandChoiceInt;
    int transChoiceInt;
    int bodyChoiceInt;
    int engineChoiceInt;

    @FXML
    private void initialize() {
        con = Main.con;
        brandChoiceInt = -1;
        transChoiceInt= -1;
        bodyChoiceInt = -1;
        engineChoiceInt = -1;

        brandChoice.setItems(new Details().takeSpecialDetails(con,"Марка"));
        brandChoice.setVisible(true);
        brandChoice.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) ->
        {
            if(newValue!=null)
            {
                brandChoiceInt = takeIdFrString(newValue);
            }
        });
        transmissionChoice.setItems(new Details().takeSpecialDetails(con,"Коробка передач"));
        transmissionChoice.setVisible(true);
        transmissionChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            if(newValue!=null)
            {
                transChoiceInt = takeIdFrString(newValue);
            }
        });

        bodyChoice.setItems(new Details().takeSpecialDetails(con,"Кузов"));
        bodyChoice.setVisible(true);
        bodyChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            if(newValue!=null)
            {
                bodyChoiceInt = takeIdFrString(newValue);
            }
        });

        engineChoice.setItems(new Details().takeSpecialDetails(con,"Двигатель"));
        engineChoice.setVisible(true);
        engineChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            if(newValue!=null)
            {
               engineChoiceInt = takeIdFrString(newValue);
            }
        });
        editBut.setVisible(false);
        delBut.setVisible(false);
    }

    public void cancel(ActionEvent actionEvent) {
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    private int takeIdFrString(String newValue)
    {
        String idStr ="";
        for(int i=0;i<newValue.length();i++)
        {
            if(newValue.charAt(i) ==' ')
            {
                break;
            }
            else {
                idStr+=newValue.charAt(i);
            }
        }

        return Integer.parseInt(idStr);
    }

    private String takeStringFrInt(int newValue, ObservableList<String> boxList)
    {
        for(String i:boxList)
        {
            int count = takeIdFrString(i);
            if(count == newValue)
                return i;
        }
        return null;
    }

    public void editItem(ActionEvent actionEvent) {
        Car car = new Car(stateNumberField.getText(),modelField.getText(),brandChoiceInt, transChoiceInt, bodyChoiceInt,
                engineChoiceInt, Integer.parseInt(yearField.getText()),Integer.parseInt(priceField.getText()),Integer.parseInt(mileageField.getText()), null);
        //Details det = new Details(curCar.getIdDetails(), type, titleField.getText());
        car.editNote(con,curCar.getStateNumber(),curIndexCar,infoCar);
        cancel(actionEvent);
    }

    public void delItem(ActionEvent actionEvent) {
        curCar.delNote(con, curIndexCar, infoCar);
        cancel(actionEvent);
    }

    public void initData(ObservableList infoCar, Car curCar, int curIndexCar, boolean carMode) {
        this.infoCar = infoCar;
        this.curCar = curCar;
        this.curIndexCar = curIndexCar;
        if(carMode)
        {
            delBut.setVisible(true);
            editBut.setVisible(true);
            saveBut.setVisible(false);
            stateNumberField.setText(curCar.getStateNumber());
            modelField.setText(curCar.getModel());
            brandChoice.setValue(takeStringFrInt(curCar.getIdBrand(),brandChoice.getItems()));
            brandChoiceInt = curCar.getIdBrand();
            transmissionChoice.setValue(takeStringFrInt(curCar.getIdTransmission(),transmissionChoice.getItems()));
            transChoiceInt = curCar.getIdTransmission();
            bodyChoice.setValue(takeStringFrInt(curCar.getIdBody(),bodyChoice.getItems()));
            bodyChoiceInt = curCar.getIdBody();
            engineChoice.setValue(takeStringFrInt(curCar.getIdEngine(),engineChoice.getItems()));
            engineChoiceInt = curCar.getIdEngine();
            priceField.setText(String.valueOf(curCar.getPrice()));
            mileageField.setText(String.valueOf(curCar.getMileage()));
            yearField.setText(String.valueOf(curCar.getYear()));
        }
    }

    public void save(ActionEvent actionEvent) {
        int year = takeInt(yearField.getText());
        int mil = takeInt(mileageField.getText());
        int price = takeInt(priceField.getText());

        if(brandChoiceInt !=-1 && transChoiceInt!=-1&& bodyChoiceInt!=-1 && engineChoiceInt!=-1){
            Car car = new Car(stateNumberField.getText(), modelField.getText(), brandChoiceInt, transChoiceInt, bodyChoiceInt,
                    engineChoiceInt, year, price, mil, null);
            car.addRowToDataBase(con, infoCar);
            cancel(actionEvent);
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("ERROR");
            alert.setContentText("Заполните поля Двигатель, Кузов, Коробка передач, Марка");
            alert.showAndWait();
        }
    }

    public int takeInt(String txt)
    {
        int t = 0;
        if(txt.compareTo("")!=0)
        {
            t = Integer.parseInt(txt);
        }
        return t;
    }


}
