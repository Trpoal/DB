package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Main;
import sample.MyTextField;
import tables.*;

import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;

public class Tables {


    public Button addDetailsBut;
    public TableView salesTable;
    public TableView customerTable;
    public TableView carTable;
    public TableView detailsTable;
    public TableView workerTable;
    public MyTextField yearField;
    public Button joinBut;

    ObservableList infoDetails;
    ObservableList infoWorker;

    Details curDetails;
    int curIndexDetails;
    private ObservableList infoCar;
    private Car curCar;
    private int curIndexCar;
    private int curIndexWorker;
    private Worker curWorker;
    private ObservableList infoCustomer;
    private int curIndexCustomer;
    private Customer curCustomer;
    private ObservableList infoSales;
    private int curIndexSales;
    private Sales curSales;
    private boolean carMode;
    private boolean customerMode;
    private boolean workerMode;
    private boolean salesMode;
    private boolean detailsMode;

    public static boolean flag;
    Connection con;

    @FXML
    private void initialize() {
        con = Main.con;
        Car c = new Car();
        carTable.getColumns().addAll(c.listColumn());
        infoCar = c.takeInfo(con);
        carTable.setItems(infoCar);
        detailsTable.getColumns().addAll(new Details().listColumn());
        infoDetails = new Details().takeInfo(con);
        detailsTable.setItems(infoDetails);

        workerTable.getColumns().addAll(new Worker().listColumn());
        infoWorker = new Worker().takeInfo(con);
        workerTable.setItems(infoWorker);

        customerTable.getColumns().addAll(new Customer().listColumn());
        infoCustomer = new Customer().takeInfo(con);
        customerTable.setItems(infoCustomer);

        salesTable.getColumns().addAll(new Sales().listColumn());
        infoSales = new Sales().takeInfo(con);
        salesTable.setItems(infoSales);

        carMode = false;
        customerMode = false;
        workerMode = false;
        salesMode = false;
        detailsMode = false;
    }

    public void clickCar(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            curIndexCar = carTable.getSelectionModel().getFocusedIndex();
            curCar = (Car) infoCar.get(curIndexCar);
            carMode = true;
            addCar(new ActionEvent());
        }
    }

    public void clickDetails(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            curIndexDetails = detailsTable.getSelectionModel().getFocusedIndex();
            curDetails = (Details) infoDetails.get(curIndexDetails);
            detailsMode = true;
            addDetails(new ActionEvent());
        }
    }

    public void clickWorker(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            curIndexWorker = workerTable.getSelectionModel().getFocusedIndex();
            curWorker = (Worker) infoWorker.get(curIndexWorker);
            workerMode = true;
            addWorker(new ActionEvent());
        }
    }

    public void clickCustomer(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            curIndexCustomer = customerTable.getSelectionModel().getFocusedIndex();
            curCustomer = (Customer) infoCustomer.get(curIndexCustomer);
            customerMode = true;
            addCustomer(new ActionEvent());
        }
    }

    public void clickSales(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            curIndexSales = salesTable.getSelectionModel().getFocusedIndex();
            curSales = (Sales) infoSales.get(curIndexSales);
            salesMode = true;
            addSales(new ActionEvent());
        }
    }

    public void addCar(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "../fxml/carForm.fxml"
                    )
            );

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(
                    new Scene(
                            (Pane) loader.load()
                    )
            );

            CarForm controller = loader.getController();
            controller.initData(infoCar, curCar, curIndexCar, carMode);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        carMode=false;
        carTable.refresh();
    }

    public void addDetails(ActionEvent actionEvent) {
        Stage st = new Stage();
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "../fxml/detailsForm.fxml"
                    )
            );

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(
                    new Scene(
                            (Pane) loader.load()
                    )
            );

            DetailsForm controller = loader.<DetailsForm>getController();
            controller.initData(infoDetails, curDetails, curIndexDetails, detailsMode);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        detailsMode = false;
        detailsTable.refresh();
    }

    public void addWorker(ActionEvent actionEvent) {
        Stage st = new Stage();
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "../fxml/workerForm.fxml"
                    )
            );

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(
                    new Scene(
                            (Pane) loader.load()
                    )
            );

            WorkerForm controller = loader.getController();
            controller.initData(infoWorker, curWorker, curIndexCustomer, workerMode);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        workerMode = false;
        workerTable.refresh();
    }

    public void addCustomer(ActionEvent actionEvent) {
        Stage st = new Stage();
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "../fxml/customerForm.fxml"
                    )
            );

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(
                    new Scene(
                            (Pane) loader.load()
                    )
            );

            CustomerForm controller = loader.getController();
            controller.initData(infoCustomer, curCustomer, curIndexCustomer, customerMode);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        customerMode = false;
        customerTable.refresh();
    }

    public void addSales(ActionEvent actionEvent) {
        Stage st = new Stage();
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "../fxml/salesForm.fxml"
                    )
            );

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(
                    new Scene(
                            (Pane) loader.load()
                    )
            );

            SalesForm controller = loader.getController();
            controller.initData(infoSales, curSales, curIndexSales, salesMode);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        salesMode = false;
        salesTable.refresh();
    }

    public void join(ActionEvent event) {
        TableView tablJoin = new TableView();
        int year;
        if(yearField.getText().compareTo("")!=0) {
            year = Integer.parseInt(yearField.getText());
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("ERROR");
            alert.setContentText("Введите год");
            alert.showAndWait();
            return;
        }
        String SQL = "select sales.statenumber,car.year, customer.surname, customer.name from sales " +
                "inner join car on sales.statenumber = car.statenumber " +
                "inner join customer on sales.idcustomer = customer.idcustomer where year = "+year;

        PreparedStatement preparedStmt = null;
        try {
            preparedStmt = con.prepareStatement(SQL);
            ResultSet rs = preparedStmt.executeQuery();
            ResultSetMetaData re = rs.getMetaData();

            //while(rs.next())
            // {
            for (int i = 1; i <= re.getColumnCount(); i++) {
                int finalI = i - 1;
                TableColumn<ObservableList<String>, String> col = new TableColumn<>(re.getColumnName(i));
                col.setCellValueFactory(
                        param -> new SimpleStringProperty(param.getValue().get(finalI))
                );

                tablJoin.getColumns().add(col);
            }
            while (rs.next()) {
                LinkedList<String> row = new LinkedList<>();
                ObservableList<String> a = FXCollections.observableArrayList();
                for (int i = 1; i <= re.getColumnCount(); i++) {
                    row.add(rs.getString(i));

                }
                a.addAll(row);
                tablJoin.getItems().add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        StackPane root = new StackPane();
        root.getChildren().add(tablJoin);

        Stage stage = new Stage();
        stage.setTitle("Join ");

        Scene scene = new Scene(root, 450, 300);
        stage.setScene(scene);
        stage.show();
    }

    public void refreshTable(ActionEvent actionEvent) {
        carTable.refresh();
        detailsTable.refresh();
        workerTable.refresh();
        salesTable.refresh();
    }

}
