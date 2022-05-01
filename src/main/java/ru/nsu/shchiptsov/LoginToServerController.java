package main.java.ru.nsu.shchiptsov;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.*;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static java.lang.System.exit;

public class LoginToServerController extends MainWindowController {
	@FXML
	private Button localHostButton;
	@FXML
	private Button nsuButton;
	@FXML
	private Button exitButton;
	private String user;
	private String pwd;
	private String url;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Oracle JDBC Driver is not found");
			e.printStackTrace();
			exit (-1);
		}
		localHostButton.setOnAction(event -> {
			this.url = "jdbc:oracle:thin:@//localhost:1521/ORCLCDB.localdomain";
			user = "dummy";
			pwd = "dummy";
			loginServer();
		});
		nsuButton.setOnAction(event -> {
			String sid  = "XE";
			int port = 1521;
			String host = "84.237.50.81";
			this.url = String.format("jdbc:oracle:thin:@%s:%d:%s", host, port, sid);
			user = "19212_SHSHIPTSOV";
			pwd  = "12345o";
			loginServer();
		});
		exitButton.setOnAction(event -> {
			exit(0);
		});
	}

	private void executeScriptInitializeDB() {
		try {
			ScriptRunner runner = new ScriptRunner(getConnection());
			Reader reader = new BufferedReader(new FileReader(
					getClass().getResource("/initialization.sql").getFile()));
			runner.runScript(reader);
			getConnection().commit();
		} catch (FileNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	private void loginServer() {
		try {
			setConnection(DriverManager.getConnection(url, user, pwd));
			//executeScriptInitializeDB();
			getConnection().setAutoCommit(false);
		} catch (SQLException e) {
			System.out.println("Connection Failed : " + e.getMessage());
			exit (-1);
		}
		if (getConnection() != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}

		FXMLLoader loader = new FXMLLoader ();
		loader.setLocation (getClass ().getResource ("/ChooseRole.fxml"));
		try {
			loader.load ();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ChooseRoleController chooseRoleController = loader.getController();
		chooseRoleController.setStage(getStage());
		chooseRoleController.setConnection(getConnection());
		showStage(loader);
	}

}
