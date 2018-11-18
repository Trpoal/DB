package tables;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Car  {
    private String stateNumber;
    private String model;
    private Integer idBrand;
    private Integer idTransmission;
    private Integer idBody;
    private Integer idEngine;
    private Integer year;
    private Integer mileage;
    private Integer price;
    private String nameOffice;
    private ObservableList<Car> info;

    public Car(String stateNumber, String model, Integer idBrand, Integer idTransmission, Integer idBody, Integer idEngine, Integer year,
               Integer mileage, Integer price, String nameOffice) {
        this.stateNumber = stateNumber;
        this.model = model;
        this.idBrand = idBrand;
        this.idTransmission = idTransmission;
        this.idBody = idBody;
        this.idEngine = idEngine;
        this.year = year;
        this.mileage = mileage;
        this.price = price;
        this.nameOffice = nameOffice;
    }

    public Car() {
    }

    public LinkedList<TableColumn> listColumn() {
        LinkedList<TableColumn> list = new LinkedList<TableColumn>();

        TableColumn<Car, String> stateNumber = new TableColumn("StateNumber");
        TableColumn<Car, String> model = new TableColumn("Model");
        TableColumn<Car, Integer> idBrand = new TableColumn("IdBrand");
        TableColumn<Car, Integer> idTransmission = new TableColumn("IdTransmission");
        TableColumn<Car, Integer> idBody = new TableColumn("IdBody");
        TableColumn<Car, Integer> year = new TableColumn("Year");
        TableColumn<Car, Integer> mileage = new TableColumn("Mileage");
        TableColumn<Car, Integer> price = new TableColumn("Price");
        TableColumn<Car, Integer> nameOffice = new TableColumn("Name of Office");

        stateNumber.setCellValueFactory(new PropertyValueFactory("stateNumber"));
        stateNumber.setMinWidth(40);
        list.add(stateNumber);

        model.setCellValueFactory(new PropertyValueFactory("model"));
        model.setMinWidth(15);
        list.add(model);

        idBrand.setCellValueFactory(new PropertyValueFactory("idBrand"));
        idBrand.setMinWidth(10);
        list.add(idBrand);

        idTransmission.setCellValueFactory(new PropertyValueFactory("idTransmission"));
        idTransmission.setMinWidth(10);
        list.add(idTransmission);

        idBody.setCellValueFactory(new PropertyValueFactory("idBody"));
        idBody.setMinWidth(10);
        list.add(idBody);

        year.setCellValueFactory(new PropertyValueFactory("year"));
        year.setMinWidth(25);
        list.add(year);

        mileage.setCellValueFactory(new PropertyValueFactory("mileage"));
        mileage.setMinWidth(25);
        list.add(mileage);

        price.setCellValueFactory(new PropertyValueFactory("price"));
        price.setMinWidth(10);
        list.add(price);

        nameOffice.setCellValueFactory(new PropertyValueFactory("nameOffice"));
        nameOffice.setMinWidth(40);
        list.add(nameOffice);

        return list;
    }

    public ObservableList takeInfo(Connection con) {
        PreparedStatement pr = null;
        ArrayList<Car> dir = new ArrayList<>();
        String prepSt = "select * from car";
        try {
            pr = con.prepareStatement(prepSt);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                dir.add(new Car(rs.getString(1), rs.getString(2), rs.getInt(3),
                        rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7),
                        rs.getInt(8), rs.getInt(9), rs.getString(10)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        info = FXCollections.observableArrayList(dir);
        return info;
    }

    public void addRowToDataBase(Connection con,  ObservableList info) {

        String query = "insert into car (stateNumber, model, idBrand, idTransmission," +
                " idBody, idEngine, year, mileage, price, nameOffice ) values (?,?,?,?,?,?,?,?,?,?)";
        System.out.println(query);

        PreparedStatement preparedStmt = null;
        try {
            preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, stateNumber);
            preparedStmt.setString(2, model);                  //Model
            preparedStmt.setInt(3, idBrand);   //Brand
            preparedStmt.setInt(4, idTransmission);
            preparedStmt.setInt(5, idBody);
            preparedStmt.setInt(6, idEngine);
            preparedStmt.setInt(7, year);
            preparedStmt.setInt(8, price);
            preparedStmt.setInt(9, mileage);
            preparedStmt.setString(10, nameOffice);

            System.out.println(preparedStmt);
            preparedStmt.execute();
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Ошибка с PRIMARY KEY");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("ERROR");
            alert.setContentText("Дубликат Гос.номера!!!!");
            alert.showAndWait();
            e.printStackTrace();
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
        info.add(this);
    }

    public void delNote(Connection con,  int indexObs, ObservableList info) {
        String SQL = "delete from car where stateNumber = ? ";
        PreparedStatement pstmt = null;

// get a connection and then in your try catch for executing your delete...

        try {
            pstmt = con.prepareStatement(SQL);
           pstmt.setString(1, stateNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("ERROR");
            alert.setContentText("Нельзя удалить. Учавствует в продаже!");
            alert.showAndWait();
        }
        info.remove(indexObs);
    }

    public void editNote(Connection con,  String oldstateNumber, int indexObs, ObservableList info) {
        String SQL = "update car set stateNumber=?, model = ?, idBrand = ?, idTransmission = ?, idBody = ?," +
                "idEngine = ?, year = ?, mileage = ?, price = ?, nameOffice = ? where stateNumber = ? ";
        PreparedStatement preparedStmt = null;

        try {
            preparedStmt = con.prepareStatement(SQL);
            preparedStmt.setString(1, stateNumber);                  //StateNumber
            preparedStmt.setString(2, model);                  //Model
            preparedStmt.setInt(3, idBrand);   //Brand
            preparedStmt.setInt(4, idTransmission);
            preparedStmt.setInt(5, idBody);
            preparedStmt.setInt(6, idEngine);
            preparedStmt.setInt(7, year);
            preparedStmt.setInt(8, mileage);
            preparedStmt.setInt(9, price);
            preparedStmt.setString(10, nameOffice);
            preparedStmt.setString(11, oldstateNumber);
            preparedStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("MISTAKE");
            e.printStackTrace();
        }
        Car tmp = (Car) info.get(indexObs);
        tmp.setStateNumber(stateNumber);
        tmp.setModel(model);
        tmp.setIdBrand(idBrand);
        tmp.setIdTransmission(idTransmission);
        tmp.setIdBody(idBody);
        tmp.setIdEngine(idEngine);
        tmp.setYear(year);
        tmp.setMileage(mileage);
        tmp.setPrice(price);
        tmp.setNameOffice(nameOffice);


        //info.get(indexObs).setName(link.get(1).toString());*/
    }

    public ObservableList takeStateNumber(Connection con) {
        ArrayList<String> dir = new ArrayList<>();
        String prepSt = "select car.statenumber, sales.statenumber from car left join sales using(statenumber) where sales.statenumber is null";
        try {
            PreparedStatement pr = con.prepareStatement(prepSt);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                dir.add(rs.getString(1));
                //System.out.println(rs.getInt(1)+rs.getString(2)+rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return FXCollections.observableArrayList(dir);
    }

    public String getStateNumber() {
        return stateNumber;
    }

    public void setStateNumber(String stateNumber) {
        this.stateNumber = stateNumber;
    }

    public Integer getIdTransmission() {
        return idTransmission;
    }

    public void setIdTransmission(Integer idTransmission) {
        this.idTransmission = idTransmission;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getIdBrand() {
        return idBrand;
    }

    public void setIdBrand(Integer idBrand) {
        this.idBrand = idBrand;
    }

    public Integer getIdBody() {
        return idBody;
    }

    public void setIdBody(Integer idBody) {
        this.idBody = idBody;
    }

    public Integer getIdEngine() {
        return idEngine;
    }

    public void setIdEngine(Integer idEngine) {
        this.idEngine = idEngine;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getNameOffice() {
        return nameOffice;
    }

    public void setNameOffice(String nameOffice) {
        this.nameOffice = nameOffice;
    }



}
