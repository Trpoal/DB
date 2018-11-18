package tables;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Worker  {

    private int idWorker;
    private String surname;
    private String name;
    private String patronymic;
    private Date dateOfBirth;
    private long passport;
    private String address;
    private long phone;
    private String email;

    private ObservableList<Worker> info;

    public Worker(){}

    public Worker(int id, String surname, String name, String patronymic, Date dateOfBirth, long passport, String address, long phone, String email)
    {
        this.idWorker = id;
        this.name = name;
        this.surname=surname;
        this.dateOfBirth = dateOfBirth;
        this.passport = passport;
        this.address = address;
        this.phone=phone;
        this.email=email;
        this.patronymic=patronymic;
    }

    public LinkedList<TableColumn> listColumn() {
        LinkedList<TableColumn> list = new LinkedList<TableColumn>();

        TableColumn<Car, String> idWorker = new TableColumn("ID");
        TableColumn<Car, String> surname = new TableColumn("Фамилия");
        TableColumn<Car, String> name = new TableColumn("Имя");
        TableColumn<Car, String> patronymic = new TableColumn("Отчество");
        TableColumn<Car, Date> dateOfBirth = new TableColumn("Дата рождения");
        TableColumn<Car, Integer> passport = new TableColumn("Паспорт");
        TableColumn<Car, String> address = new TableColumn("Адрес");
        TableColumn<Car, Integer> phone = new TableColumn("Телефон");
        TableColumn<Car, String> email = new TableColumn("Email");

        idWorker.setCellValueFactory(new PropertyValueFactory("idWorker"));
        idWorker.setMinWidth(40);
        list.add(idWorker);

        surname.setCellValueFactory(new PropertyValueFactory("surname"));
        surname.setMinWidth(15);
        list.add(surname);

        name.setCellValueFactory(new PropertyValueFactory("name"));
        name.setMinWidth(10);
        list.add(name);

        patronymic.setCellValueFactory(new PropertyValueFactory("patronymic"));
        patronymic.setMinWidth(10);
        list.add(patronymic);

        dateOfBirth.setCellValueFactory(new PropertyValueFactory("dateOfBirth"));
        dateOfBirth.setMinWidth(10);
        list.add(dateOfBirth);

        passport.setCellValueFactory(new PropertyValueFactory("passport"));
        passport.setMinWidth(25);
        list.add(passport);

        address.setCellValueFactory(new PropertyValueFactory("address"));
        address.setMinWidth(25);
        list.add(address);

        phone.setCellValueFactory(new PropertyValueFactory("phone"));
        phone.setMinWidth(10);
        list.add(phone);

        email.setCellValueFactory(new PropertyValueFactory("email"));
        email.setMinWidth(40);
        list.add(email);

        return list;
    }

    public ObservableList takeInfo(Connection con) {
        PreparedStatement pr = null;
        ArrayList<Worker> dir = new ArrayList<>();
        String prepSt = "select * from worker";
        try {
            pr = con.prepareStatement(prepSt);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                dir.add(new Worker(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getDate(5), rs.getLong(6), rs.getString(7),
                        rs.getLong(8), rs.getString(9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        info = FXCollections.observableArrayList(dir);
        return info;
    }

    public void addRowToDataBase(Connection con,  ObservableList info) {

        String query = "insert into worker (surname, name, patronymic," +
                " dateOfBirth, passport, address, phone,email) values (?,?,?,?,?,?,?,?)";

        PreparedStatement preparedStmt = null;
        try {
            preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, surname);
            preparedStmt.setString(2, name);                  //Model
            preparedStmt.setString(3, patronymic);   //Brand
            preparedStmt.setDate(4, dateOfBirth);
            preparedStmt.setLong(5, passport);
            preparedStmt.setString(6, address);
            preparedStmt.setLong(7, phone);
            preparedStmt.setString(8, email);
            System.out.println(preparedStmt);
            preparedStmt.execute();
            preparedStmt = con.prepareStatement("SELECT LAST_INSERT_ID()");
            ResultSet peee = preparedStmt.executeQuery();
            if(peee.next())
            {
                idWorker = peee.getInt(1);
            }
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
        String SQL = "delete from worker where idworker = ? ";
        PreparedStatement pstmt = null;

// get a connection and then in your try catch for executing your delete...

        try {
            pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, idWorker);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("MISTAKE");
            e.printStackTrace();
        }
        info.remove(indexObs);
    }

    public void editNote(Connection con, int indexObs, ObservableList info) {
        String SQL = "update worker set surname=?, name = ?, patronymic = ?, dateOfBirth = ?, passport = ?," +
                "address = ?, phone = ?, email =?  where idworker = ?";
        PreparedStatement preparedStmt = null;

        try {
            preparedStmt = con.prepareStatement(SQL);
            preparedStmt.setString(1, surname);
            preparedStmt.setString(2, name);                  //Model
            preparedStmt.setString(3, patronymic);   //Brand
            preparedStmt.setDate(4, dateOfBirth);
            preparedStmt.setLong(5, passport);
            preparedStmt.setString(6, address);
            preparedStmt.setLong(7, phone);
            preparedStmt.setString(8, email);
            preparedStmt.setInt(9, idWorker);
            preparedStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("MISTAKE");
            e.printStackTrace();
        }
        Worker tmp = (Worker) info.get(indexObs);
        tmp.setSurname(surname);
        tmp.setName(name);
        tmp.setPatronymic(patronymic);
        tmp.setDateOfBirth(dateOfBirth);
        tmp.setPassport(passport);
        tmp.setAddress(address);
        tmp.setPhone(phone);
        tmp.setEmail(email);


        //info.get(indexObs).setName(link.get(1).toString());
    }

    public ObservableList takeFIO(Connection con) {
        ArrayList<String> dir = new ArrayList<>();
        String prepSt = "select idworker, surname, name, patronymic from worker";
        try {
            PreparedStatement pr = con.prepareStatement(prepSt);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                dir.add(rs.getInt(1)+" -> "+rs.getString(2) + " " + rs.getString(3)+
                        " "+rs.getString(4));
                //System.out.println(rs.getInt(1)+rs.getString(2)+rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return FXCollections.observableArrayList(dir);
    }

    public void setIdWorker(int idWorker) {
        this.idWorker = idWorker;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setPassport(long passport) {
        this.passport = passport;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public int getIdWorker() {
        return idWorker;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public long getPassport() {
        return passport;
    }

    public String getAddress() {
        return address;
    }

    public long getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

}
