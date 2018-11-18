package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends Application {
    final static String url = "jdbc:mysql://127.0.0.1:3306/mydb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    final static String user = "root";
    final static String password = "123" ;
    Stage primaryStage;
    public static Connection con;
    static String curTable;

    @Override
    public void start(Stage primaryStage) throws Exception{

        try {
            con = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("ERROR");
            alert.setContentText("No connection to DataBase. Fix your url");
            alert.showAndWait();
            e.printStackTrace();
            return;
        }
        this.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/Tables.fxml"));
        primaryStage.setTitle("DataBase" );
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public Stage getStage()
    {
      return  primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
