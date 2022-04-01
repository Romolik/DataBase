package main.java.ru.nsu.shchiptsov;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.*;
import java.sql.*;
import java.sql.Connection;
import java.util.Objects;

import static java.lang.System.exit;

public class Main extends Application {
	private String pwd  = "12345o";
	private Connection connection = null;

	public static void main (String[] args) {
		launch (args);
	}

	public Connection getConnection() {
		return connection;
	}

	@Override
	public void start(Stage primaryStage) {
		String host = "84.237.50.81";
		int port = 1521;
		String sid  = "XE";
		String user = "19212_SHSHIPTSOV";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Oracle JDBC Driver is not found");
			e.printStackTrace();
			exit (-1);
		}

		//String url = String.format("jdbc:oracle:thin:@%s:%d:%s", host, port, sid);
		String url = ("jdbc:oracle:thin:@//localhost:1521/ORCLCDB.localdomain");
		user = "dummy";
		pwd = "dummy";
		try {
			connection = DriverManager.getConnection(url, user, pwd);
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			System.out.println("Connection Failed : " + e.getMessage());
			exit (-1);
		}
		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}
		primaryStage.setTitle ("DataBase");
		FXMLLoader loader = new FXMLLoader ();
		loader.setLocation (getClass ().getResource ("/MainWindow.fxml"));
		try {
			loader.load ();
		} catch (IOException e) {
			e.printStackTrace ();
		}
		MainController mainController = loader.getController();
		mainController.setConnection(connection);
		primaryStage.setOnCloseRequest (new EventHandler<WindowEvent>() {
			@Override
			public void handle (WindowEvent event) {
				event.consume ();
			}
		});
		Parent root = loader.getRoot();
		primaryStage.setResizable (false);
		primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		Scene scene = new Scene (root);
		scene.getStylesheets().add(Objects.requireNonNull(
				getClass().getResource("/application.css")).toExternalForm());
		primaryStage.setScene (scene);
		primaryStage.show ();

	}

}
