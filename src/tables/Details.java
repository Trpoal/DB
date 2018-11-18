package tables;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Details {

    private int idDetails;
    private String type;
    private String tittle;

    public Details(){}

    public Details(int idDetails, String type, String tittle)
    {
        this.idDetails = idDetails;
        this.type = type;
        this.tittle = tittle;
    }

    public LinkedList<TableColumn> listColumn() {
        LinkedList<TableColumn> list = new LinkedList<TableColumn>();

        TableColumn<Details, Integer> idDetails = new TableColumn("ID Детали");
        TableColumn<Details, String> type = new TableColumn("Тип");
        TableColumn<Details, String> tittle = new TableColumn("Название");

        idDetails.setCellValueFactory(new PropertyValueFactory("idDetails"));
        idDetails.setMinWidth(40);
        list.add(idDetails);

        type.setCellValueFactory(new PropertyValueFactory("type"));
        type.setMinWidth(15);
        list.add(type);

        tittle.setCellValueFactory(new PropertyValueFactory("tittle"));
        tittle.setMinWidth(10);
        list.add(tittle);

        return list;
    }

    public ObservableList takeInfo(Connection con) {
        PreparedStatement pr = null;
        ArrayList<Details> dir = new ArrayList<>();
        String prepSt = "select * from Details";
        try {
            pr = con.prepareStatement(prepSt);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                dir.add(new Details(rs.getInt(1),rs.getString(2),rs.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return FXCollections.observableArrayList(dir);
    }

    public ObservableList takeSpecialDetails(Connection con, String det)
    {

        ArrayList<String> dir = new ArrayList<>();
        String prepSt = "select iddetails, tittle from Details where type = ?";
        try {
            PreparedStatement pr = con.prepareStatement(prepSt);
            pr.setString(1,det);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                dir.add(rs.getInt(1)+" -> "+rs.getString(2));
                //System.out.println(rs.getInt(1)+rs.getString(2)+rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return FXCollections.observableArrayList(dir);

    }
    public void addRowToDataBase(Connection con, ObservableList info) {


        PreparedStatement pr = null;
        String query = "insert into Details (type, tittle) values (?,?)";

        PreparedStatement preparedStmt = null;
        try {
            preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, type);                  //Model
            preparedStmt.setString(2, tittle);

            System.out.println(preparedStmt);
            preparedStmt.execute();

            pr = con.prepareStatement("SELECT LAST_INSERT_ID()");
            ResultSet peee = pr.executeQuery();
            if(peee.next())
            {
                idDetails = peee.getInt(1);
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Ошибка с PRIMARY KEY");
            e.printStackTrace();
        } catch (
                SQLException e) {
            e.printStackTrace();

        }
        info.add(this);
    }

    public void delNote(Connection con,  int indexObs, ObservableList info) {
        String SQL = "delete from details where idDetails = ? ";
        PreparedStatement pstmt = null;

// get a connection and then in your try catch for executing your delete...

        try {
            pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, idDetails);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("ERROR");
            alert.setContentText("Нельзя удалить. Есть машины с этой деталью");
            alert.showAndWait();
        }
        info.remove(indexObs);
    }

    public void editNote(Connection con, int indexObs, ObservableList info) {
        String SQL = "update details set type = ?, tittle = ? where idDetails = ? ";
        PreparedStatement preparedStmt = null;

// get a connection and then in your try catch for executing your delete...

        try {
            preparedStmt = con.prepareStatement(SQL);
            preparedStmt.setString(1, type);
            preparedStmt.setString(2, tittle);
            preparedStmt.setInt(3, idDetails);
            preparedStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("MISTAKE");
            e.printStackTrace();
        }
        // info.get(indexObs).setId(Integer.parseInt(link.get(0).toString()));
        Details tmp = (Details) info.get(indexObs);
        tmp.setTittle(tittle);
        tmp.setType(type);

        //info.get(indexObs).setName(link.get(1).toString());
    }

    public int getIdDetails() {
        return idDetails;
    }
    public String getType() {
        return type;
    }
    public String getTittle() {
        return tittle;
    }
    public void setIdDetails(int idDetails) {
        this.idDetails = idDetails;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setTittle(String tittle) {
        this.tittle = tittle;
    }
}
