package main.java.ru.nsu.shchiptsov;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.Objects;

public abstract class MainWindowController implements Initializable {
	private Stage stage;
	private Connection connection = null;
	private ChooseRoleController.NameRole nameRole;
	private Integer idEmployee;

	public void setNameRole(ChooseRoleController.NameRole nameRole) {
		this.nameRole = nameRole;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Connection getConnection() {
		return connection;
	}

	public ChooseRoleController.NameRole getNameRole() {
		return nameRole;
	}

	public Stage getStage() {
		return stage;
	}

	public void showStage(FXMLLoader loader) {
		Parent root = loader.getRoot();
		Scene scene = new Scene (root);
		scene.getStylesheets().add(Objects.requireNonNull(
				getClass().getResource("/application.css")).toExternalForm());
		stage.setScene (scene);
		stage.show ();
	}

	protected void showStageTable(String nameFxmlFile) {
		FXMLLoader loader = new FXMLLoader ();
		loader.setLocation (getClass ().getResource (nameFxmlFile));
		try {
			loader.load ();
		} catch (IOException e) {
			e.printStackTrace();
		}
		MainWindowController mainWindowController = loader.getController();
		mainWindowController.setNameRole(getNameRole());
		mainWindowController.setStage(getStage());
		mainWindowController.setIdEmployee(getIdEmployee());
		mainWindowController.setConnection(getConnection());
		mainWindowController.initializeInitialState();
		showStage(loader);
	}

	public Integer getIdEmployee() {
		return idEmployee;
	}

	public void setIdEmployee(Integer idEmployee) {
		this.idEmployee = idEmployee;
	}


	protected void loadMainMenuWindow(ChooseRoleController.NameRole nameRole, Integer id) {
		FXMLLoader loader = new FXMLLoader ();
		loader.setLocation (getClass ().getResource ("/MainMenuWindow.fxml"));
		try {
			loader.load ();
		} catch (IOException e) {
			e.printStackTrace();
		}
		MainMenuWindowController mainMenuWindowController = loader.getController();
		mainMenuWindowController.setNameRole(nameRole);
		mainMenuWindowController.setRoleLabel();
		mainMenuWindowController.setStage(getStage());
		mainMenuWindowController.setIdEmployee(id);
		mainMenuWindowController.setConnection(getConnection());
		showStage(loader);
	}

	public void initializeInitialState() {}

}
