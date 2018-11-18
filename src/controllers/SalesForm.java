package controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import sample.Main;
import tables.*;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;

public class SalesForm {
    public Button saveBut;
    public Button cancelBut;
    public TextField priceField;
    public Button editBut;
    public Button delBut;
    public DatePicker dateField;
    public TextField transactionNumberField;
    public CheckBox creditBox;
    public TextField discountField;
    public ChoiceBox<String> idCustomerChoice;
    public ChoiceBox<String> idWorkerChoice;
    public ChoiceBox<String> stateNumberChoice;
    private Connection con;
    private int idCustomerCh;
    private int idWorkerCh;
    private String stateNumberCh;
    private ObservableList infoSales;
    private Sales curSales;
    private int curIndexSales;

    @FXML
    private void initialize() {
        con = Main.con;

        idCustomerCh = -1;
        idWorkerCh = -1;

        dateField.setValue(LocalDate.now());

        stateNumberChoice.setItems(new Car().takeStateNumber(con));
        stateNumberChoice.setVisible(true);
        stateNumberChoice.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) ->
        {
            if(newValue!=null)
            {
                stateNumberCh = newValue;
            }
        });

        idCustomerChoice.setItems(new Customer().takeFIO(con));
        idCustomerChoice.setVisible(true);
        idCustomerChoice.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) ->
        {
            if(newValue!=null)
            {
                idCustomerCh = takeIdFrString( newValue);
            }
        });
        idWorkerChoice.setItems(new Worker().takeFIO(con));
        idWorkerChoice.setVisible(true);
        idWorkerChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            if(newValue!=null)
            {
                idWorkerCh = takeIdFrString(newValue);
            }
        });

        editBut.setVisible(false);
        delBut.setVisible(false);
    }

    public void save(ActionEvent actionEvent) {
        if(idCustomerCh !=-1 && idWorkerCh!=-1&& stateNumberCh!=null){
            Sales sale = new Sales(0, stateNumberCh,Long.parseLong(priceField.getText()), Date.valueOf(dateField.getValue()),
                    Long.parseLong(transactionNumberField.getText()), creditBox.isSelected(),Integer.parseInt(discountField.getText())
                    , idWorkerCh, idCustomerCh);
            sale.addRowToDataBase(con, infoSales);
            cancel(actionEvent);
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("ERROR");
            alert.setContentText("Заполните поля Гос.номер, Работник, Покупатель");
            alert.showAndWait();
        }
    }

    public void cancel(ActionEvent actionEvent) {
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    public void editItem(ActionEvent actionEvent) {
        Sales sale = new Sales(curSales.getIdSales(), stateNumberCh,Long.parseLong(priceField.getText()), Date.valueOf(dateField.getValue()),
                Long.parseLong(transactionNumberField.getText()), creditBox.isSelected(),Integer.parseInt(discountField.getText())
                , idWorkerCh, idCustomerCh);
        //Details det = new Details(curCar.getIdDetails(), type, titleField.getText());
        sale.editNote(con,curIndexSales,infoSales);
        cancel(actionEvent);
    }

    public void delItem(ActionEvent actionEvent) {
        curSales.delNote(con, curIndexSales, infoSales);
        cancel(actionEvent);
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

    public void initData(ObservableList infoSales, Sales curSales, int curIndexSales, boolean mode) {
        this.infoSales = infoSales;
        this.curSales = curSales;
        this.curIndexSales = curIndexSales;
        if(mode)
        {
            stateNumberChoice.getItems().add(curSales.getStateNumber());
            stateNumberChoice.setValue(curSales.getStateNumber());
            priceField.setText(String.valueOf(curSales.getPrice()));
            dateField.setValue(curSales.getDate().toLocalDate());
            transactionNumberField.setText(String.valueOf(curSales.getTransactionNumber()));
            creditBox.setSelected(curSales.isCredit());
            discountField.setText(String.valueOf(curSales.getDiscount()));
            idCustomerChoice.setValue(takeStringFrInt(curSales.getIdCustomer(),idCustomerChoice.getItems()));
            idWorkerChoice.setValue(takeStringFrInt(curSales.getIdWorker(),idWorkerChoice.getItems()));
            delBut.setVisible(true);
            editBut.setVisible(true);
            saveBut.setVisible(false);
        }
    }
}
