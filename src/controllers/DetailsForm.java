package controllers;

import com.mysql.cj.xdevapi.Table;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import sample.Main;
import tables.Details;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DetailsForm {


    public ChoiceBox typeChoice;
    public TextField titleField;
    public Button delBut;
    public Button editBut;
    public Button cancelBut;
    public Button saveBut;

    String type;
    String title;
    private Connection con;
    private ObservableList infoDetails;
    private Details curDetails;
    private int curIndexDetails;

    @FXML
    private void initialize() {
        con = Main.con;
        ChangeListener<String> listener = (observable, oldValue, newValue) ->
        {
            if (newValue != null) {
                type = newValue;
            }
        };

        typeChoice.setItems(FXCollections.observableArrayList("Двигатель", "Коробка передач", "Кузов", "Марка"));
        typeChoice.setVisible(true);
        typeChoice.getSelectionModel()
                .selectedItemProperty()
                .addListener(listener);
        editBut.setVisible(false);
        delBut.setVisible(false);

    }

    public void cancel(ActionEvent actionEvent) {
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    public void save(ActionEvent actionEvent) {
        Details det = new Details(0,type,titleField.getText());
        det.addRowToDataBase(con, infoDetails);
        cancel(actionEvent);
    }

    public void delItem(ActionEvent actionEvent) {
        curDetails.delNote(con, curIndexDetails, infoDetails);
        cancel(actionEvent);
    }

    public void editItem(ActionEvent actionEvent) {

        Details det = new Details(curDetails.getIdDetails(), type, titleField.getText());
        det.editNote(con,curIndexDetails,infoDetails);
        cancel(actionEvent);
    }

    public void initData(ObservableList infoDetails, Details curDetails, int curIndexDetails, boolean mode) {
        this.infoDetails = infoDetails;
        this.curDetails = curDetails;
        this.curIndexDetails = curIndexDetails;
        if(mode)
        {
            delBut.setVisible(true);
            editBut.setVisible(true);
            saveBut.setVisible(false);
            typeChoice.setValue(curDetails.getType());
            titleField.setText(curDetails.getTittle());
            this.type = curDetails.getType();
        }
    }
}
