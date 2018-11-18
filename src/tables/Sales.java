package tables;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.LongSummaryStatistics;

public class Sales {
    private int idSales;
    private String stateNumber;
    private long price;
    private Date date;
    private long transactionNumber;
    private boolean credit;
    private int discount;
    private int idWorker;
    private int idCustomer;

    private ObservableList info;

    public Sales(){}

    public Sales(int idSales, String stateNumber, long price, Date date, long transactionNumber, boolean credit,
                 int discount, int idWorker, int idCustomer)
    {
        this.idSales = idSales;
        this.stateNumber = stateNumber;
        this.price = price;
        this.date=date;
        this.transactionNumber=transactionNumber;
        this.credit=credit;
        this.discount=discount;
        this.idWorker=idWorker;
        this.idCustomer=idCustomer;
    }

    public LinkedList<TableColumn> listColumn() {
        LinkedList<TableColumn> list = new LinkedList<TableColumn>();

        TableColumn<Sales, Integer> idSales = new TableColumn("idSales");
        TableColumn<Sales, String> stateNumber = new TableColumn("StateNumber");
        TableColumn<Sales, Long> price = new TableColumn("Price");
        TableColumn<Sales, Date> date = new TableColumn("Date");
        TableColumn<Sales, Long> transactionNumber = new TableColumn("TransactionNumber");
        TableColumn<Sales, Boolean> credit = new TableColumn("Credit");
        TableColumn<Sales, Integer> discount = new TableColumn("Discount");
        TableColumn<Sales, Integer> idWorker = new TableColumn("Worker");
        TableColumn<Sales, Integer> idCustomer = new TableColumn("Customer");

        idSales.setCellValueFactory(new PropertyValueFactory("idSales"));
        idSales.setMinWidth(40);
        list.add(idSales);

        stateNumber.setCellValueFactory(new PropertyValueFactory("stateNumber"));
        stateNumber.setMinWidth(15);
        list.add(stateNumber);

        price.setCellValueFactory(new PropertyValueFactory("price"));
        price.setMinWidth(10);
        list.add(price);

        date.setCellValueFactory(new PropertyValueFactory("date"));
        date.setMinWidth(10);
        list.add(date);

        transactionNumber.setCellValueFactory(new PropertyValueFactory("transactionNumber"));
        transactionNumber.setMinWidth(10);
        list.add(transactionNumber);

        credit.setCellValueFactory(new PropertyValueFactory("credit"));
        credit.setMinWidth(25);
        list.add(credit);

        discount.setCellValueFactory(new PropertyValueFactory("discount"));
        discount.setMinWidth(25);
        list.add(discount);

        idWorker.setCellValueFactory(new PropertyValueFactory("idWorker"));
        idWorker.setMinWidth(40);
        list.add(idWorker);

        idCustomer.setCellValueFactory(new PropertyValueFactory("idCustomer"));
        idCustomer.setMinWidth(40);
        list.add(idCustomer);

        return list;
    }

    public ObservableList takeInfo(Connection con) {
        PreparedStatement pr = null;
        ArrayList<Sales> dir = new ArrayList<>();
        String prepSt = "select * from sales";
        try {
            pr = con.prepareStatement(prepSt);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                dir.add(new Sales(rs.getInt(1), rs.getString(2), rs.getLong(3),
                        rs.getDate(4), rs.getLong(5), rs.getBoolean(6),
                        rs.getInt(7),rs.getInt(8), rs.getInt(9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        info = FXCollections.observableArrayList(dir);
        return info;
    }

    public void addRowToDataBase(Connection con,  ObservableList info) {

        String query = "insert into Sales (stateNumber, price, date, transactionNumber," +
                " credit, discount,  idWorker, idCustomer) values (?,?,?,?,?,?,?,?)";
        System.out.println(query);

        PreparedStatement preparedStmt = null;
        try {
            preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, stateNumber);
            preparedStmt.setLong(2, price);                  //Model
            preparedStmt.setDate(3, date);   //Brand
            preparedStmt.setLong(4, transactionNumber);
            preparedStmt.setBoolean(5, credit);
            preparedStmt.setInt(6, discount);
            preparedStmt.setInt(7, idWorker);
            preparedStmt.setInt(8, idCustomer);

            System.out.println(preparedStmt);
            preparedStmt.execute();
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
        String SQL = "delete from sales where idSales = ? ";
        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, idSales);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("MISTAKE");
            e.printStackTrace();
        }
        info.remove(indexObs);
    }

    public void editNote(Connection con, int indexObs, ObservableList info) {
        String SQL = "update sales set stateNumber=?, price = ?, date = ?, transactionNumber = ?, credit= ?," +
                "discount = ?, idWorker = ?, idCustomer = ? where idSales = ? ";
        PreparedStatement preparedStmt = null;

        try {
            preparedStmt = con.prepareStatement(SQL);
            preparedStmt.setString(1, stateNumber);
            preparedStmt.setLong(2, price);                  //Model
            preparedStmt.setDate(3, date);   //Brand
            preparedStmt.setLong(4, transactionNumber);
            preparedStmt.setBoolean(5, credit);
            preparedStmt.setInt(6, discount);
            preparedStmt.setInt(7, idWorker);
            preparedStmt.setInt(8, idCustomer);
            preparedStmt.setInt(9, idSales);
            preparedStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("MISTAKE");
            e.printStackTrace();
        }
        Sales tmp = (Sales) info.get(indexObs);
        tmp.setStateNumber(stateNumber);
        tmp.setPrice(price);
        tmp.setDate(date);
        tmp.setTransactionNumber(transactionNumber);
        tmp.setCredit(credit);
        tmp.setDiscount(discount);
        tmp.setIdWorker(idWorker);
        tmp.setIdCustomer(idCustomer);
        tmp.setIdSales(idSales);
    }
/*
    public void join(Connection con, String nameOfTable) {
        TableView tablJoin = new TableView();
        String query = "select stateNumber, brand.brand, body.body, transmission.transmission, engine.engine" +
                " from car  inner Join Brand using (idBrand) inner join Body using (idBody) inner join Transmission" +
                " using (idTransmission) inner join Engine using (IdEngine)";
        PreparedStatement preparedStmt = null;
        try {
            preparedStmt = con.prepareStatement(query);
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

    public boolean checkNull(String value) {
        if (value.compareTo("") == 0) {
            return false;
        } else {
            return true;
        }
    }*/

    public int getIdSales() {
        return idSales;
    }

    public void setIdSales(int idSales) {
        this.idSales = idSales;
    }

    public String getStateNumber() {
        return stateNumber;
    }

    public void setStateNumber(String stateNumber) {
        this.stateNumber = stateNumber;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(long transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public boolean isCredit() {
        return credit;
    }

    public void setCredit(boolean credit) {
        this.credit = credit;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getIdWorker() {
        return idWorker;
    }

    public void setIdWorker(int idWorker) {
        this.idWorker = idWorker;
    }

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }
}
